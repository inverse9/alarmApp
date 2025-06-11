package com.example.alarmapp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class AddAlarm extends AppCompatActivity {
    private int alarmId = -1;
    private String selectedTime = "";
    private TextView tvTimePicker;
    private EditText etLabel;
    private Switch switchEnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Button btnDelete = findViewById(R.id.btn_delete);

        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView btnCheck = findViewById(R.id.btn_check);
        tvTimePicker = findViewById(R.id.tv_time_picker);
        etLabel = findViewById(R.id.et_label);
        switchEnable = findViewById(R.id.switch_enable);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            alarmId = intent.getIntExtra("id", -1);
            selectedTime = intent.getStringExtra("time");
            String label = intent.getStringExtra("label");
            boolean isEnabled = intent.getBooleanExtra("isEnabled", true);

            tvTimePicker.setText(selectedTime);
            etLabel.setText(label);
            switchEnable.setChecked(isEnabled);
            toolbarTitle.setText("Edit Alarm");

            btnDelete.setVisibility(View.VISIBLE);
        } else {
            toolbarTitle.setText("Add Alarm");
            btnDelete.setVisibility(View.GONE);
        }

        tvTimePicker.setOnClickListener(v -> openTimePicker());

        btnBack.setOnClickListener(v -> finish());

        btnCheck.setOnClickListener(v -> {
            String label = etLabel.getText().toString().trim();
            boolean isEnabled = switchEnable.isChecked();

            if (selectedTime.isEmpty()) {
                tvTimePicker.setError("Please select a time");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("time", selectedTime);
            resultIntent.putExtra("label", label);
            resultIntent.putExtra("isEnabled", isEnabled);
            if (alarmId != -1) {
                resultIntent.putExtra("id", alarmId);
            }

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            if (alarmId != -1) {
                AlarmDatabaseHelper dbHelper = new AlarmDatabaseHelper(this);
                dbHelper.deleteAlarm(alarmId);

                Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void openTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    tvTimePicker.setText(selectedTime);
                }, 6, 0, true);
        timePickerDialog.show();
    }
}
