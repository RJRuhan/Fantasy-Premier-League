package Javafx;

import Database.Country;
import Database.Player;
import dto.LogOutMessage;
import dto.LoginMessage;
import dto.PlayerListMessage;
import dto.RegisterMessage;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.NetworkUtil;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class HomePageController{

    private HomePage homePage;
    private NetworkUtil networkUtil;
    boolean exit;


    @FXML ImageView playerImage;
    @FXML ImageView ClubImage;
    @FXML Label PlayerNameLabel;
    @FXML Label ClubNameLabel;
    @FXML private Button SignupButton;
    @FXML private Button LoginButton;
    @FXML private Button LogOutButton;
    @FXML private Button BuyButton;
    @FXML private Button SellButton;
    @FXML  TextField Username_TextField;
    @FXML  TextField Password_TextField;

    @FXML TextField Total_Yearly_Salary_TextField;

    @FXML
    private TableView<Player> playerTableView;

    @FXML private TableColumn<Player,String> Name;
    @FXML private TableColumn<Player,String> Country;
    @FXML private TableColumn<Player,Integer> Age;
    @FXML private TableColumn<Player,Double> Height;
    @FXML private TableColumn<Player,String> Position;
    @FXML private TableColumn<Player,Integer> Number;
    @FXML private TableColumn<Player,Double> Weekly_Salary;
    @FXML private TableColumn<Player,String> SellStatus;

    @FXML
    private Button show_all_player_button;

    @FXML
    private MenuItem Search_Player_By_Salary_Range_MenuItem;

    @FXML
    private MenuItem Search_Player_By_Position_MenuItem;

    @FXML
    private MenuItem Search_Player_By_Country_MenuItem;

    @FXML
    private MenuItem Search_Player_By_Name_MenuItem;

    @FXML
    private MenuItem Maximum_Salary_MenuItem;

    @FXML
    private MenuItem Maximum_Age_MenuItem;
    @FXML
    private MenuItem Maximum_Height_MenuItem;
    @FXML
    private MenuItem Total_Yearly_Salary_MenuItem;

    @FXML
    private MenuItem CountryWise_Player_Count_MenuItem;

    public void Search_Player_By_Name (ActionEvent actionEvent) throws Exception {
        InputBox inputBox = new InputBox(searchType.ByName);

        String Name = inputBox.getInfo();

        if( Name == null)
            return;

        clearTable();

        Player p = homePage.getData().search_player_by_Name(Name);
        if( p != null ){
            playerTableView.getItems().add(p);
        }
        show_all_player_button.setDisable(false);
    }

    private void clearTable() {
        playerTableView.getItems().clear();
    }

    public void Search_Player_By_Country(ActionEvent actionEvent) throws Exception{
        InputBox inputBox = new InputBox(searchType.ByCountry);
        String Country = inputBox.getInfo();
        if( Country == null)
            return;


        playerTableView.setItems(createObservablePlayerList(homePage.getData().search_players_by_country(Country)));
        show_all_player_button.setDisable(false);
    }

    public void Search_Player_By_Position(ActionEvent actionEvent) throws Exception{
        InputBox inputBox = new InputBox(searchType.ByPosition);
        String Position = inputBox.getInfo();
        if( Position == null)
            return;


        playerTableView.setItems(createObservablePlayerList(homePage.getData().search_players_by_position(Position)));
        show_all_player_button.setDisable(false);
    }

    public void Search_Player_By_Salary(ActionEvent actionEvent) throws Exception{
        InputBox inputBox = new InputBox(searchType.BySalaryRange);
        double[] range = inputBox.getRange();

        if( range == null )
            return;

        playerTableView.setItems(createObservablePlayerList(homePage.getData().search_player_by_salary_range(range[0],range[1] )));
        show_all_player_button.setDisable(false);
    }

    public void Exit(ActionEvent actionEvent)  {
        try{
            exit = true;
            if( homePage.isLoggedIn.getValue() )
                LogOutButtonClicked(null);
            networkUtil.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }

        homePage.getWindow().close();
    }

    public void Total_Yearly_Salary(ActionEvent actionEvent) {
        double total_yearly_salary = homePage.getData().total_yearly_salary_of_a_club(homePage.getClientName());
        Total_Yearly_Salary_TextField.setText(String.format("%.1f",total_yearly_salary));

    }

    public void Players_with_Maximum_Height(ActionEvent actionEvent) {
        clearTable();
        playerTableView.setItems(createObservablePlayerList(homePage.getData().maximum_height_of_a_club(homePage.getClientName())));
        show_all_player_button.setDisable(false);
    }

    public void Players_with_Maximum_Age(ActionEvent actionEvent) {
        clearTable();
        playerTableView.setItems(createObservablePlayerList(homePage.getData().maximum_age_of_a_club(homePage.getClientName())));
        show_all_player_button.setDisable(false);
    }

    public void Players_with_Maximum_Salary(ActionEvent actionEvent) {
        clearTable();
        playerTableView.setItems(createObservablePlayerList(homePage.getData().maximum_salary_of_a_club(homePage.getClientName())));
        show_all_player_button.setDisable(false);
    }

    public void CountryWise_Player_Count(ActionEvent actionEvent) {
        Stage newWindow = new Stage();

        TableView<Database.Country> tableView = new TableView<>();

        TableColumn<Country,String> CountryNameColumn = new TableColumn<>("Name of The Country");
        CountryNameColumn.setMinWidth(150);
        CountryNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<Country,Integer> PlayerCountColumn = new TableColumn<>("Player Count");
        PlayerCountColumn.setMinWidth(50);
        PlayerCountColumn.setCellValueFactory(new PropertyValueFactory<>("Number_of_players"));

        PlayerCountColumn.setMaxWidth(100);

        ObservableList<Country> observableCountryList = FXCollections.observableArrayList();
        observableCountryList.addAll(homePage.getData().getCountries());

        tableView.setItems(observableCountryList);
        tableView.getColumns().addAll(CountryNameColumn,PlayerCountColumn);

        Button button = new Button("Close");
        button.setOnAction(e -> {
            newWindow.close();
                });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(tableView, button);

        newWindow.setScene(new Scene(layout, 350, 400));
        newWindow.setResizable(false);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.showAndWait();

    }

    public void showAllPlayer(ActionEvent actionEvent) {
        clearTable();
        playerTableView.setItems(createObservablePlayerList(homePage.getData().getPlayers()));
        show_all_player_button.setDisable(true);
    }

    public HomePage getHomePage() {
        return homePage;
    }

    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public void init() {

        networkUtil = homePage.getNetworkUtil();

        show_all_player_button.setDisable(true);

        playerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerTableView.setPlaceholder(new Label("No Players To Show"));


        Name.setCellValueFactory(new PropertyValueFactory<>(Name.getId()));
        Country.setCellValueFactory(new PropertyValueFactory<>(Country.getId()));
        Age.setCellValueFactory(new PropertyValueFactory<>(Age.getId()));
        Height.setCellValueFactory(new PropertyValueFactory<>(Height.getId()));
        Position.setCellValueFactory(new PropertyValueFactory<>(Position.getId()));
        Number.setCellValueFactory(new PropertyValueFactory<>(Number.getId()));
        Weekly_Salary.setCellValueFactory(new PropertyValueFactory<>(Weekly_Salary.getId()));
        SellStatus.setCellValueFactory(new PropertyValueFactory<>(SellStatus.getId()));

        playerTableView.setItems(createObservablePlayerList(homePage.getData().getPlayers()));

        SellButton.disableProperty().bind(Bindings.isEmpty(playerTableView.getSelectionModel().getSelectedItems()));

        homePage.getWindow().getScene().addEventFilter( MouseEvent.MOUSE_CLICKED, event -> {

            if ( !(event.getSceneX() >= 0 && event.getSceneX() <= 672 && event.getSceneY() >= 88 && event.getSceneY() <= 596) ) {
                playerTableView.getSelectionModel().clearSelection();

            }

        });


        homePage.getWindow().setOnCloseRequest(e->Exit(null));

        Search_Player_By_Name_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Search_Player_By_Country_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Search_Player_By_Position_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Search_Player_By_Salary_Range_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        CountryWise_Player_Count_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());

        Maximum_Salary_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Maximum_Age_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Maximum_Height_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());
        Total_Yearly_Salary_MenuItem.disableProperty().bind(homePage.isLoggedIn.not());

        BuyButton.disableProperty().bind(homePage.isLoggedIn.not());
        LogOutButton.disableProperty().bind(homePage.isLoggedIn.not());
        LoginButton.disableProperty().bind(homePage.isLoggedIn);
        SignupButton.disableProperty().bind(homePage.isLoggedIn);

    }


    public static ObservableList<Player> createObservablePlayerList(List<Player> playerList){
        ObservableList<Player> observableList= FXCollections.observableArrayList();

        if( playerList == null )
            return observableList;

        for( Player p : playerList ){
            observableList.add(p);
        }

        return observableList;
    }

    public void clear_total_yearly_salary_textField(ActionEvent actionEvent) {
        Total_Yearly_Salary_TextField.clear();
    }

    public void ShowAlertBox(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("INVALID INPUT");
        alert.setHeaderText(msg);
        //alert.setContentText();
        alert.showAndWait();
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws Exception {

        String clientName = Username_TextField.getText().strip();
        String password = Password_TextField.getText().strip();

        if( clientName.length() == 0 ){
            ShowAlertBox("Please Enter Username");
            return;
        }

        if( password.length() == 0 ){
            ShowAlertBox("Please Enter Password");
            return;
        }


        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setName(clientName);
        loginMessage.setPassword(password);

        homePage.setClientName(clientName);
        networkUtil.write(loginMessage);
    }

    public void signupButtonClicked(ActionEvent actionEvent) throws Exception{

        String clientName = Username_TextField.getText().strip();
        String password = Password_TextField.getText().strip();

        if( clientName.length() == 0 ){
            ShowAlertBox("Please Enter Username");
            return;
        }

        if( password.length() == 0 ){
            ShowAlertBox("Please Enter Password");
            return;
        }

        RegisterMessage registerMessage = new RegisterMessage();
        registerMessage.setName(clientName);
        registerMessage.setPassword(password);

        networkUtil.write(registerMessage);
        //Username_TextField.clear();
        //Password_TextField.clear();
    }

    public void BuyButtonClicked(ActionEvent actionEvent) throws Exception {
        showBuyPlayers();
    }

    public void SellButtonClicked(ActionEvent actionEvent) throws Exception {

        List<Player> playerList = playerTableView.getSelectionModel().getSelectedItems().stream().collect(Collectors.toList());

        boolean flag = true;
        for( Player p : playerList ){
            if( p.getSellStatus().equalsIgnoreCase("Up For Sale") ){
                flag = false;
                break;
            }
        }

        if( !flag ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Player Selection");
            alert.setHeaderText("Selected Player is already put up for Sale");
            alert.showAndWait();
            playerTableView.getSelectionModel().clearSelection();
            return;
        }

        Stage window = new Stage();

        window.setTitle("Set Price");
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SellPriceBox.fxml"));
        Parent root = loader.load();

        SellPriceBoxController controller = loader.getController();
        controller.setWindow(window);
        controller.init();

        window.setScene(new Scene(root,300,200));

        window.showAndWait();

        if( !controller.CancelButtonClicked ){
            for( Player p : playerList ){
                p.setSellStatus("Up For Sale");
            }
            sendSellPlayerListMessage(playerList,controller.Price);
            playerTableView.refresh();
        }


        playerTableView.getSelectionModel().clearSelection();


    }



    private void showBuyPlayers() throws Exception {
        new BuyPlayerListPage(homePage);
    }

    private void showSellPlayers(){
        Iterator<Player> itr2 = homePage.SellRequestPlayerMap.keySet().iterator();
        while (itr2.hasNext()) {
            Player p = itr2.next();

            System.out.println(p.getName() + "  -  " + homePage.SellRequestPlayerMap.get(p));
        }
    }

    private void sendSellPlayerListMessage(List<Player> playerList, double price) throws Exception{
        PlayerListMessage sellplayerListMessage = new PlayerListMessage();

        HashMap<Player,Double> NewSellRequestPlayerMap = new HashMap<>();

        for( Player p : playerList ){
            NewSellRequestPlayerMap.put(p,price);
            homePage.SellRequestPlayerMap.put(p,price);
        }


        sellplayerListMessage.setPlayerList(playerList);
        sellplayerListMessage.setPlayerPriceMap(NewSellRequestPlayerMap);
        sellplayerListMessage.setMessageType(PlayerListMessage.PlayerListMessageType.SellType);


        networkUtil.write(sellplayerListMessage);



    }

    public void LogOutButtonClicked(ActionEvent actionEvent) throws IOException {
        LogOutMessage msg = new LogOutMessage();
        msg.setName(homePage.getClientName());
        networkUtil.write(msg);
        homePage.setLoggedIn(false);
        clearTable();
        ClubNameLabel.setText("");
        ClubImage.setImage(null);
        playerImage.setImage(null);
        PlayerNameLabel.setText("");
        PlayerNameLabel.setStyle("-fx-background-color: White; -fx-text-fill: Black");
    }

    public void TableViewItemMouseClicked(MouseEvent mouseEvent) {
        List<Player> playerList = playerTableView.getSelectionModel().getSelectedItems();
        if( playerList.size() == 1 ){
            try{
                InputStream stream = new FileInputStream(playerList.get(0).getName().toLowerCase()+ ".jpg");
                Image image = new Image(stream);
                playerImage.setImage(image);
                playerImage.setFitWidth(228);
                playerImage.setFitHeight(210);
                playerImage.setPreserveRatio(false);
            }catch (FileNotFoundException e){
                playerImage.setImage(null);
            }
            PlayerNameLabel.setText(playerList.get(0).getName());
            PlayerNameLabel.setStyle("-fx-background-color: Black; -fx-text-fill: white");
        }else{
            playerImage.setImage(null);
            PlayerNameLabel.setText("");
            PlayerNameLabel.setStyle("-fx-background-color: White; -fx-text-fill: Black");
        }
    }
}
