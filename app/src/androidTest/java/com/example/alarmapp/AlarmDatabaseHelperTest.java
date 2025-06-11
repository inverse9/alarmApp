package com.example.alarmapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AlarmDatabaseHelperTest {

    private AlarmDatabaseHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("alarms.db");  // Ensure clean DB for each test
        dbHelper = new AlarmDatabaseHelper(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testInsertAlarm() {
        dbHelper.insertAlarm("07:00", "Wake up", true);
        ArrayList<AlarmModel> alarms = dbHelper.getAllAlarms();

        assertEquals(1, alarms.size());
        assertEquals("07:00", alarms.get(0).getTime());
        assertEquals("Wake up", alarms.get(0).getLabel());
        assertTrue(alarms.get(0).isEnabled());
    }

    @Test
    public void testUpdateAlarm() {
        dbHelper.insertAlarm("07:00", "Wake up", true);
        AlarmModel alarm = dbHelper.getAllAlarms().get(0);

        dbHelper.updateAlarm(alarm.getId(), "08:00", "Updated", false);
        AlarmModel updated = dbHelper.getAllAlarms().get(0);

        assertEquals("08:00", updated.getTime());
        assertEquals("Updated", updated.getLabel());
        assertFalse(updated.isEnabled());
    }

    @Test
    public void testDeleteAlarm() {
        dbHelper.insertAlarm("07:00", "Wake up", true);
        AlarmModel alarm = dbHelper.getAllAlarms().get(0);

        dbHelper.deleteAlarm(alarm.getId());
        ArrayList<AlarmModel> alarms = dbHelper.getAllAlarms();

        assertTrue(alarms.isEmpty());
    }

    @Test
    public void testUpdateAlarmEnabled() {
        dbHelper.insertAlarm("07:00", "Wake up", false);
        AlarmModel alarm = dbHelper.getAllAlarms().get(0);

        dbHelper.updateAlarmEnabled(alarm.getId(), true);
        AlarmModel updated = dbHelper.getAllAlarms().get(0);

        assertTrue(updated.isEnabled());
    }
}
