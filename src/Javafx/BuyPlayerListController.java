package Javafx;

import Database.Player;
import dto.PlayerListMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;


public class BuyPlayerListController {

    private BuyPlayerListPage buyPlayerListPage;

    @FXML
    private TableView<Map.Entry<Player,Double>> BuyplayerTableView;

    @FXML private TableColumn<Map.Entry<Player,Double>,String> Name;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Club;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Country;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Age;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Height;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Position;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Number;
    @FXML private TableColumn<Map.Entry<Player,Double>,String> Weekly_Salary;
    @FXML private  TableColumn<Map.Entry<Player,Double>,String> Price;

    public void BuyButtonClicked(ActionEvent actionEvent) throws Exception {

        HashMap<Player,Double> buyPlayerMap = new HashMap<>();

        for( Map.Entry<Player,Double> entry : BuyplayerTableView.getSelectionModel().getSelectedItems() ){
            buyPlayerMap.put(entry.getKey(),entry.getValue());
        }

        SendBuyPlayerListMessage(buyPlayerMap);
        BuyplayerTableView.getItems().removeAll(BuyplayerTableView.getSelectionModel().getSelectedItems());

    }

    private void SendBuyPlayerListMessage(HashMap<Player,Double> BuyPlayerMap) throws Exception{

        List<Player> BuyPlayerList = new ArrayList<>(BuyPlayerMap.keySet());

        if( BuyPlayerList.isEmpty() )
            return;

        PlayerListMessage BuyPlayerListMessage = new PlayerListMessage();
        BuyPlayerListMessage.setMessageType(PlayerListMessage.PlayerListMessageType.BuyType);
        BuyPlayerListMessage.setPlayerList(BuyPlayerList);
        BuyPlayerListMessage.setPlayerPriceMap(BuyPlayerMap);
        BuyPlayerListMessage.setClubName(buyPlayerListPage.homePage.getClientName());

        buyPlayerListPage.homePage.getNetworkUtil().write(BuyPlayerListMessage);



    }

    public void GoBackButtonClicked(ActionEvent actionEvent) {
        buyPlayerListPage.getWindow().close();
    }

    public void setBuyPlayerListPage(BuyPlayerListPage buyPlayerListPage) {
        this.buyPlayerListPage = buyPlayerListPage;
    }

    public void init() {

        BuyplayerTableView.setPlaceholder(new Label("No rows to display"));
        BuyplayerTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        Name.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(KeyValuePair.getValue().getKey().getName()));
        Club.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(KeyValuePair.getValue().getKey().getClub().getName()));
        Country.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(KeyValuePair.getValue().getKey().getCountry().getName()));
        Age.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(String.valueOf(KeyValuePair.getValue().getKey().getAge())));
        Height.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(String.valueOf(KeyValuePair.getValue().getKey().getHeight())));
        Position.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(KeyValuePair.getValue().getKey().getPosition()));
        Number.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(String.valueOf(KeyValuePair.getValue().getKey().getNumber())));
        Weekly_Salary.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(String.valueOf(KeyValuePair.getValue().getKey().getWeekly_Salary())));
        Price.setCellValueFactory(KeyValuePair -> new SimpleStringProperty(String.valueOf(KeyValuePair.getValue().getValue())));

        ObservableList<Map.Entry<Player,Double>> entryObservableList = FXCollections.observableArrayList(buyPlayerListPage.homePage.BuyRequestPlayerMap.entrySet());

        BuyplayerTableView.setItems(entryObservableList);

        buyPlayerListPage.homePage.booleanProperty.addListener((observableValue, oldvalue,newvalue) -> {
            BuyplayerTableView.setItems(FXCollections.observableArrayList(buyPlayerListPage.homePage.BuyRequestPlayerMap.entrySet()));
        });

    }


}
