package com.example.alarmapp;

import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.contrib.PickerActions.setTime;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Before
    public void launchMainActivity() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testAddAlarmAndDisplay() throws InterruptedException {
        onView(withId(R.id.fab_add_alarm)).perform(click());

        onView(withId(R.id.tv_time_picker)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(setTime(9, 30));

        onView(withText("OK")).perform(click());

        onView(withId(R.id.et_label))
                .perform(typeText("Test Alarm 2"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());

        onView(withId(R.id.btn_check)).perform(click());

        Thread.sleep(1000);

        onView(withText("09:30")).check(matches(isDisplayed()));
        onView(withText("Test Alarm 2")).check(matches(isDisplayed()));
    }

    @Test
    public void testToggleAlarmSwitch() throws InterruptedException {
        testAddAlarmAndDisplay();

        onView(withId(R.id.alarm_switch)).perform(click());
    }

    @Test
    public void testDeleteAlarm() throws InterruptedException {
        testAddAlarmAndDisplay();

        onView(withText("08:30")).perform(click());

        onView(withId(R.id.btn_delete)).perform(click());

        Thread.sleep(1000);

        onView(withText("08:30")).check(doesNotExist());
    }


}
