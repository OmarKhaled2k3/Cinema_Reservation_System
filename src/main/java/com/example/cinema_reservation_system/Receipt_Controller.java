package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Receipt_Controller {

    @FXML
    private ListView<HBox> list_view;
    @FXML
    private Label total_price_label;

    private ObservableList<HBox> listViewItems = FXCollections.observableArrayList();
    private double totalPrice = 0;

    @FXML
    public void initialize() {
        Reservation reservation = Reservation.getInstance();
        generateReceipt();
        list_view.setItems(listViewItems);
        updateTotalPriceLabel();
        if(reservation.getCustomer()!=null){
            if(reservation.getCustomer().isModify()) reservation.getCustomer().modifyReservation();
            else reservation.getCustomer().createReservation();
        }
    }

    private void generateReceipt() {
        Reservation reservation = Reservation.getInstance();
        ArrayList<Seat> standardSeats = reservation.getStandardSeatsSelected();
        ArrayList<Seat> vipSeats = reservation.getVIPSeatsSelected();
        addMovieBooking(reservation.getShowtime(),standardSeats);
        addMovieBooking(reservation.getShowtime(),vipSeats);
        addFoodOrder(reservation.getFoodOrder());
    }


    private void addMovieBooking(ShowTime showTime, ArrayList<Seat> seatsReserved) {
        if(!seatsReserved.isEmpty()){
        String movieName=showTime.getMovie().getTitle();
        String time = showTime.getStartTime().toString();
        int num_seats=seatsReserved.size();
        String type = seatsReserved.getFirst().getType();
        double seatPrice =seatsReserved.getFirst().getSeatPrice();
        addMovieBooking(movieName,num_seats,type,seatPrice,time,seatsReserved);
        }
    }
    private void addMovieBooking(String movieName, int seats, String seatType, double seatPrice, String showTime, ArrayList<Seat> seatsReserved) {
        double total = seats * seatPrice;
        totalPrice += total;

        // Convert seat numbers to a comma-separated string
        String seatNumbersString ="";
        for(Seat seat : seatsReserved){
            seatNumbersString+=String.valueOf(seat.getSeatNumber())+", ";
        }
        // Label with movie name, seat type, seat price, total, showtime, and seat numbers
        Label itemLabel = new Label(movieName + " (" + showTime + ") - " + seats + " " + seatType + " seats ["
                + seatNumbersString + "] @ $" + seatPrice + " each = $" + total);
        itemLabel.setFont(new Font(14));

        // Layout to display the item
        HBox hBox = new HBox(itemLabel);
        HBox.setHgrow(itemLabel, Priority.ALWAYS);

        // Add to the list of items
        listViewItems.add(hBox);
    }
    private void addFoodItem(String itemName, double price, int quantity) {
        double total = price * quantity;
        totalPrice += total;

        Label itemLabel = new Label(itemName + " x " + quantity + " - $" + total);
        itemLabel.setFont(new Font(14));

        HBox hBox = new HBox(itemLabel);
        HBox.setHgrow(itemLabel, Priority.ALWAYS);

        listViewItems.add(hBox);
    }
    private void addFoodOrder(FoodOrder foodOrder) {
        if(foodOrder != null) {
            for (FoodItem foodItem : foodOrder.getFoodItems()) {
                addFoodItem(foodItem.getName(), foodItem.getPrice(), foodItem.getQty());
            }
        }
    }

    private void updateTotalPriceLabel() {
        total_price_label.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    @FXML
    void confirm(ActionEvent event) throws IOException {
        SceneController.launchScene("Customer_View.fxml");
    }
}
