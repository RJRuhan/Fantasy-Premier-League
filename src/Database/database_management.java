package Database;

import dto.PlayerListMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class database_management {

    private List<Player> players ;
    private List<Country> countries;
    private String INPUT_FILE_NAME ;
    private String OUTPUT_FILE_NAME;
    private CaseInsensitiveHashMap<Country> CountryMap;
    private CaseInsensitiveHashMap<Club> ClubMap;

    public database_management(String filename){
        INPUT_FILE_NAME = filename;
        OUTPUT_FILE_NAME = filename;

        players = new ArrayList<>();
        countries = new ArrayList<>();

        ClubMap = new CaseInsensitiveHashMap<>();
        CountryMap = new CaseInsensitiveHashMap<>();

    }

    public database_management(){
        players = new ArrayList<>();
        countries = new ArrayList<>();

        ClubMap = new CaseInsensitiveHashMap<>();
        CountryMap = new CaseInsensitiveHashMap<>();

        INPUT_FILE_NAME = "";
        OUTPUT_FILE_NAME = "";
    }

    public void setPlayers(PlayerListMessage playerListMessage) {

        if( playerListMessage == null  )
            return;

        List<Player> playerList = playerListMessage.getPlayerList();
        List<String> CountryNames = playerListMessage.getPlayerCountryNames();

        if( playerList == null || playerList.isEmpty() || CountryNames == null ){
            return;
        }

        if( CountryNames.size() != playerList.size() )
            return;

        CountryMap.clear();
        ClubMap.clear();
        countries.clear();

        this.players = playerList;

        int i = 0;
        for( Player p : players ){

            p.setCountry(create_country(p,CountryNames.get(i)));
            p.setClub(create_club(p,playerListMessage.getClubName()));
            i++;
        }

    }

    public List<Player> getPlayers() {
        return players;
    }

    private boolean readPlayer(String Line){
        String[] attributes = Line.split(",");
        Player p = new Player();

        if( attributes.length != 8 )
            return false;

        p.setName(attributes[0]);
        p.setAge(Integer.parseInt(attributes[2]));
        p.setHeight(Double.parseDouble(attributes[3]));
        p.setPosition(attributes[5]);
        p.setNumber(Integer.parseInt(attributes[6]));
        p.setWeekly_Salary(Double.parseDouble(attributes[7]));

        Club club = create_club(p,attributes[4]);

        if( club == null )
            return false;

        Country country = create_country(p,attributes[1]);

        p.setClub(club);
        p.setCountry(country);

        players.add(p);

        return true;
    }

    private Club create_club(Player p,String clubName){
        Club findClub = ClubMap.get(clubName);

        if( findClub == null ){
            findClub = new Club(clubName);
            ClubMap.put(clubName,findClub);
        }


        boolean success = findClub.addPlayer(p);

        if( !success )
            return null;

        return findClub;

    }

    private Country create_country(Player p,String CountryName){
        Country findCountry = CountryMap.get(CountryName);

        if( findCountry == null ){
            findCountry = new Country(CountryName);
            countries.add(findCountry);
            CountryMap.put(CountryName,findCountry);
        }

        findCountry.addPlayer(p);
        return findCountry;

    }

    private void create_country2(Player p){
        Country findCountry = CountryMap.get(p.getCountry().getName());

        if( findCountry == null ){
            findCountry = p.getCountry();
            countries.add(findCountry);
            CountryMap.put(p.getCountry().getName(),findCountry);
        }


    }

    public boolean readFile(){
        try{
            String Line;
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));

            while( true ){
                Line = br.readLine();
                if( Line == null )
                    break;

                if ( !readPlayer(Line) ){
                    System.out.println("Error:Input File may be corrupted");
                    return false;
                }

            }

            br.close();


        }catch(Exception e){
            System.out.println(e);
            return false;
        }

        return true;

    }


    public boolean writeFile(){

        try{

            BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));

            for (Player p : players) {
                bw.write(p.getName()+ "," + p.getCountry().getName() + "," + p.getAge() + "," + p.getHeight() + ','+
                        p.getClub().getName() + ',' + p.getPosition() + ',' + p.getNumber() + ',' + p.getWeekly_Salary() );
                bw.write("\n");
            }

            bw.close();

            return true;

        }catch (Exception e){
            System.out.println(e);
            return false;
        }

    }

    public Player search_player_by_Name(String Name){

        for( Player p : players ){
            if( p.getName().equalsIgnoreCase(Name) )
                return p;
        }

        return null;
    }

    public List<Player> search_players_by_country(String Country){

        Country country = CountryMap.get(Country);

        if( country == null )
            return  null;

        return country.getPlayerList();
    }

    public List<Player> search_players_by_club(String ClubName){

        Club club = ClubMap.get(ClubName);

        if( club == null )
            return  null;

        return club.getPlayerList();
    }

    public List<Player> search_players_by_country_club(String Country,String Club){

        if( Country.equalsIgnoreCase("ANY") ){
            return search_players_by_club(Club);

        }
        else if( Club.equalsIgnoreCase("ANY") ){
            return search_players_by_country(Country);
        }
        else{
            List<Player> Players = new ArrayList<>();
            List<Player> p1 = search_players_by_club(Club);

            if( p1 == null )
                return null;

            for( Player p : p1 ){
                if( p.getCountry().getName().equalsIgnoreCase(Country) )
                    Players.add(p);
            }

            if( Players.isEmpty() )
                return null;

            return Players;


        }

    }


    public List<Player> search_players_by_position(String Position){

        List<Player> Players = new ArrayList<>();

        for( Player p : players ){
            if( p.getPosition().equalsIgnoreCase(Position) ){
                Players.add(p);
            }
        }

        if(Players.isEmpty())
            return null;

        return Players;
    }

    public List<Player> search_player_by_salary_range( double range1,double range2 ){

        List<Player> Players = new ArrayList<>();

        for( Player p : players ){
            if( p.getWeekly_Salary() >= range1 && p.getWeekly_Salary() <= range2 ){
                Players.add(p);
            }
        }

        if(Players.isEmpty())
            return null;

        return Players;

    }


    public List<Player> maximum_salary_of_a_club(String ClubName){

        Club c = ClubMap.get(ClubName);

        if( c == null ){
            return null;
        }

        return c.maximum_salary();
    }

    public List<Player> maximum_age_of_a_club(String ClubName){

        Club c = ClubMap.get(ClubName);

        if( c == null ){
            return null;
        }

        return c.maximum_age();
    }

    public List<Player> maximum_height_of_a_club(String ClubName){

        Club c = ClubMap.get(ClubName);

        if( c == null ){
            return null;
        }

        return c.maximum_height();

    }

    public double total_yearly_salary_of_a_club(String ClubName){

        Club c = ClubMap.get(ClubName);

        if(c == null)
            return -1;

        return c.total_yearly_salary();
    }

    public boolean addPlayer(Player p,String clubName,String countryName){

        Club club = create_club(p,clubName);
        if( club == null )
            return false;

        Country country = create_country(p,countryName);

        p.setClub(club);
        p.setCountry(country);

        players.add(p);

        return true;
    }

    public List<Country> getCountries() {

        if( countries.isEmpty() )
            return null;

        return countries;
    }

    public CaseInsensitiveHashMap<Club> getClubMap(){
        return ClubMap;
    }

}
