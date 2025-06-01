package com.example.alarmapp;

public class AlarmModel {
    public int id;
    public String time;
    public String label;
    public boolean isEnabled;

    public AlarmModel(int id, String time, String label, boolean isEnabled) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.isEnabled = isEnabled;
    }
    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
