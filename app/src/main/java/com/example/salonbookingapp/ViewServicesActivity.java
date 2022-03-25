package com.example.salonbookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewServicesActivity extends AppCompatActivity {

    List<Service> serviceList;
    RecyclerView services_admin_rc;
    private Salon salon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);

        salon = (Salon) getIntent().getExtras().get("salonItem");

        findViewById(R.id.add_service).setOnClickListener(view ->{
            Intent intent = new Intent(ViewServicesActivity.this, AddServiceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("salon_id", salon.getSalon_id());
            intent.putExtras(bundle);
            startActivity(intent);
                });

        serviceList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Services")
                .whereEqualTo("service_salon_id", salon.getSalon_id())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        serviceList.clear();
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                Service service = documentChange.getDocument().toObject(Service.class);
                                service.setService_id(documentChange.getDocument().getId());
                                serviceList.add(service);
                            }
                        }
                        services_admin_rc.setAdapter(new ServicesAdapter());
                        services_admin_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    }

                });

        services_admin_rc = findViewById(R.id.services_admin_rc);

        services_admin_rc.setAdapter(new ServicesAdapter());
        services_admin_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



    }

    public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>{

        @NonNull
        @Override
        public ServicesAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ServicesAdapter.ServiceViewHolder(LayoutInflater.from(ViewServicesActivity.this).inflate(R.layout.service_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ServicesAdapter.ServiceViewHolder holder, int position) {

            Service service = serviceList.get(position);

            Glide.with(ViewServicesActivity.this).load(service.getStylist_pic()).into(holder.stylist_dp);
            holder.stylist_name.setText(service.getStylist_name());
            holder.service_name.setText(service.getService_name());
            holder.service_price.setText(String.valueOf(service.getPrice()));
            holder.service_duration.setText(String.valueOf(service.getDuration()));

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