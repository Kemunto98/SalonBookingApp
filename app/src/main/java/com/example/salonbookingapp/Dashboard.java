package com.example.salonbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    private CircleImageView user_image, user_image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        user_image = findViewById(R.id.user_image);
        user_image2 = findViewById(R.id.user_image2);

        FirebaseFirestore.getInstance().collection("Customer")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    Glide.with(Dashboard.this).load(customer.getCustomerDP()).into(user_image);
                    Glide.with(Dashboard.this).load(customer.getCustomerDP()).into(user_image2);

                });

        findViewById(R.id.card_logOut).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Dashboard.this, MainActivity.class));
            finish();
        });

        findViewById(R.id.toBook).setOnClickListener(view -> startActivity(new Intent(this, SalonsActivity.class)));
        findViewById(R.id.toSalons).setOnClickListener(view -> startActivity(new Intent(this, MySalonsActivity.class)));
        findViewById(R.id.toMyBookings).setOnClickListener(view -> startActivity(new Intent(this, MyBookingsActivity.class)));

    }
}