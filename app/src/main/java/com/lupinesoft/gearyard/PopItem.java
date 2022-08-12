package com.lupinesoft.gearyard;

public class PopItem {
    String PID, Brand, Model, Price, Photo;

    public PopItem(String PID, String brand, String model, String price, String photo) {
        this.PID = PID;
        Brand = brand;
        Model = model;
        Price = price;
        Photo = photo;
    }

    public String getPID() {
        return PID;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getPrice() {
        return Price;
    }

    public String getPhoto() {
        return Photo;
    }
}
