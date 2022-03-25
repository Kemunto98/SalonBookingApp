package com.example.salonbookingapp;

public class Payment
{
    String Payment_id, Payment_Appointment_id, Payment_mode;
    int Payment_amount;

    public Payment() {
    }

    public Payment(String payment_id, String payment_Appointment_id, String payment_mode, int payment_amount) {
        Payment_id = payment_id;
        Payment_Appointment_id = payment_Appointment_id;
        Payment_mode = payment_mode;
        Payment_amount = payment_amount;
    }

    public String getPayment_id() {
        return Payment_id;
    }

    public void setPayment_id(String payment_id) {
        Payment_id = payment_id;
    }

    public String getPayment_Appointment_id() {
        return Payment_Appointment_id;
    }

    public void setPayment_Appointment_id(String payment_Appointment_id) {
        Payment_Appointment_id = payment_Appointment_id;
    }

    public String getPayment_mode() {
        return Payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        Payment_mode = payment_mode;
    }

    public int getPayment_amount() {
        return Payment_amount;
    }

    public void setPayment_amount(int payment_amount) {
        Payment_amount = payment_amount;
    }
}
