package Javafx;

import Database.Player;
import dto.PlayerListMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.NetworkUtil;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;


public class ReadThreadClient implements Runnable {
    private Thread thr;
    private NetworkUtil networkUtil;
    private HomePage homePage;

    public ReadThreadClient(NetworkUtil networkUtil, HomePage homePage) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        this.homePage = homePage;
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                if (o instanceof String) {
                    // Login Response
                    String s = (String) o;
                    if (s.equals("success")) {

                        homePage.setLoggedIn(true);
                        homePage.getController().Username_TextField.clear();
                        homePage.getController().Password_TextField.clear();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    InputStream stream = null;
                                    stream = new FileInputStream(
                                             homePage.getClientName().toLowerCase() + ".jpg");
                                    Image image = new Image(stream);
                                    homePage.getController().ClubImage.setImage(image);
                                    homePage.getController().ClubImage.setFitWidth(228);
                                    homePage.getController().ClubImage.setFitHeight(153);
                                    homePage.getController().ClubImage.setPreserveRatio(false);
                                    if( homePage.getClientName() != null ){
                                        homePage.getController().ClubNameLabel.setText(homePage.getClientName().toUpperCase());
                                    }
                                } catch (FileNotFoundException e) {
                                    homePage.getController().ClubImage.setImage(null);
                                }

                            }
                        });

                    } else {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String msg = "";
                                if( s.equals("failure_username") )
                                    msg = "No such club with this username";
                                else if( s.equals("failure_password") )
                                    msg = "Wrong Password";
                                else if( s.equals("failure_already_logged_in") )
                                    msg = "Already Logged In with another device";
                                else if( s.equals("Already Registered") ){
                                    msg = "This Club is Already Signed Up";
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Sign up Error!");
                                    alert.setHeaderText(msg);
                                    //alert.setContentText();
                                    alert.showAndWait();
                                    return;
                                }


                                ShowAlertBox(msg);

                            }
                        });
                        homePage.setLoggedIn(false);
                    }
                }
                if( o instanceof PlayerListMessage){
                    PlayerListMessage playerListMessage = (PlayerListMessage) o;
                    if( playerListMessage.getMessageType() == PlayerListMessage.PlayerListMessageType.NoType ){
                        homePage.getData().setPlayers(playerListMessage);
                        updateSellRequestPlayerMap();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                homePage.getController().showAllPlayer(null);
                                homePage.getController().playerImage.setImage(null);
                                homePage.getController().PlayerNameLabel.setText("");
                                if( !homePage.getController().Total_Yearly_Salary_TextField.getText().equals("") ){
                                    homePage.getController().Total_Yearly_Salary(null);
                                }
                            }
                        });

                    }
                    else if( playerListMessage.getMessageType() == PlayerListMessage.PlayerListMessageType.BuyType ){
                        homePage.BuyRequestPlayerMap = playerListMessage.getPlayerPriceMap();
                        homePage.booleanProperty.set(!homePage.booleanProperty.get());
                    }
                }
            }
        }catch (IOException e){
        }
        catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
                if( !homePage.getController().exit ){
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Server Stopped!");
                            alert.setHeaderText("Connection Lost or the Server has Stopped Working");
                            alert.showAndWait();
                            try {
                                Thread.sleep(1000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            System.exit(0);
                        }
                    });
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSellRequestPlayerMap() {
        HashMap<Player,Double> NewSellRequestPlayerMap = new HashMap<>();

        for( Player p : homePage.getData().getPlayers() ){
            if( homePage.SellRequestPlayerMap.get(p) != null )
                NewSellRequestPlayerMap.put(p,homePage.SellRequestPlayerMap.get(p));
        }

        homePage.SellRequestPlayerMap = NewSellRequestPlayerMap;

    }

    private void showBuyplayers(){
        Iterator<Player> itr2 = homePage.BuyRequestPlayerMap.keySet().iterator();
        while (itr2.hasNext()) {
            Player p = itr2.next();

            System.out.println(p.getName() + "  -  " + homePage.BuyRequestPlayerMap.get(p));
        }
    }

    public void ShowAlertBox(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Log In Error!");
        alert.setHeaderText(msg);
        //alert.setContentText();
        alert.showAndWait();
    }
}



