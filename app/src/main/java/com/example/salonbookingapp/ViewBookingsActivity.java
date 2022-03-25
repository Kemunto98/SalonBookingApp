package com.example.salonbookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewBookingsActivity extends AppCompatActivity {

    private RecyclerView myBookingsRC;
    private List<Appointment> appointmentList;
    private Salon salon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        salon = (Salon) getIntent().getExtras().get("salonItem");

        myBookingsRC = findViewById(R.id.myBookingsRC);
        appointmentList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Bookings")
                .whereEqualTo("booking_salon_id", salon.getSalon_id())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        appointmentList.clear();
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Appointment appointment = documentChange.getDocument().toObject(Appointment.class);
                                appointment.setBooking_id(documentChange.getDocument().getId());
                                appointmentList.add(appointment);
                            }
                        }
                        myBookingsRC.setAdapter(new MyBookingsAdapter());
                        myBookingsRC.setLayoutManager(new LinearLayoutManager(ViewBookingsActivity.this, LinearLayoutManager.VERTICAL, false));
                    }
                });

    }

    public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.BookingViewHolder>{

        @NonNull
        @Override
        public MyBookingsAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyBookingsAdapter.BookingViewHolder(LayoutInflater.from(ViewBookingsActivity.this).inflate(R.layout.booking_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyBookingsAdapter.BookingViewHolder holder, int position) {

            Appointment appointment = appointmentList.get(position);

            Glide.with(ViewBookingsActivity.this).load(appointment.getBooking_customer_pic()).into(holder.customer_dp);
            holder.customer_name.setText(appointment.getBooking_customer_name());
            holder.txt_title.setText("customer");
            holder.salon_name.setText(appointment.getSalon_name());
            holder.service_name.setText(appointment.getService_name());
            holder.txt_price.setText(String.valueOf(appointment.getService_price()));
            holder.txt_description.setText(appointment.getBooking_service_desc());

        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        public class BookingViewHolder extends RecyclerView.ViewHolder{

            CircleImageView customer_dp;
            TextView customer_name, salon_name, service_name, txt_price, txt_description, txt_title;

            public BookingViewHolder(@NonNull View itemView) {
                super(itemView);

                customer_dp = itemView.findViewById(R.id.stylist_dp);
                customer_name = itemView.findViewById(R.id.stylist_name);
                salon_name = itemView.findViewById(R.id.salon_name);
                service_name = itemView.findViewById(R.id.service_name);
                txt_price = itemView.findViewById(R.id.txt_price);
                txt_description = itemView.findViewById(R.id.txt_description);
                txt_title = itemView.findViewById(R.id.txt_title);



            }
        }

    }
}