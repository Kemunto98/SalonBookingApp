package com.example.salonbookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonDetailsActivity extends AppCompatActivity {

    Salon salon;
    ImageView salon_image;
    TextView salon_name;
    TextView salon_contact;
    TextView salon_location;
    List<Service> serviceList;
    List<Stylist> stylistList;

    RecyclerView services_rc, stylists_rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_details);
        salon = (Salon) getIntent().getExtras().getSerializable("salonItem");

        salon_image = findViewById( R.id.salon_image);
        salon_name = findViewById(R.id.salon_name);
        salon_contact = findViewById(R.id.salon_contact);
        salon_location = findViewById(R.id.salon_location);
        services_rc = findViewById(R.id.services_rc);
        stylists_rc = findViewById(R.id.stylists_rc);

        Glide.with(SalonDetailsActivity.this).load(salon.getImage()).into(salon_image);
        salon_name.setText(salon.getSalon_name());
        salon_contact.setText(salon.getSalon_contact());
        salon_location.setText(salon.getLocation());

        stylistList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Stylists")
                .whereEqualTo("stylist_salon_id", salon.getSalon_id())
                .addSnapshotListener((value, error) -> {

                    if (value != null){
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Stylist stylist = documentChange.getDocument().toObject(Stylist.class);
                                stylist.setStylist_id(documentChange.getDocument().getId());
                                stylistList.add(stylist);
                            }
                        }
                        stylists_rc.setAdapter(new StylistAdapter());
                        stylists_rc.setLayoutManager(new GridLayoutManager(this, 2));
                    }

                });

        serviceList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("service_salon_id", salon.getSalon_id())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Service service = documentChange.getDocument().toObject(Service.class);
                                service.setService_id(documentChange.getDocument().getId());
                                serviceList.add(service);
                            }
                        }
                        services_rc.setAdapter(new ServicesAdapter());
                        services_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    }

                });

    }

    private class StylistAdapter extends RecyclerView.Adapter<StylistAdapter.StylistViewHolder>{

        @NonNull
        @Override
        public StylistAdapter.StylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new StylistViewHolder(LayoutInflater.from(SalonDetailsActivity.this).inflate(R.layout.stylist_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull StylistAdapter.StylistViewHolder holder, int position) {

            Glide.with(SalonDetailsActivity.this).load(stylistList.get(position).getStylist_profile_pic()).into(holder.stylist_image);
            holder.stylist_name.setText(stylistList.get(position).getStylist_name());
            holder.stylist_phone.setText(stylistList.get(position).getStylist_phone());


        }

        @Override
        public int getItemCount() {
            return stylistList.size();
        }

        public class StylistViewHolder extends RecyclerView.ViewHolder{

            CircleImageView stylist_image;
            TextView stylist_name, stylist_phone;

            public StylistViewHolder(@NonNull View itemView) {
                super(itemView);

                stylist_image = itemView.findViewById(R.id.stylist_image);
                stylist_name = itemView.findViewById(R.id.stylist_name);
                stylist_phone = itemView.findViewById(R.id.stylist_phone);
            }
        }
    }

    public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>{

        @NonNull
        @Override
        public ServicesAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ServiceViewHolder(LayoutInflater.from(SalonDetailsActivity.this).inflate(R.layout.service_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ServicesAdapter.ServiceViewHolder holder, int position) {

            Service service = serviceList.get(position);

            Glide.with(SalonDetailsActivity.this).load(service.getStylist_pic()).into(holder.stylist_dp);
            holder.stylist_name.setText(service.getStylist_name());
            holder.service_name.setText(service.getService_name());
            holder.service_price.setText(String.valueOf(service.getPrice()));
            holder.service_duration.setText(String.valueOf(service.getDuration()));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(SalonDetailsActivity.this, RequestServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("service", service);
                intent.putExtras(bundle);
                startActivity(intent);
            });


        }

        @Override
        public int getItemCount() {
            return serviceList.size();
        }

        public class ServiceViewHolder extends RecyclerView.ViewHolder{

            CircleImageView stylist_dp;
            TextView stylist_name, service_name, service_price, service_duration;

            public ServiceViewHolder(@NonNull View itemView) {
                super(itemView);

                stylist_dp = itemView.findViewById(R.id.stylist_dp);
                stylist_name = itemView.findViewById(R.id.stylist_name);
                service_name = itemView.findViewById(R.id.service_name);
                service_price = itemView.findViewById(R.id.service_price);
                service_duration = itemView.findViewById(R.id.service_duration);
            }
        }
    }

}