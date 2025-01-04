package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Admin_Controller {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_remove;

    @FXML
    private Button btn_save;

    @FXML
    private ListView<Item> list_view;

    @FXML
    private TextField name_field;

    @FXML
    private TextField price_field;

    @FXML
    private TextField qty_field;

    @FXML
    private TextField type_field;

    @FXML
    private TextField url_field;

    private ObservableList<Item> itemList;

    @FXML
    public void initialize() {
        itemList = FXCollections.observableArrayList();
        list_view.setItems(itemList);

        btn_add.setOnAction(event -> addItem());
        btn_remove.setOnAction(event -> removeItem());
        btn_save.setOnAction(event -> saveItem());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showItemDetails(newValue));
    }

    private void addItem() {
        String name = name_field.getText();
        String type = type_field.getText();
        String url = url_field.getText();

        try {
            double price = Double.parseDouble(price_field.getText());
            int qty = Integer.parseInt(qty_field.getText());

            if (name.isEmpty() || type.isEmpty() || url.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
                return;
            }

            Item newItem = new Item(name, price, qty, type, url);
            itemList.add(newItem);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numeric values for price and quantity.");
        }
    }

    private void removeItem() {
        Item selectedItem = list_view.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            itemList.remove(selectedItem);
            clearFields();
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to remove.");
        }
    }

    private void saveItem() {
        Item selectedItem = list_view.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                selectedItem.setName(name_field.getText());
                selectedItem.setType(type_field.getText());
                selectedItem.setUrl(url_field.getText());
                selectedItem.setPrice(Double.parseDouble(price_field.getText()));
                selectedItem.setQty(Integer.parseInt(qty_field.getText()));
                list_view.refresh();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numeric values for price and quantity.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to save.");
        }
    }

    private void showItemDetails(Item item) {
        if (item != null) {
            name_field.setText(item.getName());
            price_field.setText(String.valueOf(item.getPrice()));
            qty_field.setText(String.valueOf(item.getQty()));
            type_field.setText(item.getType());
            url_field.setText(item.getUrl());
        }
    }

    private void clearFields() {
        name_field.clear();
        price_field.clear();
        qty_field.clear();
        type_field.clear();
        url_field.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Item {
        private String name;
        private double price;
        private int qty;
        private String type;
        private String url;

        public Item(String name, double price, int qty, String type, String url) {
            this.name = name;
            this.price = price;
            this.qty = qty;
            this.type = type;
            this.url = url;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        @Override
        public String toString() {
            return name + " - " + price + " - " + qty + " - " + type;
        }
    }
}
