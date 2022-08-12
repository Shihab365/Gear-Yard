package com.lupinesoft.gearyard;

public class RecyList {
    String pid, brand, model, price, photo;

    public RecyList(String pid, String brand, String model, String price, String photo) {
        this.pid = pid;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.photo = photo;
    }

    public String getPid() {
        return pid;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getPhoto() {
        return photo;
    }
}
