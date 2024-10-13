package server;

import Database.CaseInsensitiveHashMap;
import Database.Player;
import Database.database_management;
import util.NetworkUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class Stop implements Runnable{

    Server server;

    Stop(Server server){
        this.server = server;
        new Thread(this).start();
    }
    @Override
    public void run() {
        while(true){
            System.out.println("Press Q to exit");
            Scanner input = new Scanner(System.in);
            String Line = input.nextLine();
            if( Line.equals("Q") ){
                try {
                    server.getserverSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}

public class Server {

    private ServerSocket serverSocket;
    public CaseInsensitiveHashMap<ClientInfo> clientMap;
    public CaseInsensitiveHashMap<ClientInfo> registerMap;
    database_management data;
    public HashMap<Player,Double> SellRequestPlayerMap;
    private List<NetworkUtil> networkUtilList;

    Server() {

        data = new database_management("players.txt");
        SellRequestPlayerMap = new HashMap<>();
        boolean success = data.readFile();

        if( success ){

        }else{
            System.out.println("File read Unsuccessful");
            return;
        }

        clientMap = new CaseInsensitiveHashMap<>();
        registerMap = new CaseInsensitiveHashMap<>();
        networkUtilList = new ArrayList<>();

        ReadRegisterMap("Register.txt");

        new Stop(this);
        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            //System.out.println("server.Server starts:" + e);
            for( NetworkUtil networkUtil : networkUtilList  ) {
                try {
                    networkUtil.closeConnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            WriteRegisterMap("Register.txt");

        }



    }

    public ServerSocket getserverSocket() {
        return serverSocket;
    }

    public void serve(Socket clientSocket) throws IOException, ClassNotFoundException {
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        networkUtilList.add(networkUtil);
        new ReadThreadServer(clientMap, networkUtil,this);
    }

    public static void main(String args[]) {
        Server server = new Server();
    }

    public HashMap<Player, Double> getSellRequestPlayerMap() {
        return SellRequestPlayerMap;
    }

    public void setSellRequestPlayerMap(HashMap<Player, Double> sellRequestPlayerMap) {
        SellRequestPlayerMap = sellRequestPlayerMap;
    }

    public boolean ReadRegisterMap(String INPUT_FILE_NAME){
        try{
            String Line;
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));

            while( true ){
                Line = br.readLine();
                if( Line == null )
                    break;
                String []info = Line.split(",");

                if( info.length != 2 )
                    return false;

                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setName(info[0]);
                clientInfo.setPassword(info[1]);
                clientInfo.setOnline(false);

                registerMap.put(info[0],clientInfo);
            }

            br.close();


        }catch(Exception e){
            System.out.println(e);
            return false;
        }

        return true;
    }

    public boolean WriteRegisterMap(String OUTPUT_FILE_NAME){

        try{

            BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));

            Iterator<String> itr = registerMap.keySet().iterator();
            while( itr.hasNext() ){
                String ClientName = itr.next();
                ClientInfo clientInfo = registerMap.get(ClientName);

                bw.write(clientInfo.getName() + "," + clientInfo.getPassword());
                bw.write("\n");
            }

            bw.close();

            return true;

        }catch (Exception e){
            System.out.println(e);
            return false;
        }

    }
}
