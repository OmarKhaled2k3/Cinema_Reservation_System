package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        private String seats="";
        private Double price=0d;
        private Integer qty=0;
        private Double totalPrice=0d;
        private String type;

        public Integer getQty() {
            return qty;
        }

        public String getSeats() {
            return seats;
        }

        public Double getPrice() {
            return price;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public String getType() {
            return type;
        }

        public SeatDataObject(String seats, Double price, Integer qty, Double totalPrice, String type) {
            this.seats = seats;
            this.price = price;
            this.qty = qty;
            this.totalPrice = totalPrice;
            this.type = type;
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
    @FXML
    private Button btn_save;
    ArrayList<Integer> selectedSeatsID =new ArrayList<>();
    ArrayList<Seat>selectedSeats=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pricecolumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtycolumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        seatscolumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
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
        Color VIPcolor = new Color(0.248, 0.7763, 0.4417, 1.0);
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
                if(Seat.getType(seatID).equals("Standard") )
                    seatPressed.setFill(Color.DODGERBLUE);
                else
                    seatPressed.setFill(VIPcolor);
                selectedSeatsID.remove(Integer.valueOf(seatID));
                selectedSeats.remove(Seat.getSeatbyID(selectedSeats,seatID));
                UpdateCart();
            }
        }
    }
    public void UpdateCart(){
        String vipSeats="",standardSeats="";
        Double standardPrice=0d,vipPrice=0d;
        int standardQty=0,vipQty=0;
        String type="";
        double standard_seat_price = 100;
        double vip_seat_price=120 ;
        for(Seat sid :selectedSeats){
            if(sid.getType().equals("Standard")){
                standard_seat_price=sid.getSeatPrice(sid.getSeatNumber());
                standardSeats+=sid.getSeatNumber() +" ,";
                standardPrice+=standard_seat_price;
                standardQty++;
            }
            else{
                vip_seat_price=sid.getSeatPrice(sid.getSeatNumber());
                vipSeats+=sid.getSeatNumber()+" ,";
                vipPrice+=vip_seat_price;
                vipQty++;
            }
        }
        tableview.getItems().clear();

        if(standardQty>0){
            type="Standard";
            SeatDataObject seatDataObject = new SeatDataObject(standardSeats,standard_seat_price,standardQty,standardPrice,type);
            tableview.getItems().add(seatDataObject);
        }
        if(vipQty>0){
            type="VIP";
            SeatDataObject seatDataObject = new SeatDataObject(vipSeats,vip_seat_price,vipQty,vipPrice,type);
            tableview.getItems().add(seatDataObject);
        }
        totalpricelabel.setText(String.valueOf(standardPrice+vipPrice)+"$");
    }
    //To be removed
    @FXML
    private void next() throws IOException {
        Reservation reservation = Reservation.getInstance();
        reservation.addSeatSelected(selectedSeats);
        SceneController.launchScene("Food_CustomerTab.fxml");
}}