package com.lupinesoft.gearyard;

public class AddressList {
    String UID, FullName, Mobile, State, City, Road;

    public AddressList(String UID, String fullName, String mobile, String state, String city, String road) {
        this.UID = UID;
        FullName = fullName;
        Mobile = mobile;
        State = state;
        City = city;
        Road = road;
    }

    public String getUID() {
        return UID;
    }

    public String getFullName() {
        return FullName;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getState() {
        return State;
    }

    public String getCity() {
        return City;
    }

    public String getRoad() {
        return Road;
    }
}
