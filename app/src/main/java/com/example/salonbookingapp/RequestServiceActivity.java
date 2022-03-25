package com.example.salonbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestServiceActivity extends AppCompatActivity {

    private Service service;
    private ImageView salon_image;
    private CircleImageView stylist_dp;
    private TextView salon_name, service_stylist, salon_location, service_duration;
    private TextInputEditText service_des;
    private Appointment appointment;
    private SweetAlertDialog sweetAlertDialog;
    private Customer customer;
    private Salon salon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        service = (Service) getIntent().getExtras().get("service");

        appointment = new Appointment();

        salon_image = findViewById(R.id.salon_image);
        stylist_dp = findViewById(R.id.stylist_dp);
        salon_name = findViewById(R.id.salon_name);
        service_stylist = findViewById(R.id.service_stylist);
        service_duration = findViewById(R.id.service_duration);
        salon_location = findViewById(R.id.salon_location);
        service_des = findViewById(R.id.service_des);

        FirebaseFirestore.getInstance().collection("Customer")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         customer= documentSnapshot.toObject(Customer.class);
                         customer.setCustomer_id(documentSnapshot.getId());

                    }
                });

        FirebaseFirestore.getInstance().collection("Salons")
                .document(service.getService_salon_id())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        salon = documentSnapshot.toObject(Salon.class);
                        salon.setSalon_id(documentSnapshot.getId());

                        Glide.with(RequestServiceActivity.this).load(salon.getImage()).into(salon_image);
                        salon_name.setText(salon.getSalon_name());
                        salon_location.setText(salon.getLocation());


                    }
                });

        Glide.with(RequestServiceActivity.this).load(service.getStylist_pic()).into(stylist_dp);
        service_stylist.setText(service.getStylist_name());
        service_duration.setText(service.getDuration());


        findViewById(R.id.materialButton).setOnClickListener(view -> sendRequest());

    }

    private void sendRequest() {
        if (TextUtils.isEmpty(Objects.requireNonNull(service_des.getText()).toString())){
            service_des.setText(RequestServiceActivity.this.getString(R.string.input_error));
        }else {
            sweetAlertDialog = new SweetAlertDialog(RequestServiceActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText("Sending Booking");
            sweetAlertDialog.show();

            appointment.setBooking_customer_id(customer.getCustomer_id());
            appointment.setBooking_customer_name(customer.getCustomer_name());
            appointment.setBooking_customer_pic(customer.getCustomerDP());
            appointment.setBooking_service_id(service.getService_id());
            appointment.setBooking_service_desc(service_des.getText().toString());
            appointment.setBooking_salon_id(salon.getSalon_id());
            appointment.setBooking_stylist_id(service.getStylist_id());
            appointment.setBooking_stylist_name(service.getStylist_name());
            appointment.setBooking_stylist_pic(service.getStylist_pic());
            appointment.setSalon_name(salon.getSalon_name());
            appointment.setService_price(service.getPrice());
            appointment.setService_name(service.getService_name());


            FirebaseFirestore.getInstance().collection("Bookings")
                    .add(appointment)
                    .addOnSuccessListener(documentReference -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Toast.makeText(RequestServiceActivity.this, "Booking Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RequestServiceActivity.this, MyBookingsActivity.class));
                        finishActivity(0);

                    });
        }
    }
}