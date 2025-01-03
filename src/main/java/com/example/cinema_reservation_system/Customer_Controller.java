package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

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
    private static List<FoodItem> orderList = new ArrayList<>();

    private FoodItem[] foodItems = {
            new FoodItem("Burger", 5.0),
            new FoodItem("Pizza", 7.0),
            new FoodItem("Fries", 3.0),
            new FoodItem("Popcorn", 3.0),
            new FoodItem("Cola", 2.0),
            new FoodItem("Sprite", 2.5),
            new FoodItem("Juice", 6.5),
            new FoodItem("Energy-Drink", 10.0)
    };

    private String[] imagePaths = {
            "https://thecookierookie.com/wp-content/uploads/2023/04/featured-stovetop-burgers-recipe.jpg",// Burger
            "https://www.foodandwine.com/thmb/Wd4lBRZz3X_8qBr69UOu2m7I2iw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/classic-cheese-pizza-FT-RECIPE0422-31a2c938fc2546c9a07b7011658cfd05.jpg",// Pizza
            "https://upload.wikimedia.org/wikipedia/commons/8/83/French_Fries.JPG",// Fries
            "https://bittmanproject.com/wp-content/uploads/GettyImages-1060532718.jpg",// Popcorn
            "https://mcprod.hyperone.com.eg/media/catalog/product/cache/8d4e6327d79fd11192282459179cc69e/5/4/5449000294975-320mll.jpg",// Cola
            "https://www.ubuy.com.eg/productimg/?image=aHR0cHM6Ly9tLm1lZGlhLWFtYXpvbi5jb20vaW1hZ2VzL0kvNjF3aCt2OXMwekwuX1NMMTUwMF8uanBn.jpg",// Sprite
            "https://theallnaturalvegan.com/wp-content/uploads/2023/08/mango-juice-featured-image.jpg",// Juice
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Monster_Energy_drink_%28cropped%29.jpg/1200px-Monster_Energy_drink_%28cropped%29.jpg"// Energy-Drink
    };

    @FXML
    public void initialize() {
        loadImages();
        foodpic_1.setOnMouseClicked(event -> addItem(foodItems[0]));
        foodpic_2.setOnMouseClicked(event -> addItem(foodItems[1]));
        foodpic_3.setOnMouseClicked(event -> addItem(foodItems[2]));
        foodpic_4.setOnMouseClicked(event -> addItem(foodItems[3]));
        bevpic_1.setOnMouseClicked(event -> addItem(foodItems[4]));
        bevpic_2.setOnMouseClicked(event -> addItem(foodItems[5]));
        bevpic_3.setOnMouseClicked(event -> addItem(foodItems[6]));
        bevpic_4.setOnMouseClicked(event -> addItem(foodItems[7]));

        items_list.setItems(listViewItems);
    }

    private void loadImages() {
        foodpic_1.setImage(new Image(imagePaths[0]));
        foodpic_2.setImage(new Image(imagePaths[1]));
        foodpic_3.setImage(new Image(imagePaths[2]));
        foodpic_4.setImage(new Image(imagePaths[3]));
        bevpic_1.setImage(new Image(imagePaths[4]));
        bevpic_2.setImage(new Image(imagePaths[5]));
        bevpic_3.setImage(new Image(imagePaths[6]));
        bevpic_4.setImage(new Image(imagePaths[7]));
    }

    private void addItem(FoodItem item) {
        boolean exists = false;
        for (FoodItem orderItem : orderList) {
            if (orderItem.getName().equals(item.getName())) {
                orderItem.qty++;
                exists = true;
                break;
            }
        }

        if (!exists) {
            orderList.add(new FoodItem(item.getName(), item.getPrice()));
        }

        refreshListView();
        calculateTotalPrice();
    }

    private void refreshListView() {
        listViewItems.clear();

        for (FoodItem item : orderList) {
            Label itemLabel = new Label(item.getName() + " x " + item.qty + " - $" + String.format("%.2f", item.getTotalPrice()));

            Button btnPlus = new Button("+");
            btnPlus.setOnAction(e -> {
                item.qty++;
                refreshListView();
                calculateTotalPrice();
            });

            Button btnMinus = new Button("-");
            btnMinus.setOnAction(e -> {
                if (item.qty > 1) {
                    item.qty--;
                } else {
                    orderList.remove(item);
                }
                refreshListView();
                calculateTotalPrice();
            });

            Button btnRemove = new Button("Remove");
            btnRemove.setOnAction(e -> {
                orderList.remove(item);
                refreshListView();
                calculateTotalPrice();
            });

            HBox hBox = new HBox(10, itemLabel, btnMinus, btnPlus, btnRemove);
            HBox.setHgrow(itemLabel, Priority.ALWAYS);
            listViewItems.add(hBox);
        }
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (FoodItem item : orderList) {
            total += item.getTotalPrice();
        }
        totalprice_label.setText("$" + String.format("%.2f", total));
    }

    public static class FoodItem {
        private final String name;
        private final double price;
        private int qty = 1;

        public FoodItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public double getTotalPrice() {
            return price * qty;
        }
    }
}
