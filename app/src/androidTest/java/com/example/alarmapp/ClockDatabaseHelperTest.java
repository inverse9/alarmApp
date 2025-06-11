package com.example.alarmapp;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ClockDatabaseHelperTest {

    private ClockDatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new ClockDatabaseHelper(context);
        dbHelper.getWritableDatabase().delete("timezones", null, null);
    }

    @Test
    public void testAddTimeZone() {
        boolean result = dbHelper.addTimeZone("Asia/Jakarta");
        assertTrue(result);
    }

    @Test
    public void testPreventDuplicateTimeZone() {
        dbHelper.addTimeZone("Asia/Jakarta");
        boolean second = dbHelper.addTimeZone("Asia/Jakarta");
        assertFalse(second);
    }

    @Test
    public void testGetAllTimeZones() {
        dbHelper.addTimeZone("Asia/Jakarta");
        dbHelper.addTimeZone("Asia/Tokyo");

        List<ClockItem> zones = dbHelper.getAllTimeZones();
        assertEquals(2, zones.size());
    }

    @Test
    public void testDeleteTimeZone() {
        dbHelper.addTimeZone("Europe/London");
        boolean deleted = dbHelper.deleteTimeZone("Europe/London");
        assertTrue(deleted);

        List<ClockItem> zones = dbHelper.getAllTimeZones();
        assertTrue(zones.isEmpty());
    }
}
