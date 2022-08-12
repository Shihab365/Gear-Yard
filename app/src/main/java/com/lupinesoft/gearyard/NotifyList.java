package com.lupinesoft.gearyard;

public class NotifyList {

    String Type, Title, Description, Date;

    public NotifyList(String type, String title, String description, String date) {
        Type = type;
        Title = title;
        Description = description;
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }
}
