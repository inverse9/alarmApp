package com.example.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private LinearLayout alarmContainer;
    private static final int REQUEST_ADD_ALARM = 1;
    private static final int REQUEST_EDIT_ALARM = 2;

    private View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        alarmContainer = view.findViewById(R.id.test);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_alarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddAlarm.class);
                startActivityForResult(intent, REQUEST_ADD_ALARM);
            }
        });
        loadAlarms();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadAlarms();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ADD_ALARM || requestCode == REQUEST_EDIT_ALARM)
                && resultCode == Activity.RESULT_OK && data != null) {
            AlarmDatabaseHelper dbHelper = new AlarmDatabaseHelper(getContext());
            if (data.getBooleanExtra("delete", false)) {
                int id = data.getIntExtra("id", -1);
                if (id != -1) {
                    dbHelper.deleteAlarm(id);
                    Toast.makeText(getContext(), "Alarm deleted", Toast.LENGTH_SHORT).show();
                }
            } else {
                int id = data.getIntExtra("id", -1);
                String time = data.getStringExtra("time");
                String label = data.getStringExtra("label");
                boolean isEnabled = data.getBooleanExtra("isEnabled", true);

                if (id == -1) {
                    dbHelper.insertAlarm(time, label, isEnabled);
                    Toast.makeText(getContext(), "Alarm added", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updateAlarm(id, time, label, isEnabled);
                    Toast.makeText(getContext(), "Alarm updated", Toast.LENGTH_SHORT).show();
                }
                Log.d("AlarmEdit", "Received ID: " + id);

        }
            loadAlarms();
    }
    }

    private void addAlarmView(String time, String label, boolean isEnabled) {
        if (alarmContainer == null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View alarmView = inflater.inflate(R.layout.alarm_item, alarmContainer, false);

        TextView timeTextView = alarmView.findViewById(R.id.alarm_time);
        TextView labelTextView = alarmView.findViewById(R.id.alarm_label);
        Switch switchToggle = alarmView.findViewById(R.id.alarm_switch);

        timeTextView.setText(time);
        labelTextView.setText(label);
        switchToggle.setChecked(isEnabled);
        alarmContainer.addView(alarmView);
        alarmContainer.post(() -> alarmContainer.invalidate());
    }

    private void loadAlarms() {
        AlarmDatabaseHelper dbHelper = new AlarmDatabaseHelper(getContext());
        ArrayList<AlarmModel> alarms = dbHelper.getAllAlarms();

        LinearLayout container = view.findViewById(R.id.test);
        container.removeAllViews();

        for (AlarmModel alarm : alarms) {
            View alarmView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, container, false);

            TextView tvTime = alarmView.findViewById(R.id.alarm_time);
            TextView tvLabel = alarmView.findViewById(R.id.alarm_label);
            Switch switchEnable = alarmView.findViewById(R.id.alarm_switch);

            tvTime.setText(alarm.time);
            tvLabel.setText(alarm.label);
            switchEnable.setChecked(alarm.isEnabled);

            switchEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update DB with new switch state
                    AlarmDatabaseHelper dbHelper = new AlarmDatabaseHelper(getContext());
                    dbHelper.updateAlarmEnabled(alarm.getId(), isChecked);
                }
            });
            alarmView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AddAlarm.class);
                intent.putExtra("id", alarm.getId());
                intent.putExtra("time", alarm.getTime());
                intent.putExtra("label", alarm.getLabel());
                intent.putExtra("isEnabled", alarm.isEnabled());
                startActivityForResult(intent, REQUEST_EDIT_ALARM);
            });
            container.addView(alarmView);
        }
    }

}
