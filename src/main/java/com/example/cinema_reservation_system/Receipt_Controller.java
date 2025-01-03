package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class Receipt_Controller {

    @FXML
    private ListView<HBox> list_view;
    @FXML
    private Label total_price_label;

    private ObservableList<HBox> listViewItems = FXCollections.observableArrayList();
    private double totalPrice = 0;

    @FXML
    public void initialize() {
        generateSampleReceipt();
        list_view.setItems(listViewItems);
        updateTotalPriceLabel();
    }

    private void generateSampleReceipt() {
        addMovieBooking("Avengers: Endgame", 3, 10.0);
        addMovieBooking("Inception", 2, 8.0);
        addFoodItem("Pizza", 7.0, 2);
        addFoodItem("Cola", 2.0, 3);
        addFoodItem("Popcorn", 3.0, 1);
    }

    private void addMovieBooking(String movieName, int seats, double seatPrice) {
        double total = seats * seatPrice;
        totalPrice += total;

        Label itemLabel = new Label(movieName + " - " + seats + " seats @ $" + seatPrice + " each = $" + total);
        itemLabel.setFont(new Font(14));

        HBox hBox = new HBox(itemLabel);
        HBox.setHgrow(itemLabel, Priority.ALWAYS);

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

    private void updateTotalPriceLabel() {
        total_price_label.setText("Total: $" + String.format("%.2f", totalPrice));
    }
}
