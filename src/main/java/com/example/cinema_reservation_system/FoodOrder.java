package com.example.cinema_reservation_system;

import javafx.collections.ObservableList;

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