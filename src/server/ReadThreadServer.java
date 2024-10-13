package server;

import Database.CaseInsensitiveHashMap;
import Database.Club;
import Database.Player;
import Database.database_management;
import dto.*;
import util.NetworkUtil;

import java.io.IOException;
import java.util.*;

import dto.PlayerListMessage.PlayerListMessageType;

public class ReadThreadServer implements Runnable {

    private Thread thr;
    private NetworkUtil networkUtil;
    public CaseInsensitiveHashMap<ClientInfo> clientMap;
    Server server;
    private String clientName;


    public ReadThreadServer(CaseInsensitiveHashMap<ClientInfo> map, NetworkUtil networkUtil,Server server) {
        this.clientMap = map;
        this.networkUtil = networkUtil;
        this.server = server;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                System.out.println(o);
                if (o instanceof RegisterMessage) {
                    RegisterMessage obj = (RegisterMessage) o;

                    ClientInfo clientInfo = server.registerMap.get(obj.getName());
                    if( clientInfo != null ){
                        networkUtil.write("Already Registered");
                        continue;
                    }

                    clientInfo = new ClientInfo();
                    clientInfo.setName(obj.getName());
                    clientInfo.setPassword(obj.getPassword());
                    clientInfo.setOnline(false);
                    clientInfo.setNetworkUtil(networkUtil);

                    clientMap.put(obj.getName(), clientInfo);
                    server.registerMap.put(obj.getName(),clientInfo);
                }
                if (o instanceof LoginMessage) {
                    LoginMessage obj = (LoginMessage) o;
                    ClientInfo clientInfo = server.registerMap.get(obj.getName());
                    if (clientInfo != null) {
                        if (clientInfo.getPassword().equals(obj.getPassword())) {
                            ClientInfo NewClientInfo = clientMap.get(obj.getName());

                            if( NewClientInfo == null ){
                                NewClientInfo = new ClientInfo();
                                NewClientInfo.setName(obj.getName());
                                NewClientInfo.setPassword(obj.getPassword());
                                NewClientInfo.setOnline(false);
                                NewClientInfo.setNetworkUtil(networkUtil);
                                clientMap.put(obj.getName(), NewClientInfo);
                            }


                            if( NewClientInfo.isOnline() ){
                                networkUtil.write("failure_already_logged_in");
                                continue;
                            }

                            clientName = obj.getName();

                            NewClientInfo.setNetworkUtil(networkUtil);
                            NewClientInfo.setOnline(true);
                            networkUtil.write("success");

                            sendPlayerListMessage(obj.getName(),networkUtil);
                            sendSellRequestPlayerMap(networkUtil);

                        } else {
                            networkUtil.write("failure_password");
                        }
                    }
                    else
                        networkUtil.write("failure_username");
                }
                if( o instanceof LogOutMessage ){
                    LogOutMessage obj = (LogOutMessage) o;
                    ClientInfo clientInfo = clientMap.get(obj.getName());
                    if( clientInfo != null ){
                        clientInfo.setOnline(false);
                    }
                }
                if( o instanceof PlayerListMessage ){
                    PlayerListMessage playerListMessage = (PlayerListMessage) o;
                    if( playerListMessage.getMessageType() == PlayerListMessageType.SellType ){
                        addToSellRequestPlayerMap(playerListMessage.getPlayerPriceMap());
                        SendToAllClientUpdatedSellRequestPlayerList();
                    }
                    else if( playerListMessage.getMessageType() == PlayerListMessageType.BuyType ){
                        completeBuy(playerListMessage.getPlayerList(),playerListMessage.getClubName());
                        SendToAllClientUpdatedSellRequestPlayerList();
                    }
                }

            }
        } catch (Exception e) {
            if( clientName != null ){
                ClientInfo clientInfo = clientMap.get(clientName);
                if( clientInfo != null )
                    clientInfo.setOnline(false);
            }

        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void sendSellRequestPlayerMap(NetworkUtil networkUtil) throws Exception {
        PlayerListMessage SellRequestPlayerListMessage = new PlayerListMessage();
        SellRequestPlayerListMessage.setPlayerPriceMap(server.SellRequestPlayerMap);
        SellRequestPlayerListMessage.setMessageType(PlayerListMessageType.BuyType);

        networkUtil.write(SellRequestPlayerListMessage);
    }

    private void completeBuy(List<Player> playerList,String ClubName) throws Exception {
        for( Player p : playerList ){
            Double check = server.SellRequestPlayerMap.remove(p);
            if( check == null ){
                System.out.println("Error:No Match for buy player request");
                continue;
            }
            server.data.search_player_by_Name(p.getName()).setSellStatus("Not For Sale");
        }

        update_data(server.data,playerList,ClubName);
    }

    private void update_data(database_management data, List<Player> playerList, String clubName) throws Exception {
        HashMap<String,Player> PlayerMap = new HashMap<>();

        for( Player p : data.getPlayers() )
            PlayerMap.put(p.getName(),p);

        for( Player p : playerList ){
            Player player = PlayerMap.get(p.getName());
            if( player == null ){
                System.out.println("Error:Buy Player Not found in database");
                continue;
            }

            player.getClub().remove_player(player);
            Club club = data.getClubMap().get(clubName);
            club.addPlayer(player);
            player.setClub(club);

        }

        update_transfer_clubs_database(playerList,clubName);

    }

    private void update_transfer_clubs_database(List<Player> playerList,String ClubName) throws Exception {
        for( Player p : playerList ){
            ClientInfo clientInfo = clientMap.get(p.getClub().getName());
            if( clientInfo == null ){
                System.out.println("Error:No match for buy player club");
                return;
            }
            sendPlayerListMessage(p.getClub().getName(),clientInfo.getNetworkUtil());
        }
        sendPlayerListMessage(ClubName,networkUtil);
    }

    private void SendToAllClientUpdatedSellRequestPlayerList() throws Exception {
        Iterator<String> itr = clientMap.keySet().iterator();

        while( itr.hasNext() ){
            ClientInfo clientInfo = clientMap.get(itr.next());

            if( !clientInfo.isOnline() )
                continue;

            PlayerListMessage SellPlayerListMessage = new PlayerListMessage();
            SellPlayerListMessage.setMessageType(PlayerListMessageType.BuyType);

            HashMap<Player,Double> NewSellRequestPlayerMap = new HashMap<>();

            Iterator<Player> itr2 = server.SellRequestPlayerMap.keySet().iterator();
            while (itr2.hasNext()) {
                Player p = itr2.next();
                if( !p.getClub().getName().equalsIgnoreCase(clientInfo.getName()) )
                    NewSellRequestPlayerMap.put(p,server.SellRequestPlayerMap.get(p));
            }

            SellPlayerListMessage.setPlayerPriceMap(NewSellRequestPlayerMap);
            clientInfo.getNetworkUtil().write(SellPlayerListMessage);
        }

    }

    private void addToSellRequestPlayerMap(HashMap<Player, Double> SellPlayerPriceMap) {

        Iterator<Player> itr = SellPlayerPriceMap.keySet().iterator();
        while (itr.hasNext()) {
            Player p = itr.next();
            server.SellRequestPlayerMap.put(p,SellPlayerPriceMap.get(p));
            server.data.search_player_by_Name(p.getName()).setSellStatus("Up For Sale");
        }


    }

    private void sendPlayerListMessage(String ClubName,NetworkUtil networkUtil) throws Exception{

        ClientInfo clientInfo = clientMap.get(ClubName);

        if( clientInfo == null || !clientInfo.isOnline() )
            return;

        PlayerListMessage playerListSendMessage = new PlayerListMessage();

        List<Player> playerList = server.data.search_players_by_club(ClubName);

        if( playerList == null || playerList.isEmpty() )
            return;

        List<Player> newPlayerList = new ArrayList<>();
        List<String> CountryNames = new ArrayList<>();

        for( Player p : playerList ){
            Player newPlayer = new Player();

            newPlayer.setName(p.getName());
            newPlayer.setAge(p.getAge());
            newPlayer.setHeight(p.getHeight());
            newPlayer.setPosition(p.getPosition());
            newPlayer.setNumber(p.getNumber());
            newPlayer.setWeekly_Salary(p.getWeekly_Salary());
            newPlayer.setSellStatus(p.getSellStatus());

            CountryNames.add(p.getCountry().getName());
            newPlayerList.add(newPlayer);

        }

        playerListSendMessage.setClubName(playerList.get(0).getClub().getName());
        playerListSendMessage.setPlayerList(newPlayerList);
        playerListSendMessage.setMessageType(PlayerListMessageType.NoType);
        playerListSendMessage.setPlayerCountryNames(CountryNames);

        networkUtil.write(playerListSendMessage);
    }

    private void showSellPlayers(){
        Iterator<Player> itr2 = server.SellRequestPlayerMap.keySet().iterator();
        while (itr2.hasNext()) {
            Player p = itr2.next();

            System.out.println(p.getName() + "  -  " + server.SellRequestPlayerMap.get(p));
        }
    }
}



