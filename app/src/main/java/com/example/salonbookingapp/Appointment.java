package com.example.salonbookingapp;

public class Appointment {

    String booking_id, salon_name, service_name, booking_service_id, booking_salon_id, booking_customer_id;
    String booking_customer_pic, booking_customer_name, booking_stylist_id, booking_stylist_name;
    String booking_stylist_pic, booking_service_desc;
    int service_price;

    public Appointment() {
    }

    public Appointment(String booking_id, String salon_name, int service_price, String service_name, String booking_service_id,
                       String booking_salon_id, String booking_customer_id, String booking_customer_pic, String booking_customer_name, String booking_stylist_id,
                       String booking_stylist_name, String booking_stylist_pic, String booking_service_desc) {
        this.booking_id = booking_id;
        this.salon_name = salon_name;
        this.service_price = service_price;
        this.service_name = service_name;
        this.booking_service_id = booking_service_id;
        this.booking_salon_id = booking_salon_id;
        this.booking_customer_id = booking_customer_id;
        this.booking_customer_pic = booking_customer_pic;
        this.booking_customer_name = booking_customer_name;
        this.booking_stylist_id = booking_stylist_id;
        this.booking_stylist_name = booking_stylist_name;
        this.booking_stylist_pic = booking_stylist_pic;
        this.booking_service_desc = booking_service_desc;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public int getService_price() {
        return service_price;
    }

    public void setService_price(int service_price) {
        this.service_price = service_price;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_service_id() {
        return booking_service_id;
    }

    public void setBooking_service_id(String booking_service_id) {
        this.booking_service_id = booking_service_id;
    }

    public String getBooking_salon_id() {
        return booking_salon_id;
    }

    public void setBooking_salon_id(String booking_salon_id) {
        this.booking_salon_id = booking_salon_id;
    }

    public String getBooking_customer_id() {
        return booking_customer_id;
    }

    public void setBooking_customer_id(String booking_customer_id) {
        this.booking_customer_id = booking_customer_id;
    }

    public String getBooking_customer_pic() {
        return booking_customer_pic;
    }

    public void setBooking_customer_pic(String booking_customer_pic) {
        this.booking_customer_pic = booking_customer_pic;
    }

    public String getBooking_customer_name() {
        return booking_customer_name;
    }

    public void setBooking_customer_name(String booking_customer_name) {
        this.booking_customer_name = booking_customer_name;
    }

    public String getBooking_stylist_id() {
        return booking_stylist_id;
    }

    public void setBooking_stylist_id(String booking_stylist_id) {
        this.booking_stylist_id = booking_stylist_id;
    }

    public String getBooking_stylist_name() {
        return booking_stylist_name;
    }

    public void setBooking_stylist_name(String booking_stylist_name) {
        this.booking_stylist_name = booking_stylist_name;
    }

    public String getBooking_stylist_pic() {
        return booking_stylist_pic;
    }

    public void setBooking_stylist_pic(String booking_stylist_pic) {
        this.booking_stylist_pic = booking_stylist_pic;
    }

    public String getBooking_service_desc() {
        return booking_service_desc;
    }

    public void setBooking_service_desc(String booking_service_desc) {
        this.booking_service_desc = booking_service_desc;
    }
}

