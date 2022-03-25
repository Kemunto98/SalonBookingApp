package com.example.salonbookingapp;

import java.io.Serializable;

public class Service implements Serializable {

    String service_id, service_name, service_salon_id, duration;
    int price;
    String Service_desc;
    String stylist_name, stylist_id, stylist_pic;

    public Service() {
    }

    public Service(String service_id, String service_name, String service_salon_id, String duration, int price, String service_desc,
                   String stylist_name, String stylist_id, String stylist_pic) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_salon_id = service_salon_id;
        this.duration = duration;
        this.price = price;
        Service_desc = service_desc;
        this.stylist_name = stylist_name;
        this.stylist_id = stylist_id;
        this.stylist_pic = stylist_pic;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStylist_name() {
        return stylist_name;
    }

    public String getService_desc() {
        return Service_desc;
    }

    public void setService_desc(String service_desc) {
        Service_desc = service_desc;
    }

    public void setStylist_name(String stylist_name) {
        this.stylist_name = stylist_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getService_salon_id() {
        return service_salon_id;
    }

    public void setService_salon_id(String service_salon_id) {
        this.service_salon_id = service_salon_id;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
    }

    public String getStylist_pic() {
        return stylist_pic;
    }

    public void setStylist_pic(String stylist_pic) {
        this.stylist_pic = stylist_pic;
    }
}
