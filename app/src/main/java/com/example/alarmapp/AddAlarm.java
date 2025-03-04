package com.example.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

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
            Intent resultIntent=new Intent();
            resultIntent.putExtra("time","06:00 AM");
            resultIntent.putExtra("label","test");
            resultIntent.putExtra("isEnabled",true);
            setResult(Activity.RESULT_OK,resultIntent);
            finish();
        });
    }


}

