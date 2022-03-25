package com.example.salonbookingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText edt_username, edt_email, edt_password, edt_password_confirm, edt_phone, edt_location;
    String dialogContent2;
    private SweetAlertDialog sweetAlertDialog;
    private CircleImageView img_userDp;
    private TextView txt_photo_error;
    private FirebaseAuth mAuth;
    private Uri image_uri;
    private ChipGroup chip_group;
    private Customer customer;
    private String userID;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        customer = new Customer();

        mAuth = FirebaseAuth.getInstance();
        dialogContent2 = this.getString(R.string.setting_up_photo);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                image_uri = result.getData().getData();
                Glide.with(this).load(image_uri).into(img_userDp);
            }
        });


        edt_username = findViewById(R.id.edt_username);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_password_confirm = findViewById(R.id.edt_password_confirm);
        txt_photo_error = findViewById(R.id.txt_photo_error);
        img_userDp = findViewById(R.id.img_userDp);
        edt_phone = findViewById(R.id.edt_phone);
        edt_location = findViewById(R.id.edt_location);
        chip_group = findViewById(R.id.chip_group);

        chip_group.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.chip_male:
                            customer.setCustomer_gender("male");
                            break;
                        case R.id.chip_female:
                            customer.setCustomer_gender("female");
                            break;
                    }
                });


        findViewById(R.id.btn_signUp).setOnClickListener(view -> signUpUser());
        findViewById(R.id.btn_toLogIn).setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, MainActivity.class)));
        img_userDp.setOnClickListener(view1 -> openGallery());


    }

    private void signUpUser() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_username.getText()).toString()))
            edt_username.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_email.getText()).toString()))
            edt_email.setError(this.getString(R.string.input_error));
        else if (Objects.requireNonNull(edt_password.getText()).toString().length() < 6)
            edt_password.setError(this.getString(R.string.password_length_error));
        else if (!Objects.requireNonNull(edt_password.getText()).toString().equals(Objects.requireNonNull(edt_password_confirm.getText()).toString()))
            edt_password_confirm.setError(this.getString(R.string.password_matching_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_phone.getText()).toString()))
            edt_phone.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_location.getText()).toString()))
            edt_location.setError(this.getString(R.string.input_error));
        else if (image_uri == null)
            txt_photo_error.setVisibility(View.VISIBLE);
        else {
            txt_photo_error.setVisibility(View.GONE);

            sweetAlertDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText(this.getString(R.string.creating_account_dialog_title));
            sweetAlertDialog.show();

            mAuth.createUserWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                    .addOnSuccessListener(runnable -> {
                        this.userID = Objects.requireNonNull(runnable.getUser()).getUid();
                        Log.d("CREATE_UPDATES", "userID: " + userID);

                        customer.setCustomer_id(userID);
                        customer.setCustomer_name(edt_username.getText().toString());
                        customer.setCustomer_email(edt_email.getText().toString());
                        customer.setCustomer_phone(edt_phone.getText().toString());
                        customer.setCustomer_location(edt_location.getText().toString());

                        FirebaseFirestore.getInstance().collection("Customer").document(userID)
                                .set(customer)
                                .addOnSuccessListener(runnable1 -> setDP())
                                .addOnFailureListener(runnable2 -> nextActivity());

                    }).addOnFailureListener(e -> {
                sweetAlertDialog.dismissWithAnimation();
                Toast.makeText(SignUpActivity.this, "Authentication failed. Try again later", Toast.LENGTH_SHORT).show();
                Log.d("CREATE_UPDATES: ", "Failed to create user because " + e.getMessage());
            });


        }
    }

    private void setDP() {
        Log.d("CREATE_UPDATES: ", "photo started to upload");
        sweetAlertDialog.setContentText(dialogContent2);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("profile_image");


        try {
            storageReference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                    FirebaseFirestore.getInstance().collection("Customer")
                            .document(this.userID).update("customerDP", uri.toString())
                            .addOnSuccessListener(unused -> {
                                Log.d("CREATE_UPDATES: ", "photo saved, registration complete");
                                Toast.makeText(SignUpActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.dismiss();
                                nextActivity();

                            }).addOnFailureListener(runnable1 -> Log.d("CREATE_UPDATES: ", "photo failed to upload because " + runnable1.getMessage()))
            ));
        } catch (Exception e) {
            Log.d("CREATE_UPDATES: ", "failed to push file to firebase because " + e.getMessage());
            nextActivity();
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(SignUpActivity.this, Dashboard.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", userID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intentActivityResultLauncher.launch(intent);
    }
}