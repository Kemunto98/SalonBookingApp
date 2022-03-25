package com.example.salonbookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddSalonActivity extends AppCompatActivity {

    private List<Service> serviceList;
    private List<Stylist> stylistList;
    private RecyclerView stylists_rc, services_rc;
    private SweetAlertDialog sweetAlertDialog;
    private Uri image_uri;
    private Salon salon;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ImageView img_salonImage;
    private Customer customer;
    private TextInputEditText edt_salonName, edt_salonLocation, edt_salonContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon);

        salon = new Salon();

        stylistList = new ArrayList<>();

        stylists_rc = findViewById(R.id.stylists_rc);
        services_rc = findViewById(R.id.services_rc);
        img_salonImage = findViewById(R.id.img_salonImage);
        edt_salonName = findViewById(R.id.edt_salonName);
        edt_salonLocation = findViewById(R.id.edt_salonLocation);
        edt_salonContact = findViewById(R.id.edt_salonContact);

        img_salonImage.setOnClickListener(view -> openGallery());


        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                image_uri = result.getData().getData();
                Glide.with(AddSalonActivity.this).load(image_uri).into(img_salonImage);
            }
        });

        FirebaseFirestore.getInstance().collection("Customer")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    customer = documentSnapshot.toObject(Customer.class);
                    customer.setCustomer_id(documentSnapshot.getId());
                });


        findViewById(R.id.btn_addService).setOnClickListener(view ->{
                    if (stylistList != null && !stylistList.isEmpty()){
                        nextActivity(new Intent(AddSalonActivity.this, AddServiceActivity.class));
                    }else Toast.makeText(AddSalonActivity.this, "Add a stylist first", Toast.LENGTH_SHORT).show();
                });

        findViewById(R.id.btn_addStylist).setOnClickListener(view ->
                nextActivity(new Intent(AddSalonActivity.this, AddStylistActivity.class)));

        findViewById(R.id.btn_addSalon).setOnClickListener(view -> {
            addNewSalon();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intentActivityResultLauncher.launch(intent);
    }

    private void addNewSalon() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_salonName.getText()).toString()))
            edt_salonName.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_salonLocation.getText()).toString()))
            edt_salonLocation.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_salonContact.getText()).toString()))
            edt_salonContact.setError(this.getString(R.string.input_error));
        else if (image_uri == null)
            findViewById(R.id.photo_error).setVisibility(View.VISIBLE);
        else {
            sweetAlertDialog = new SweetAlertDialog(AddSalonActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Adding salon");
            sweetAlertDialog.show();

           salon.setSalon_name(edt_salonName.getText().toString());
           salon.setLocation(edt_salonLocation.getText().toString());
           salon.setSalon_contact(edt_salonContact.getText().toString());
           salon.setOwner_name(customer.getCustomer_name());
           salon.setOwner_id(customer.getCustomer_id());

            FirebaseFirestore.getInstance().collection("Salons")
                    .document(salon.getSalon_id())
                    .set(salon)
                    .addOnSuccessListener(documentReference -> setSalonImage(salon.getSalon_id()))
                    .addOnFailureListener(e -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Log.d("StylistAddError", e.getLocalizedMessage());
                        Toast.makeText(AddSalonActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void setSalonImage(String salon_id) {
        Log.d("SalonProg: ", "photo started to upload");
        sweetAlertDialog.setContentText("Setting photo");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Salons")
                .child("Images");

        try {
            storageReference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri ->

                            FirebaseFirestore.getInstance().collection("Salons")
                                    .document(salon_id).update("image", uri.toString())
                                    .addOnSuccessListener(unused -> {
                                        Log.d("SalonProg: ", "photo saved, salon complete");
                                        Toast.makeText(AddSalonActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                                        sweetAlertDialog.dismiss();
                                        AddSalonActivity.super.onBackPressed();

                                    }).addOnFailureListener(runnable1 -> {
                                sweetAlertDialog.dismissWithAnimation();
                                Toast.makeText(AddSalonActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                Log.d("SalonProg: ", "photo failed to upload because " + runnable1.getMessage());
                            })));
        } catch (Exception e) {
            sweetAlertDialog.dismissWithAnimation();
            Toast.makeText(AddSalonActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("SalonProg: ", "failed to push file to firebase because " + e.getMessage());
        }
    }

    private void nextActivity(Intent intent) {

        sweetAlertDialog = new SweetAlertDialog(AddSalonActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();

        if (salon.getSalon_id() == null || salon.getSalon_id().isEmpty()){
            FirebaseFirestore.getInstance().collection("Salons")
                    .add(salon)
                    .addOnSuccessListener(documentReference -> {
                        salon.setSalon_id(documentReference.getId());
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.dismissWithAnimation();
                        Bundle bundle = new Bundle();
                        bundle.putString("salon_id", salon.getSalon_id());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                sweetAlertDialog.dismissWithAnimation();
                Log.d("AddSalonError", e.getLocalizedMessage());
                Toast.makeText(AddSalonActivity.this, "Try later", Toast.LENGTH_SHORT).show();
            });
        }else {
            sweetAlertDialog.dismissWithAnimation();
            Bundle bundle = new Bundle();
            bundle.putString("salon_id", salon.getSalon_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private class StylistAdapter extends RecyclerView.Adapter<AddSalonActivity.StylistAdapter.StylistViewHolder>{

        @NonNull
        @Override
        public AddSalonActivity.StylistAdapter.StylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddSalonActivity.StylistAdapter.StylistViewHolder(LayoutInflater.from(AddSalonActivity.this).inflate(R.layout.stylist_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddSalonActivity.StylistAdapter.StylistViewHolder holder, int position) {

            Glide.with(AddSalonActivity.this).load(stylistList.get(position).getStylist_profile_pic()).into(holder.stylist_image);
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


    public class ServicesAdapter extends RecyclerView.Adapter<AddSalonActivity.ServicesAdapter.ServiceViewHolder>{

        @NonNull
        @Override
        public AddSalonActivity.ServicesAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddSalonActivity.ServicesAdapter.ServiceViewHolder(LayoutInflater.from(AddSalonActivity.this).inflate(R.layout.service_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddSalonActivity.ServicesAdapter.ServiceViewHolder holder, int position) {

            Service service = serviceList.get(position);

            Glide.with(AddSalonActivity.this).load(service.getStylist_pic()).into(holder.stylist_dp);
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

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseFirestore.getInstance().collection("Stylists")
                .whereEqualTo("stylist_salon_id", salon.getSalon_id())
                .addSnapshotListener((value, error) -> {

                    if (value != null){
                        stylistList.clear();
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
                        serviceList.clear();
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

}