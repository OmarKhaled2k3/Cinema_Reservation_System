package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class Customer_Controller {

    @FXML
    private ImageView bevpic_1, bevpic_2, bevpic_3, bevpic_4;
    @FXML
    private ImageView foodpic_1, foodpic_2, foodpic_3, foodpic_4;

    @FXML
    private Button btn_back, btn_checkout;
    @FXML
    private ListView<HBox> items_list;
    @FXML
    private Label totalprice_label;

    private ObservableList<HBox> listViewItems = FXCollections.observableArrayList();
    private Map<String, FoodItem> orderMap = new HashMap<>();

    private FoodItem[] foodItems = {
            new FoodItem("Burger", 5.0),
            new FoodItem("Pizza", 7.0),
            new FoodItem("Fries", 3.0),
            new FoodItem("Cola", 2.0)
    };

    private String[] imagePaths = {
            "https://cdn.pixabay.com/photo/2016/03/05/19/02/hamburger-1238246_1280.jpg",
            "https://cdn.pixabay.com/photo/2017/01/22/19/20/pizza-2000618_1280.jpg",
            "https://cdn.pixabay.com/photo/2014/04/22/02/56/fries-329523_1280.jpg",
            "https://cdn.pixabay.com/photo/2017/09/04/18/39/cola-2715069_1280.jpg"
    };



    @FXML
    public void initialize() {
        loadImages();

        bevpic_1.setOnMouseClicked(event -> addItem(foodItems[3]));
        foodpic_1.setOnMouseClicked(event -> addItem(foodItems[0]));
        foodpic_2.setOnMouseClicked(event -> addItem(foodItems[1]));
        foodpic_3.setOnMouseClicked(event -> addItem(foodItems[2]));

        items_list.setItems(listViewItems);
    }

    private void loadImages() {
        foodpic_1.setImage(new Image(imagePaths[0]));
        foodpic_2.setImage(new Image(imagePaths[1]));
        foodpic_3.setImage(new Image(imagePaths[2]));
        bevpic_1.setImage(new Image(imagePaths[3]));
    }

    private void addItem(FoodItem item) {
        String itemName = item.getName();

        if (orderMap.containsKey(itemName)) {
            FoodItem existingItem = orderMap.get(itemName);
            existingItem.qty += 1;
        } else {
            orderMap.put(itemName, new FoodItem(itemName, item.getPrice()));
        }

        refreshListView();
        calculateTotalPrice();
    }

    private void refreshListView() {
        listViewItems.clear();

        for (FoodItem item : orderMap.values()) {
            Label itemLabel = new Label(item.getName() + " x " + item.qty + " - $" + item.getPrice());
            HBox hBox = new HBox(10, itemLabel);
            listViewItems.add(hBox);
        }
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (FoodItem item : orderMap.values()) {
            total += item.getPrice();
        }
        totalprice_label.setText("$" + String.format("%.2f", total));
    }
}
