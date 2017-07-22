package com.example.android.product_inventory.Utils;

import java.io.Serializable;


public class Product implements Serializable {
    private int id;
    private String name;
    private float price;
    private int available;
    private int sold;
    private String imageString;
    private String supplier;

    public Product() {
    }

    public Product(int id, String name, float price, int available, int sold, String imageString, String supplier) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.sold = sold;
        this.imageString = imageString;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}