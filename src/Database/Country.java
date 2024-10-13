package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Country implements Serializable {
    final private String Name;
    private List<Player> playerList;
    private int Number_of_players;

    Country(String Name){
        this.Name = Name;
        playerList = new ArrayList<>();
    }

    @Override
    public String toString(){
        return Name;
    }

    public boolean addPlayer(Player p){

        playerList.add(p);
        Number_of_players++;

        return true;
    }

    public void display(){
        System.out.println("Country: " + Name);
        for( Player p : playerList )
            System.out.println(p.getName());
    }

    public String getName() {
        return Name;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public int getNumber_of_players() {
        return Number_of_players;
    }

    public void setNumber_of_players(int number_of_players) {
        this.Number_of_players = number_of_players;
    }
}
