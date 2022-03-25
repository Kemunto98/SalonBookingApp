package com.example.salonbookingapp;

import java.io.Serializable;
import java.util.List;

public class Salon implements Serializable {

    String salon_id, salon_name, location, salon_contact, image, owner_name, owner_id;
    List<String> stylist_names;
    List<Service> services;
    int salon_services_num;
    float rating;

    public Salon() {
    }


    public Salon(String salon_id, String salon_name, String location, String salon_contact, String image, String owner_name, String owner_id,
                 List<String> stylist_names, List<Service> services, int salon_services_num, float rating) {
        this.salon_id = salon_id;
        this.salon_name = salon_name;
        this.location = location;
        this.salon_contact = salon_contact;
        this.image = image;
        this.owner_name = owner_name;
        this.owner_id = owner_id;
        this.stylist_names = stylist_names;
        this.services = services;
        this.salon_services_num = salon_services_num;
        this.rating = rating;
    }

    public String getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(String salon_id) {
        this.salon_id = salon_id;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public List<String> getStylist_names() {
        return stylist_names;
    }

    public void setStylist_names(List<String> stylist_names) {
        this.stylist_names = stylist_names;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public int getSalon_services_num() {
        return salon_services_num;
    }

    public void setSalon_services_num(int salon_services_num) {
        this.salon_services_num = salon_services_num;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getSalon_contact() {
        return salon_contact;
    }

    public void setSalon_contact(String salon_contact) {
        this.salon_contact = salon_contact;
    }
}
