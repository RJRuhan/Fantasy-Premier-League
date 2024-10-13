package Javafx;

import Database.Player;
import Database.database_management;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.util.*;

public class HomePage{

    private NetworkUtil networkUtil;
    private String clientName;
    SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    private database_management data;
    HashMap<Player,Double> BuyRequestPlayerMap;
    HashMap<Player,Double> SellRequestPlayerMap;
    SimpleObjectProperty<HashMap<Player,Double>> BuyPlayerPriceMapProperty;
    SimpleBooleanProperty booleanProperty;

    private Stage window;
    private HomePageController controller;


    public HomePage(NetworkUtil networkUtil,Stage PrimaryStage) throws Exception{

        this.networkUtil = networkUtil;

        new ReadThreadClient(networkUtil, this);

        data = new database_management();
        BuyRequestPlayerMap = new HashMap<>();
        SellRequestPlayerMap = new HashMap<>();
        BuyPlayerPriceMapProperty = new SimpleObjectProperty<>();
        booleanProperty = new SimpleBooleanProperty(true);

        window = PrimaryStage;
        window.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        window.setScene(new Scene(root,900,650));

        controller = loader.getController();
        controller.setHomePage(this);
        controller.init();


        window.show();

    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn.set(loggedIn);
    }


    public database_management getData() {
        return data;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public HomePageController getController() {
        return controller;
    }

    public Stage getWindow() {
        return window;
    }


    //    public void run() {
//        try {
//            Scanner input = new Scanner(System.in);
//            while (true) {
//                System.out.println("1. Register");
//                System.out.println("2. Login");
//                System.out.println("3. Sell");
//                System.out.println("4. Show Players put up on sell");
//                System.out.println("5. Show Players to Buy");
//                System.out.println("6. Buy A player");
//                System.out.println("7. All players");
//                System.out.print("Your option: ");
//                int option = input.nextInt();
//                input.nextLine();
//                if (option == 1) {
//
//                } else if (option == 2) {
//
//                } else if (option == 3) {
//
//
//
//                } else if (option == 4) {
//                    if (!isLoggedIn) {
//                        System.out.println("Please login first");
//                        continue;
//                    }
//                    //showSellPlayers();
//
//                } else if (option == 5) {
//                    if (!isLoggedIn) {
//                        System.out.println("Please login first");
//                        continue;
//                    }
//                    //showBuyPlayers();
//
//                }else if( option == 6 ){
//                    if (!isLoggedIn) {
//                        System.out.println("Please login first");
//                        continue;
//                    }
//                    System.out.println("Enter the Player's Name : ");
//                    String pname = input.nextLine();
//
//                    //SendBuyPlayerListMessage(pname);
//
//
//                }
//                else if( option == 7 ) {
//                    if (!isLoggedIn) {
//                        System.out.println("Please login first");
//                        continue;
//                    }
//                    List<Player> playerList = data.search_players_by_club(clientName);
//                    for( Player p : playerList ){
//                        p.display();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            try {
//                networkUtil.closeConnection();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
