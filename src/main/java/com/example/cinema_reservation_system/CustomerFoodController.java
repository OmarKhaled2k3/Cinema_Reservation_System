package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFoodController implements Initializable {

    @FXML
    private HBox foodImagesHBox;
    @FXML
    private HBox beverageImagesHBox;

    @FXML
    private TableView<FoodItem> tableid;
    @FXML
    private TableColumn<FoodItem, Integer> qtColumn;
    @FXML
    private TableColumn<FoodItem, String> itremColumn;
    @FXML
    private TableColumn<FoodItem, Double> priceColumn;
    @FXML
    private Label totalprice_label;

    @FXML
    private Button btn_back, btn_checkout;

    private ObservableList<FoodItem> selectedItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        qtColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        itremColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Allow quantity column to be editable
        qtColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        qtColumn.setOnEditCommit(event -> {
            FoodItem foodItem = event.getRowValue();
            int newQty = event.getNewValue();
            foodItem.setQty(newQty);  // Update the quantity in the FoodItem object
            updateTotalPrice();  // Update the total price when quantity changes
        });

        // Load and display food items
        ArrayList<FoodItem> foodItems = retrieveFoodItems("Fast Food");
        displayItemsInHBox(foodImagesHBox, foodItems);

        // Load and display beverage items
        ArrayList<FoodItem> beverageItems = retrieveFoodItems("Beverage");
        displayItemsInHBox(beverageImagesHBox, beverageItems);
        Reservation reservation = Reservation.getInstance();
        if(reservation.getFoodOrder()!=null){
            ObservableList<FoodItem> observableFoodItems = FXCollections.observableArrayList(reservation.getFoodOrder().getFoodItems());
            selectedItems=  observableFoodItems;

        }
        // Populate the table with initial data (if any)
        tableid.setItems(selectedItems);

        // Calculate and display initial total price
        updateTotalPrice();
    }

    private void displayItemsInHBox(HBox hbox, ArrayList<FoodItem> items) {
        hbox.getChildren().clear();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(20);

        for (FoodItem item : items) {
            ImageView imageView = new ImageView(new Image(item.getImageurl()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setId(String.valueOf(item.getId()));

            imageView.setOnMouseClicked(e -> {
                if (!selectedItemExists(item)) {
                    // Set quantity to 1 when the item is added
                    item.setQty(1);
                    selectedItems.add(item);
                } else {
                    updateQuantity(item);
                }
                updateTotalPrice();
                tableid.setItems(selectedItems);
            });

            hbox.getChildren().add(imageView);
        }
    }

    private ArrayList<FoodItem> retrieveFoodItems(String type) {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        try {
            Database db = Database.getInstance();
            String query = "SELECT * FROM food WHERE type = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, type);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name").trim();
                    double price = resultSet.getDouble("price");
                    String imageurl = resultSet.getString("imageurl").trim();
                    String foodType = resultSet.getString("type");
                    int qty = 0; // Quantity starts at 0
                    FoodItem foodItem = new FoodItem(id, name, price, foodType, qty, imageurl);
                    foodItems.add(foodItem);
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    private boolean selectedItemExists(FoodItem item) {
        for (FoodItem existingItem : selectedItems) {
            if (existingItem.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    private void updateQuantity(FoodItem item) {
        for (FoodItem existingItem : selectedItems) {
            if (existingItem.getId() == item.getId()) {
                existingItem.setQty(existingItem.getQty() + 1);
                break;
            }
        }
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (FoodItem item : selectedItems) {
            totalPrice += item.getPrice() * item.getQty();
        }
        totalprice_label.setText(String.format("%.2f", totalPrice));
    }
    @FXML
    private void handleRemoveItemClick() {
        FoodItem selectedFood = tableid.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            // Remove the selected item from the list
            selectedItems.remove(selectedFood);
            updateTotalPrice();  // Update the total price after removal
            tableid.setItems(selectedItems);  // Refresh the TableView
        } else {
            showAlert("Error", "Please select an item to remove.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButtonClick() throws IOException {
        SceneController.launchScene("Seats_Admin.fxml");
    }

    @FXML
    private void handleCheckoutButtonClick() throws IOException {
        // Pass the selected items and total price to the next scene (Receipt_Tab.fxml)
        // You can use SceneController to pass data to the next scene
        Reservation reservation = Reservation.getInstance();
        if(!selectedItems.isEmpty()){
        FoodOrder foodOrder = new FoodOrder(selectedItems);
        reservation.addFoodOrder(foodOrder);
        }
        SceneController.launchScene("Receipt_Tab.fxml");
    }
}
