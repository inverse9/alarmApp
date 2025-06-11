package com.example.alarmapp;

public class ClockModel {
    String timeZoneId;

    public ClockModel(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }
}
