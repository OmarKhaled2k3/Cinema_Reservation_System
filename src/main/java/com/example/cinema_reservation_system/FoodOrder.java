package com.example.cinema_reservation_system;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FoodOrder implements Printable {
    private ArrayList<FoodItem> foodItems;
    private double totalCost;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FoodOrder() {
        this.foodItems = new ArrayList<>();
    }
    public FoodOrder(ObservableList<FoodItem> foodItems) {
        this.foodItems = new ArrayList<>();
        this.foodItems.addAll(foodItems);
    }
    public FoodOrder(ArrayList<FoodItem> foodItems) {
        this.foodItems = new ArrayList<>();
        this.foodItems.addAll(foodItems);
    }

    public void addItems(FoodItem item) {
        foodItems.add(item);
    }
    public void addItems(ArrayList<FoodItem> items) {
        foodItems.addAll(items);
    }

    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }
    public String FoodItemsToString(){
        StringBuilder foodItemsString = new StringBuilder("000000000");
        int index=0;
        for(FoodItem food:foodItems){
            foodItemsString.setCharAt(index++, String.valueOf(food.getId()).charAt(0) );
        }
        return foodItemsString.toString();
    }
    public static ArrayList<FoodItem> StringtoFoodItems(String foodItemsID,String foodItemQty){
        int foodlastindex = foodItemsID.indexOf('0');
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        if(foodlastindex >0){
            String ActualfoodItemsID=foodItemsID.substring(0,foodlastindex);
            for(int i=0;i<ActualfoodItemsID.length();i++){
                int foodid=Integer.parseInt(String.valueOf(ActualfoodItemsID.charAt(i)));
                try {
                    Database db = Database.getInstance();
                    ResultSet resultSet = db.executeQuery("select * from food where id="+foodid);
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            String foodName=resultSet.getString("name").trim();
                            Double foodPrice=resultSet.getDouble("price");
                            foodItems.add(new FoodItem(foodid,foodName,foodPrice,Integer.parseInt(String.valueOf(foodItemQty.charAt(i)))));

                        }
                        resultSet.close();
                    }
                } catch (Exception exception) {
                    System.out.println("Error: " + exception);
                }
            }
            return foodItems;
        }
        else return null;
    }
    public String FoodQtyToString(){
        StringBuilder foodQtyString = new StringBuilder("000000000");
        int index=0;
        for(FoodItem food:foodItems){
            foodQtyString.setCharAt(index++, String.valueOf(food.getQty()).charAt(0));
        }
        return foodQtyString.toString();
    }
    public double getTotalCost() {
        totalCost=0;
        for(FoodItem foodItem : foodItems){
            totalCost+=foodItem.getPrice();
        }
        return totalCost;
    }

    @Override
    public void printDetails() {
        System.out.println("Food Order Details:");
        for (FoodItem item : foodItems) {
            item.printDetails();
        }
        System.out.println("Total Cost: $" + getTotalCost());
    }
}