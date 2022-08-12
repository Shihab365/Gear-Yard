package com.lupinesoft.gearyard;

public class FavList {
    String UID, PID, Brand, Model, PhotoURL;

    public FavList(String UID, String PID, String brand, String model, String photoURL) {
        this.UID = UID;
        this.PID = PID;
        Brand = brand;
        Model = model;
        PhotoURL = photoURL;
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

    public String getPhotoURL() {
        return PhotoURL;
    }
}
