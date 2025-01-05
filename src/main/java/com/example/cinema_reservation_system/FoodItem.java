package com.example.cinema_reservation_system;

import javafx.beans.property.*;

import java.beans.PropertyChangeListener;

public class FoodItem implements Printable {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final StringProperty type;
    private final IntegerProperty qty;
    private final StringProperty imageurl;

    public FoodItem(int id, String name, double price, String type, int qty, String imageurl) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.type = new SimpleStringProperty(type);
        this.qty = new SimpleIntegerProperty(qty);
        this.imageurl = new SimpleStringProperty(imageurl);
    }

    // Getters and setters
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getQty() {
        return qty.get();
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty.set(qty);
    }

    public String getImageurl() {
        return imageurl.get();
    }

    public StringProperty imageurlProperty() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl.set(imageurl);
    }
    public void printDetails() {
        System.out.println("Food Item: " + name + ", Price: $" + price);
    }
}
