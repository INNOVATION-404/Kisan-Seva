package com.kisanseva.kisanseva.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Farmer {

    public String name;
    public String email;
    public String phone;
    public String pin;

    public GeoLocation location;

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public Farmer() {

    }

    public Farmer(String name, String email, String phone, String pin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pin = pin;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }



    @Override
    public String toString() {
        return "Farmer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

}
