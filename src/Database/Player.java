package Database;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    private String Name;
    private Country Country;
    private int Age;
    private double Height;
    private Club Club;
    private String Position;
    private int Number;
    private double Weekly_Salary;
    private String SellStatus = "Not For Sale";

    public void setName(String Name){
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }


    public Country getCountry() {
        return Country;
    }

    public void setCountry(Country country) {
        this.Country = country;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        this.Age = age;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        this.Height = height;
    }

    public Club getClub() {
        return Club;
    }

    public void setClub(Club club) {
        this.Club = club;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        this.Position = position;
    }

    public double getWeekly_Salary() {
        return Weekly_Salary;
    }

    public void setWeekly_Salary(double weekly_Salary) {
        this.Weekly_Salary = weekly_Salary;
    }


    public void setNumber(int number) {
        this.Number = number;
    }

    public int getNumber(){
        return Number;
    }

    public void display(){

        System.out.println("Name: " + Name);
        System.out.println("Country: " + Country.getName());
        System.out.println("Age: " + Age);
        System.out.println("Height: " + Height);
        System.out.println("Club: " + Club.getName());
        System.out.println("Position: " + Position);
        System.out.println("Number: " + Number);
        System.out.println("Weekly Salary: " + Weekly_Salary);

    }

    @Override
    public boolean equals(Object o){
        if( o == this)
            return true;
        if( !(o instanceof Player) )
            return false;

        Player player = (Player) o;
        if( Name.equalsIgnoreCase(player.getName()) )
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name);
    }


    public String getSellStatus() {
        return SellStatus;
    }

    public void setSellStatus(String sellStatus) {
        SellStatus = sellStatus;
    }
}


