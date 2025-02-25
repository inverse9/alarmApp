package com.example.alarmapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockFragment extends Fragment {

    private TextView tvClock;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        tvClock = view.findViewById(R.id.tv_clock);

        startClockUpdater();
        return view;
    }

    private void startClockUpdater() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                tvClock.setText("Current Time: " + currentTime);
                handler.postDelayed(this, 1000);
            }
        });
    }
}
