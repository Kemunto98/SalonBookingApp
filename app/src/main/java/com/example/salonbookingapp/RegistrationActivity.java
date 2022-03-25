package com.example.salonbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class RegistrationActivity extends AppCompatActivity {

    ChipGroup chipGroup;
    Chip customer_select, provider_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        chipGroup = findViewById(R.id.chipGroup);

        //Initialize views
        provider_select = chipGroup.findViewById(R.id.provider_select);
        customer_select = chipGroup.findViewById(R.id.customer_select);
        Log.d("Selected", String.valueOf(chipGroup.getCheckedChipId()));


        //Set default views

        findViewById(R.id.customer_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.provider_layout).setVisibility(View.GONE);
        Log.d("Selected", String.valueOf(chipGroup.getCheckedChipId()));

        findViewById(R.id.toDash).setOnClickListener(view -> startActivity(new Intent(this, Dashboard.class)));



        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == customer_select.getId()){

                findViewById(R.id.customer_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.provider_layout).setVisibility(View.GONE);
                Log.d("Selected", String.valueOf(chipGroup.getCheckedChipId()));

            }else if (checkedId == provider_select.getId()){

                findViewById(R.id.customer_layout).setVisibility(View.GONE);
                findViewById(R.id.provider_layout).setVisibility(View.VISIBLE);
                Log.d("Selected", String.valueOf(chipGroup.getCheckedChipId()));

            }
        });



    }
}