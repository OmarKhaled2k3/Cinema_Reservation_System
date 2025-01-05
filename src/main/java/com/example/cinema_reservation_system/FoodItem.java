package com.example.cinema_reservation_system;

public class FoodItem implements Printable{
    private String name;
    private double price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    private int qty;
    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
        qty=1;
    }
    public FoodItem(String name, double price,int qty) {
        this.name = name;
        this.price = price;
        this.qty=qty;
    }
    public double getPrice(int qty){
        this.qty=qty;
        return price*qty;
    }
    public double getPrice(FoodItem foodItem,int qty){
        this.qty=qty;
        return foodItem.price*qty;
    }
    public String getName() { return name; }
    public double getPrice() { return price*qty; }

    public void printDetails() {
        System.out.println("Food Item: " + name + ", Price: $" + price);
    }
}
