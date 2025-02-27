package com.example.alarmapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddAlarm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView btnCheck = findViewById(R.id.btn_check);

        btnBack.setOnClickListener(v -> finish());

        btnCheck.setOnClickListener(v -> {
            Toast.makeText(AddAlarm.this, "Alarm Saved!", Toast.LENGTH_SHORT).show();
        });
    }


}

