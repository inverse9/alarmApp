package com.example.alarmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private LinearLayout alarmContainer;
    private static final int REQUEST_ADD_ALARM = 1;
//    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        alarmContainer = view.findViewById(R.id.test);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_alarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddAlarm.class);
                startActivityForResult(intent, REQUEST_ADD_ALARM);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            String time=data.getStringExtra("time");
            String label=data.getStringExtra("label");
            boolean isEnabled=data.getBooleanExtra("isEnabled",false);

            System.out.println("masuk"+time);
            addAlarmView(time,label,isEnabled);
        }
    }

    private void addAlarmView(String time, String label, boolean isEnabled) {
        System.out.println("teststsats");

        if (alarmContainer == null) {
            System.out.println("test alarmContainer is null");
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
}
