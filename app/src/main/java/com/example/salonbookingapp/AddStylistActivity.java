package com.example.salonbookingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddStylistActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private Stylist stylist;
    private String salonId;
    private Uri image_uri;
    private CircleImageView stylistDP;
    private SweetAlertDialog sweetAlertDialog;
    private TextInputEditText edt_stylist_name, edt_stylist_phone, edt_stylist_email;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stylist);

        this.salonId = getIntent().getExtras().getString("salon_id");
        stylistDP = findViewById(R.id.stylistDP);
        edt_stylist_name = findViewById(R.id.edt_stylist_name);
        edt_stylist_phone = findViewById(R.id.edt_stylist_phone);
        edt_stylist_email = findViewById(R.id.edt_stylist_email);
        ChipGroup chip_group = findViewById(R.id.chip_group);

        stylist = new Stylist();

        chip_group.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.chip_male:
                    stylist.setGender("male");
                    break;
                case R.id.chip_female:
                    stylist.setGender("female");
                    break;
            }
        });

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                image_uri = result.getData().getData();
                Glide.with(AddStylistActivity.this).load(image_uri).into(stylistDP);
            }
        });

        stylistDP.setOnClickListener(view1 -> openGallery());
        findViewById(R.id.btn_addStylist).setOnClickListener(view -> addStylist());




    }

    private void addStylist() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_stylist_name.getText()).toString()))
            edt_stylist_name.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_stylist_phone.getText()).toString()))
            edt_stylist_phone.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_stylist_email.getText()).toString()))
            edt_stylist_email.setError(this.getString(R.string.input_error));
        else if (image_uri == null)
            findViewById(R.id.photo_error).setVisibility(View.VISIBLE);
        else {
            sweetAlertDialog = new SweetAlertDialog(AddStylistActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Adding stylist");
            sweetAlertDialog.show();

            stylist.setStylist_name(edt_stylist_name.getText().toString());
            stylist.setStylist_phone(edt_stylist_phone.getText().toString());
            stylist.setStylist_email(edt_stylist_email.getText().toString());
            stylist.setStylist_salon_id(salonId);

            FirebaseFirestore.getInstance().collection("Stylists")
                    .add(stylist)
                    .addOnSuccessListener(documentReference -> setHisImage(documentReference.getId()))
                    .addOnFailureListener(e -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Log.d("StylistAddError", e.getLocalizedMessage());
                        Toast.makeText(AddStylistActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void setHisImage(String stylist_id) {
        Log.d("StylistAddError: ", "photo started to upload");
        sweetAlertDialog.setContentText("Setting photo");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                .child("Stylist_images");

        try {
            storageReference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri ->

                    FirebaseFirestore.getInstance().collection("Stylists")
                            .document(stylist_id).update("stylist_profile_pic", uri.toString())
                            .addOnSuccessListener(unused -> {
                                Log.d("StylistAddError: ", "photo saved, registration complete");
                                Toast.makeText(AddStylistActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.dismiss();
                                AddStylistActivity.super.onBackPressed();

                            }).addOnFailureListener(runnable1 -> {
                                sweetAlertDialog.dismissWithAnimation();
                                Toast.makeText(AddStylistActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                Log.d("CREATE_UPDATES: ", "photo failed to upload because " + runnable1.getMessage());
                            })));
        } catch (Exception e) {
            sweetAlertDialog.dismissWithAnimation();
            Toast.makeText(AddStylistActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("StylistAddError: ", "failed to push file to firebase because " + e.getMessage());
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intentActivityResultLauncher.launch(intent);
    }



}