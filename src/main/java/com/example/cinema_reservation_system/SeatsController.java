package com.example.cinema_reservation_system;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.TextFieldTableCell;
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
    @FXML
    private TableColumn<ObservableList<String>, String> pricecolumn ;
    @FXML
    private TableColumn<ObservableList<String>, String> qtycolumn;
    @FXML
    private TableColumn<ObservableList<String>, String> seatscolumn;

    @FXML
    private TableView<ObservableList<String>> tableview ;

    @FXML
    private TableColumn<ObservableList<String>, String> totalpricecolumn;

    @FXML
    private Label totalpricelabel;

    @FXML
    private TableColumn<ObservableList<String>, String> typecolumn;
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
        tableview = new TableView<>();

        seatscolumn = createColumn("Seat Number", 0);
        typecolumn = createColumn("Type", 1);
         qtycolumn = createColumn("Quantity", 2);
        pricecolumn = createColumn("Price", 3);
        totalpricecolumn = createColumn("Total Price", 4);
        tableview.getColumns().addAll(seatscolumn, typecolumn, pricecolumn,qtycolumn, totalpricecolumn);

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
        //tableview.getItems().add(seat);
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String vipSeats="",standardSeats="";
        Double standardPrice=0d,vipPrice=0d;
        int standardQty=0,vipQty=0;
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
        // Add rows of raw data
        if(standardQty>0)data.add(FXCollections.observableArrayList(standardSeats, "Standard", "100", String.valueOf(standardQty), String.valueOf(standardPrice)));
        if(vipQty>0)data.add(FXCollections.observableArrayList(vipSeats, "VIP", "120", String.valueOf(vipQty), String.valueOf(vipPrice)));
        tableview.setItems(data);

    }
    private TableColumn<ObservableList<String>, String> createColumn(String title, int index) {
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> {
            ObservableList<String> row = cellData.getValue();
            return new SimpleStringProperty(row.get(index));
        });
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }

}