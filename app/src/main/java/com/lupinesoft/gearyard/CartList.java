package com.lupinesoft.gearyard;

public class CartList {
    String UID, PID, Brand, Model, Quantity, Total, PhotoUrl;

    public CartList(String UID, String PID, String brand, String model, String quantity, String total, String PhotoUrl) {
        this.UID = UID;
        this.PID = PID;
        Brand = brand;
        Model = model;
        Quantity = quantity;
        Total = total;
        this.PhotoUrl = PhotoUrl;
    }

    public String getUID() {
        return UID;
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

    public String getQuantity() {
        return Quantity;
    }

    public String getTotal() {
        return Total;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }
}
