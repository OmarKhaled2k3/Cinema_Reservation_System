package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class SeatsController implements Initializable {

    public static class SeatDataObject {
        private String vipSeats="",standardSeats="";
        private Double standardPrice=0d,vipPrice=0d;
        private Integer standardQty=0,vipQty=0;
        private Double totalPrice=0d;
        private String type;

        public String getVipSeats() {
            return vipSeats;
        }

        public void setVipSeats(String vipSeats) {
            this.vipSeats = vipSeats;
        }

        public String getStandardSeats() {
            return standardSeats;
        }

        public void setStandardSeats(String standardSeats) {
            this.standardSeats = standardSeats;
        }

        public Double getStandardPrice() {
            return standardPrice;
        }

        public void setStandardPrice(Double standardPrice) {
            this.standardPrice = standardPrice;
        }

        public Double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(Double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public Integer getStandardQty() {
            return standardQty;
        }

        public void setStandardQty(Integer standardQty) {
            this.standardQty = standardQty;
        }

        public Integer getVipQty() {
            return vipQty;
        }

        public void setVipQty(Integer vipQty) {
            this.vipQty = vipQty;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        SeatDataObject(String vipSeats, String standardSeats, Double standardPrice, Double vipPrice, Integer standardQty, Integer vipQty, Double totalPrice, String type) {
            this.vipSeats = vipSeats;
            this.standardSeats = standardSeats;
            this.standardPrice = standardPrice;
            this.vipPrice = vipPrice;
            this.standardQty = standardQty;
            this.vipQty = vipQty;
            this.totalPrice = totalPrice;
            this.type=type;
        }

    }
    @FXML
    private TableColumn<SeatDataObject, Double> pricecolumn  ;

    @FXML
    private TableColumn<SeatDataObject, Integer> qtycolumn ;
    @FXML
    private TableColumn<SeatDataObject, String> seatscolumn ;

    @FXML
    private TableView<SeatDataObject> tableview  ;

    @FXML
    private TableColumn<SeatDataObject,Double> totalpricecolumn ;

    @FXML
    private Label totalpricelabel;

    @FXML
    private TableColumn<SeatDataObject, String> typecolumn;
    public HBox AllSeats;
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
    ArrayList<Integer> selectedSeatsID =new ArrayList<>();
    ArrayList<Seat>selectedSeats=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pricecolumn.setCellValueFactory(new PropertyValueFactory<>("standardPrice"));
        qtycolumn.setCellValueFactory(new PropertyValueFactory<>("standardQty"));
        seatscolumn.setCellValueFactory(new PropertyValueFactory<>("standardSeats"));
        totalpricecolumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        typecolumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        CheckSeatsAvailability();
    }
    @FXML
    public void backToSelectedMovieScene(ActionEvent event) throws IOException {

        SceneController.launchScene("SelectedMovie.fxml");
    }

    @FXML
    public void CheckSeatsAvailability() {
        Reservation reservation = Reservation.getInstance();
        String showtimeSeats = Seat.SeatsConversiontoString(reservation.getShowtime().getSeatList());
        System.out.println(showtimeSeats);
        for (Node Section : AllSeats.getChildren()){
            if(Section instanceof VBox){
                VBox CurrentSection = (VBox) Section;
                for(Node Row:CurrentSection.getChildren()){
                    if(Row instanceof HBox){
                        HBox CurrentRow = (HBox) Row;
                        for(Node seat:CurrentRow.getChildren()){
                            if(seat instanceof Rectangle){
                                Rectangle CurrentSeat = (Rectangle) seat;
                                int seatID=Integer.parseInt(CurrentSeat.getId().substring(4));
                                System.out.println(seatID);
                                if(showtimeSeats.charAt(seatID-1)=='1')
                                    CurrentSeat.setFill(Color.GRAY);
                            }
                        }
                    }
                }
            }
        }
    }
    public void SelectSeat(MouseEvent mouseEvent) throws IOException {
        Reservation reservation = Reservation.getInstance();
        ArrayList<Integer> showtimeSeats = reservation.getShowtime().getSeatIDList();
        Rectangle seatPressed = (Rectangle) mouseEvent.getSource();
        int seatID=Integer.parseInt(seatPressed.getId().substring(4));
        //System.out.println(seatPressed.getId());
        if(showtimeSeats.contains(seatID)){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("This seat is cannot be reserved");
            a.show();
        }
        else {

            if(!selectedSeatsID.contains(seatID)){
                seatPressed.setFill(Color.RED);
                selectedSeatsID.add(seatID);
                Seat seat = new Seat(seatID,true);
                selectedSeats.add(seat);
                UpdateCart();
            }
            else{
                selectedSeatsID.remove(Integer.valueOf(seatID));
                selectedSeats.remove(new Seat(seatID,true));
                seatPressed.setFill(Color.BLUE);
                UpdateCart();
            }
        }
    }
    public void UpdateCart(){
        ObservableList<ObservableList<SeatDataObject>> data = FXCollections.observableArrayList();
        String vipSeats="",standardSeats="";
        Double standardPrice=0d,vipPrice=0d;
        int standardQty=0,vipQty=0;
        String type="Standard";
        for(Seat sid :selectedSeats){
            if(sid.getType().equals("Standard")){
                standardSeats+=sid.getSeatNumber() +" ,";
                standardPrice+=sid.getSeatPrice(sid.getSeatNumber());
                standardQty++;
            }
            else{
                vipSeats+=sid.getSeatNumber()+" ,";
                vipPrice+=sid.getSeatPrice(sid.getSeatNumber());
                vipQty++;
            }
        }
        SeatDataObject seatDataObject = new SeatDataObject(vipSeats,standardSeats,100d,vipPrice,standardQty,vipQty,standardPrice,type);
        //data.add((ObservableList<SeatDataObject>) seatDataObject);
        // Add rows of raw data
        tableview.getItems().clear();

        //if(standardQty>0){

        //}
        //if(vipQty>0)data.add(FXCollections.observableArrayList(vipSeats, "VIP", "120", String.valueOf(vipQty), String.valueOf(vipPrice)));

        if(!tableview.getItems().isEmpty())
        tableview.getItems().set(0,seatDataObject);
        else
            tableview.getItems().add(seatDataObject);
        //tableview.setItems(data);
        tableview.refresh();
    }
    //To be removed
    @FXML
    private void skip() throws IOException {
        SceneController.launchScene("Food_CustomerTab.fxml");
}}