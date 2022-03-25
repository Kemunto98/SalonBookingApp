package com.example.salonbookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MySalonsActivity extends AppCompatActivity {

    RecyclerView mySalons_rc;
    List<Salon> salonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_salons);

        findViewById(R.id.add_salon).setOnClickListener(view -> startActivity(new Intent(MySalonsActivity.this, AddSalonActivity.class)));


        salonList = new ArrayList<>();
        mySalons_rc = findViewById(R.id.mySalons_rc);

        FirebaseFirestore.getInstance().collection("Salons")
                .whereEqualTo("owner_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        salonList.clear();
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Salon salon = documentChange.getDocument().toObject(Salon.class);
                                salon.setSalon_id(documentChange.getDocument().getId());
                                salonList.add(salon);
                            }
                        }
                        mySalons_rc.setAdapter(new MySalonsAdapter());
                        mySalons_rc.setLayoutManager(new LinearLayoutManager(MySalonsActivity.this, LinearLayoutManager.VERTICAL, false));
                    }
                });




    }

    private class MySalonsAdapter extends RecyclerView.Adapter<MySalonsAdapter.SalonViewHolder>{


        @NonNull
        @Override
        public MySalonsAdapter.SalonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SalonViewHolder(LayoutInflater.from(MySalonsActivity.this).inflate(R.layout.salon_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MySalonsAdapter.SalonViewHolder holder, int position) {

            Salon salon = salonList.get(position);

            Glide.with(MySalonsActivity.this).load(salon.getImage()).into(holder.salon_dp);
            holder.salon_name.setText(salon.getSalon_name());
            holder.salon_location.setText(salon.getLocation());
            holder.salon_services_number.setText(String.valueOf(salon.getSalon_services_num()));
            holder.salon_rating.setText(String.valueOf(salon.getRating()));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(MySalonsActivity.this, Admin_dashboard.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("salonItem", salon);
                intent.putExtras(bundle);
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return salonList.size();
        }

        private class SalonViewHolder extends RecyclerView.ViewHolder{

            ImageView salon_dp;
            TextView salon_name, salon_location, salon_services_number, salon_rating;

            public SalonViewHolder(@NonNull View itemView) {
                super(itemView);

                salon_dp = itemView.findViewById(R.id.salon_dp);
                salon_name = itemView.findViewById(R.id.salon_name);
                salon_location = itemView.findViewById(R.id.salon_location);
                salon_services_number = itemView.findViewById(R.id.salon_services_number);
                salon_rating = itemView.findViewById(R.id.salon_rating);

            }
        }
    }

}