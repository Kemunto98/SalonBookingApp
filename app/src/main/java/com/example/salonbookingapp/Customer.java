package com.example.salonbookingapp;

public class Customer
{
 String Customer_id, customerDP, Customer_name, Customer_phone, Customer_email, Customer_gender,
 Customer_location, Customer_login_id;

    public Customer() {
    }

    public Customer(String customer_id, String customerDP, String customer_name, String customer_phone, String customer_email,
                    String customer_gender, String customer_location, String customer_login_id) {
        Customer_id = customer_id;
        this.customerDP = customerDP;
        Customer_name = customer_name;
        Customer_phone = customer_phone;
        Customer_email = customer_email;
        Customer_gender = customer_gender;
        Customer_location = customer_location;
        Customer_login_id = customer_login_id;
    }

    public String getCustomerDP() {
        return customerDP;
    }

    public void setCustomerDP(String customerDP) {
        this.customerDP = customerDP;
    }

    public String getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(String customer_id) {
        Customer_id = customer_id;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return Customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        Customer_phone = customer_phone;
    }

    public String getCustomer_email() {
        return Customer_email;
    }

    public void setCustomer_email(String customer_email) {
        Customer_email = customer_email;
    }

    public String getCustomer_gender() {
        return Customer_gender;
    }

    public void setCustomer_gender(String customer_gender) {
        Customer_gender = customer_gender;
    }

    public String getCustomer_location() {
        return Customer_location;
    }

    public void setCustomer_location(String customer_location) {
        Customer_location = customer_location;
    }

    public String getCustomer_login_id() {
        return Customer_login_id;
    }

    public void setCustomer_login_id(String customer_login_id) {
        Customer_login_id = customer_login_id;
    }
}
