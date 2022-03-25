package com.example.salonbookingapp;

public class Stylist {

    String stylist_id, stylist_name, stylist_phone,
            stylist_email, stylist_profile_pic, gender, stylist_salon_id, login_id;

    public Stylist() {
    }

    public Stylist(String stylist_id, String stylist_name, String stylist_phone, String stylist_email,
                   String stylist_profile_pic, String gender, String stylist_salon_id, String login_id) {
        this.stylist_id = stylist_id;
        this.stylist_name = stylist_name;
        this.stylist_phone = stylist_phone;
        this.stylist_email = stylist_email;
        this.stylist_profile_pic = stylist_profile_pic;
        this.gender = gender;
        this.stylist_salon_id = stylist_salon_id;
        this.login_id = login_id;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
    }

    public String getStylist_name() {
        return stylist_name;
    }

    public void setStylist_name(String stylist_name) {
        this.stylist_name = stylist_name;
    }

    public String getStylist_phone() {
        return stylist_phone;
    }

    public void setStylist_phone(String stylist_phone) {
        this.stylist_phone = stylist_phone;
    }

    public String getStylist_email() {
        return stylist_email;
    }

    public void setStylist_email(String stylist_email) {
        this.stylist_email = stylist_email;
    }

    public String getStylist_profile_pic() {
        return stylist_profile_pic;
    }

    public void setStylist_profile_pic(String stylist_profile_pic) {
        this.stylist_profile_pic = stylist_profile_pic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStylist_salon_id() {
        return stylist_salon_id;
    }

    public void setStylist_salon_id(String stylist_salon_id) {
        this.stylist_salon_id = stylist_salon_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }
}
