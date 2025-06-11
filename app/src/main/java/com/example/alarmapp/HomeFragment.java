package com.example.alarmapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private LinearLayout alarmContainer;
    private static final int REQUEST_ADD_ALARM = 1;
    private static final int REQUEST_EDIT_ALARM = 2;

    private View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }



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
    @SuppressLint("ScheduleExactAlarm")
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
                if (isEnabled) {
                    String[] timeParts = time.split(":");
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1);
                    }

                    AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(requireContext(), AlarmReceiver.class);
                    alarmIntent.putExtra("label", label);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            requireContext(), id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    if (alarmManager != null) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent
                        );
                    }
                }
            }
            loadAlarms();
        }
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
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1001) {
//            if (permissions.length > 0 && permissions[0].equals(Manifest.permission.POST_NOTIFICATIONS)) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getContext(), "Notification permission granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }


}
