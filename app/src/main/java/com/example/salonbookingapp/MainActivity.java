package com.example.salonbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText edt_emailLogIn, edt_passwordLogIn;
    private SweetAlertDialog sweetAlertDialog;
    private TextView txt_logInError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_emailLogIn = findViewById(R.id.edt_emailLogIn);
        edt_passwordLogIn = findViewById(R.id.edt_passwordLogIn);
        txt_logInError = findViewById(R.id.txt_logInError);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            nextActivity();
        }


        findViewById(R.id.to_signUp).setOnClickListener(view ->
                startActivity(new Intent(this, SignUpActivity.class)));

        findViewById(R.id.btn_logIn).setOnClickListener(view -> logInUser());

        findViewById(R.id.txt_forgot_password).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)));



    }

    private void logInUser() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_emailLogIn.getText()).toString())) edt_emailLogIn.setError(MainActivity.this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_passwordLogIn.getText()).toString())) edt_passwordLogIn.setError(MainActivity.this.getString(R.string.input_error));
        else {
            sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText(MainActivity.this.getString(R.string.LogIn_dialog_title));
            sweetAlertDialog.show();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(edt_emailLogIn.getText().toString(), edt_passwordLogIn.getText().toString())
                    .addOnSuccessListener(runnable ->{
                        sweetAlertDialog.dismissWithAnimation();
                        nextActivity();
                    } ).addOnFailureListener(runnable -> {
                sweetAlertDialog.dismissWithAnimation();
                txt_logInError.setText(runnable.getLocalizedMessage());
                findViewById(R.id.txt_logInError).setVisibility(View.VISIBLE);
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}