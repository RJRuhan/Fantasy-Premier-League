package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Club implements Serializable {
    final int capacity = 7;
    final private String Name;
    private Player[] players;
    private int curr_num_of_players;
    private List<Player> playerList;

    Club(String Name){
        this.Name = Name;
        players = new Player[capacity];
        playerList = new ArrayList<>();
        curr_num_of_players = 0;
    }

    public String getName() {
        return Name;
    }

    public boolean isFull(){
        return capacity == curr_num_of_players;
    }

    public boolean addPlayer(Player p){

        if( this.isFull() )
            return false;

        players[curr_num_of_players] = p;
        curr_num_of_players++;
        playerList.add(p);

        return true;
    }

    public void display(){
        System.out.println("Club: " + Name);
        for( int i = 0;i < curr_num_of_players;i++ )
            System.out.println(players[i].getName());
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<Player> maximum_salary(){

        List<Player> Players = new ArrayList<>();
        double max_salary = -1;

        for( Player p : playerList ){
            if( p.getWeekly_Salary() > max_salary )
                max_salary = p.getWeekly_Salary();
        }

        for( Player p : playerList ){
            if( p.getWeekly_Salary() == max_salary ){
                Players.add(p);
            }
        }

        return Players;
    }

    public List<Player> maximum_age(){

        List<Player> Players = new ArrayList<>();
        double max_age = -1;

        for( Player p : playerList ){
            if( p.getAge() > max_age )
                max_age = p.getAge();
        }

        for( Player p : playerList ){
            if( p.getAge() == max_age ){
                Players.add(p);
            }
        }

        return Players;
    }

    public List<Player> maximum_height(){

        List<Player> Players = new ArrayList<>();
        double max_height = -1;

        for( Player p : playerList ){
            if( p.getHeight() > max_height )
                max_height = p.getHeight();
        }

        for( Player p : playerList ){
            if( p.getHeight() == max_height ){
                Players.add(p);
            }
        }

        return Players;
    }

    public double total_yearly_salary(){

        double total_salary = 0;

        for( Player p : playerList ){
            total_salary += p.getWeekly_Salary();
        }

        return total_salary*52;
    }

    public void remove_player(Player player){

        boolean success = playerList.remove(player);

        if( !success ){
            System.out.println("Error:No such Player in playerList to Remove");
            return;
        }

        for( int i = 0 ;i < curr_num_of_players;i++ ){
            if( players[i].getName().equalsIgnoreCase(player.getName()) ){
                for( int j = i + 1;j < curr_num_of_players;j++ ){
                    players[j-1] = players[j];
                }
                players[curr_num_of_players-1] = null;
                curr_num_of_players--;
                break;
            }

        }

    }



}
