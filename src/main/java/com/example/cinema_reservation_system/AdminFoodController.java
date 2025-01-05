package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.util.Optional;
import java.io.IOException;

public class AdminFoodController {

    @FXML
    private TableView<FoodItem> table_view;

    @FXML
    private TableColumn<FoodItem, String> name_column;

    @FXML
    private TableColumn<FoodItem, Double> price_column;

    @FXML
    private TableColumn<FoodItem, String> type_column;

    @FXML
    private TableColumn<FoodItem, Integer> qty_column;

    @FXML
    private TableColumn<FoodItem, String> url_column;

    @FXML
    private Button back_button, btn_add, btn_remove, btn_save;

    @FXML
    private TextField name_field, price_field, type_field, qty_field, url_field;

    private ObservableList<FoodItem> foodList;

    @FXML
    private void initialize() {
        // Set up table columns
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("type"));
        qty_column.setCellValueFactory(new PropertyValueFactory<>("qty"));
        url_column.setCellValueFactory(new PropertyValueFactory<>("imageurl"));

        // Populate the table view with data
        populateTableView();

        // Initialize buttons
        btn_save.setDisable(true); // Initially disable save button

        // Clear input fields
        clearFields();

        // Select first row if available
        if (!foodList.isEmpty()) {
            table_view.getSelectionModel().selectFirst();
            showFoodDetails(foodList.get(0));
        }
    }

    private void populateTableView() {
        foodList = FXCollections.observableArrayList();
        Database db = Database.getInstance();
        String query = "SELECT * FROM food";

        try (ResultSet resultSet = db.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name").trim();
                double price = resultSet.getDouble("price");
                String type = resultSet.getString("type").trim();
                int qty = resultSet.getInt("qty");
                String imageurl = resultSet.getString("imageurl").trim();

                foodList.add(new FoodItem(id, name, price, type, qty, imageurl));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load food items from the database.");
        }

        table_view.setItems(foodList);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showFoodDetails(newValue);
            }
        });
    }

    private void showFoodDetails(FoodItem foodItem) {
        name_field.setText(foodItem.getName());
        price_field.setText(String.valueOf(foodItem.getPrice()));
        type_field.setText(foodItem.getType());
        qty_field.setText(String.valueOf(foodItem.getQty()));
        url_field.setText(foodItem.getImageurl());
        btn_save.setDisable(false);
    }

    @FXML
    private void goBack() throws IOException {
        SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void addFood() {
        String name = name_field.getText();
        String priceStr = price_field.getText();
        String type = type_field.getText();
        String qtyStr = qty_field.getText();
        String imageurl = url_field.getText();

        if (name.isEmpty() || priceStr.isEmpty() || type.isEmpty() || qtyStr.isEmpty() || imageurl.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(qtyStr);

            String query = "INSERT INTO food (name, price, type, qty, imageurl) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setString(3, type);
                stmt.setInt(4, qty);
                stmt.setString(5, imageurl);
                stmt.executeUpdate();
            }

            foodList.add(new FoodItem(0, name, price, type, qty, imageurl));  // Adjusted constructor usage
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Price and quantity must be valid numbers.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add food item.");
        }
    }

    @FXML
    private void removeFood() {
        FoodItem selectedFood = table_view.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this food item?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM food WHERE name = ?";  // Changed to use name instead of ID
                    try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                        stmt.setString(1, selectedFood.getName());  // Using name instead of ID
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            foodList.remove(selectedFood);
                            clearFields();
                        } else {
                            showAlert("Error", "No food item found with that name.");
                        }
                    }
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete food item.");
                }
            }
        }
    }

    @FXML
    private void saveFood() {
        FoodItem selectedFood = table_view.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            String name = name_field.getText();
            String priceStr = price_field.getText();
            String type = type_field.getText();
            String qtyStr = qty_field.getText();
            String imageurl = url_field.getText();
            if (name.isEmpty() || priceStr.isEmpty() || type.isEmpty() || qtyStr.isEmpty() || imageurl.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int qty = Integer.parseInt(qtyStr);

                String query = "UPDATE food SET price = ?, type = ?, qty = ?, imageurl = ? WHERE name = ?";  // Using name instead of ID
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                    stmt.setDouble(1, price);
                    stmt.setString(2, type);
                    stmt.setInt(3, qty);
                    stmt.setString(4, imageurl);
                    stmt.setString(5, name);  // Using name instead of ID
                    stmt.executeUpdate();
                }

                // Update the selected food item in the list
                selectedFood.setName(name);
                selectedFood.setPrice(price);
                selectedFood.setType(type);
                selectedFood.setQty(qty);
                selectedFood.setImageurl(imageurl);
                table_view.refresh();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Error", "Price and quantity must be valid numbers.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to update food item.");
            }
        }
    }

    private void clearFields() {
        name_field.clear();
        price_field.clear();
        type_field.clear();
        qty_field.clear();
        url_field.clear();
        btn_save.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
