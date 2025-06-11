package com.example.alarmapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class ClockFragment extends Fragment {
    private ClockAdapter clockAdapter;
    private List<ClockItem> clockList = new ArrayList<>();
    private ClockDatabaseHelper dbHelper;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        dbHelper = new ClockDatabaseHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Button btnAddClock = view.findViewById(R.id.btnAddClock);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String defaultZoneId = TimeZone.getDefault().getID();
        List<ClockItem> allZones = dbHelper.getAllTimeZones();

        boolean alreadyHasDefault = false;
        for (ClockItem item : allZones) {
            if (item.getTimeZoneId().equals(defaultZoneId)) {
                alreadyHasDefault = true;
                break;
            }
        }

        if (!alreadyHasDefault) {
            dbHelper.addTimeZone(defaultZoneId);
            allZones.add(0, new ClockItem(defaultZoneId));
        }

        clockList = allZones;
        clockAdapter = new ClockAdapter(clockList);
        recyclerView.setAdapter(clockAdapter);

        btnAddClock.setOnClickListener(v -> showTimeZoneDialog());


        clockAdapter.setOnClockLongClickListener((position, clockItem) -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Time Zone")
                    .setMessage("Are you sure you want to delete this clock?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteTimeZone(clockItem.getTimeZoneId());
                        clockList.remove(position);
                        clockAdapter.notifyItemRemoved(position);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        startClockUpdater();
        return view;
    }

    private static class TimeZoneEntry {
        String zoneId;
        String label;
        int offsetMillis;

        TimeZoneEntry(String zoneId, String label, int offsetMillis) {
            this.zoneId = zoneId;
            this.label = label;
            this.offsetMillis = offsetMillis;
        }
    }


    private void startClockUpdater() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (clockAdapter != null) {
                    clockAdapter.refreshTimes();
                }
                long delay = 60000 - (System.currentTimeMillis() % 60000);
                handler.postDelayed(this, delay);
            }
        });
    }

    private void showTimeZoneDialog() {
        String[] allIds = TimeZone.getAvailableIDs();
        List<TimeZoneEntry> entries = new ArrayList<>();

        for (String id : allIds) {
            TimeZone tz = TimeZone.getTimeZone(id);
            int offsetMillis = tz.getRawOffset();
            int hours = offsetMillis / (1000 * 60 * 60);

            String[] parts = id.split("/");
            String label = parts[parts.length - 1].replace('_', ' ');
            String gmt = String.format("GMT%s%d", hours >= 0 ? "+" : "-", Math.abs(hours));

            entries.add(new TimeZoneEntry(id, label + " (" + gmt + ")", offsetMillis));
        }

        Collections.sort(entries, Comparator.comparingInt(e -> e.offsetMillis));

        String[] displayArray = new String[entries.size()];
        String[] zoneIds = new String[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            displayArray[i] = entries.get(i).label;
            zoneIds[i] = entries.get(i).zoneId;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Time Zone")
                .setItems(displayArray, (dialog, which) -> {
                    String selectedZoneId = zoneIds[which];
                    if (dbHelper.addTimeZone(selectedZoneId)) {
                        clockList.add(new ClockItem(selectedZoneId));
                        clockAdapter.notifyItemInserted(clockList.size() - 1);
                    } else {
                        Toast.makeText(requireContext(), "Time zone already added", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }



}
