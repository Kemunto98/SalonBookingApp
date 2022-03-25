package com.example.salonbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_dashboard extends AppCompatActivity {

    private Salon salon;
    private CircleImageView salon_image;
    private TextView txt_salonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        salon = (Salon) getIntent().getExtras().get("salonItem");

        salon_image = findViewById(R.id.salon_image);
        txt_salonName = findViewById(R.id.txt_salonName);

        Glide.with(this).load(salon.getImage()).into(salon_image);
        txt_salonName.setText(salon.getSalon_name());


        findViewById(R.id.cardViewServices).setOnClickListener(view -> {
            Intent intent = new Intent(Admin_dashboard.this, ViewServicesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("salonItem", salon);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        findViewById(R.id.card_viewBookings).setOnClickListener(view -> {
            Intent intent = new Intent(Admin_dashboard.this, ViewBookingsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("salonItem", salon);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}