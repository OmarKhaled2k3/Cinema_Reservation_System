package com.example.cinema_reservation_system;

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
    @FXML
    private TableColumn<Seat, Double> pricecolumn ;
    @FXML
    private TableColumn<Seat, Integer> qtycolumn;
    @FXML
    private TableColumn<ArrayList<Seat>, String> seatscolumn;

    @FXML
    private TableView<Seat> tableview;

    @FXML
    private TableColumn<Seat, Double> totalpricecolumn;

    @FXML
    private Label totalpricelabel;

    @FXML
    private TableColumn<?, ?> typecolumn;
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
            seatPressed.setFill(Color.RED);
            if(!selectedSeatsID.contains(seatID)){
                selectedSeatsID.add(seatID);
                //Seat seat = new Seat(seatID,isreser)
            }
        }
    }
    public void AddSeatToCart(Seat seat){
        tableview.getItems().add(seat);
    }

}