package dto;

import Database.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PlayerListMessage implements Serializable {

    private List<Player> playerList;
    private List<String> playerCountryNames;
    private String ClubName;
    private HashMap<Player,Double> playerPriceMap;

    public List<String> getPlayerCountryNames() {
        return playerCountryNames;
    }

    public void setPlayerCountryNames(List<String> playerCountryNames) {
        this.playerCountryNames = playerCountryNames;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public HashMap<Player, Double> getPlayerPriceMap() {
        return playerPriceMap;
    }

    public void setPlayerPriceMap(HashMap<Player, Double> playerPriceMap) {
        this.playerPriceMap = playerPriceMap;
    }

    public enum PlayerListMessageType {
        SellType, BuyType, NoType;
    }


    private PlayerListMessageType messageType;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public PlayerListMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(PlayerListMessageType messageType) {
        this.messageType = messageType;
    }
}
