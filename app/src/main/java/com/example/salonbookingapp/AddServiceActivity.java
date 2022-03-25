package com.example.salonbookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddServiceActivity extends AppCompatActivity {

    private String salonId;
    private RecyclerView stylists_rc;
    private List<Stylist> stylistList;
    private Service service;
    private SweetAlertDialog sweetAlertDialog;
    private TextInputEditText edt_service_name, edt_service_price, edt_service_duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        this.salonId = getIntent().getExtras().getString("salon_id");

        stylists_rc = findViewById(R.id.stylists_rc);
        edt_service_name = findViewById(R.id.edt_service_name);
        edt_service_price = findViewById(R.id.edt_service_price);
        edt_service_duration = findViewById(R.id.edt_service_duration);

        stylistList = new ArrayList<>();
        service = new Service();
        service.setService_salon_id(salonId);

        FirebaseFirestore.getInstance().collection("Stylists")
                .whereEqualTo("stylist_salon_id", salonId)
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

        findViewById(R.id.btn_addService).setOnClickListener(view -> addService());




    }

    private void addService() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_service_name.getText()).toString()))
            edt_service_name.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_service_price.getText()).toString()))
            edt_service_price.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_service_duration.getText()).toString()))
            edt_service_duration.setError(this.getString(R.string.input_error));
        else if (service.getStylist_id() == null || service.getStylist_id().isEmpty()){
            Toast.makeText(AddServiceActivity.this, "Please choose a stylist", Toast.LENGTH_SHORT).show();
        }
        else {
            sweetAlertDialog = new SweetAlertDialog(AddServiceActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Adding Service");
            sweetAlertDialog.show();

            service.setService_name(edt_service_name.getText().toString());
            service.setDuration(edt_service_duration.getText().toString());
            service.setPrice(Integer.parseInt(edt_service_price.getText().toString()));
            service.setService_salon_id(salonId);

            FirebaseFirestore.getInstance().collection("Services")
                    .add(service)
                    .addOnSuccessListener(documentReference -> {
                        FirebaseFirestore.getInstance().collection("Salons")
                                .document(salonId)
                                .update("salon_services_num", FieldValue.increment(1))
                                .addOnSuccessListener(unused -> {
                                    Log.d("Service: ", "photo saved, registration complete");
                                    Toast.makeText(AddServiceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.dismissWithAnimation();
                                    AddServiceActivity.super.onBackPressed();
                                });
                    }).addOnFailureListener(e -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Log.d("StylistAddError", e.getLocalizedMessage());
                        Toast.makeText(AddServiceActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private class StylistAdapter extends RecyclerView.Adapter<AddServiceActivity.StylistAdapter.StylistViewHolder>{

        @NonNull
        @Override
        public AddServiceActivity.StylistAdapter.StylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddServiceActivity.StylistAdapter.StylistViewHolder(LayoutInflater.from(AddServiceActivity.this).inflate(R.layout.stylist_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddServiceActivity.StylistAdapter.StylistViewHolder holder, int position) {

            Glide.with(AddServiceActivity.this).load(stylistList.get(position).getStylist_profile_pic()).into(holder.stylist_image);
            holder.stylist_name.setText(stylistList.get(position).getStylist_name());
            holder.stylist_phone.setText(stylistList.get(position).getStylist_phone());

            holder.itemView.setOnClickListener(view -> {
                service.setStylist_id(stylistList.get(position).getStylist_id());
                service.setService_name(stylistList.get(position).getStylist_name());
                service.setStylist_pic(stylistList.get(position).getStylist_profile_pic());

                Toast.makeText(AddServiceActivity.this, stylistList.get(position).getStylist_name() + " Selected", Toast.LENGTH_SHORT).show();
            });


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

}