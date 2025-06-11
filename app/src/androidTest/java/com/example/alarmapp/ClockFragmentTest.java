package com.example.alarmapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class ClockFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddTimeZoneToClockList() throws InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.nav_clock)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.btnAddClock)).perform(click());
        Thread.sleep(5000);
        onView(withText("Samoa (GMT-11)")).perform(click());
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Samoa (GMT-11)"))));
    }

    @Test
    public void testDeleteTimeZoneFromClockList() throws InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.nav_clock)).perform(click());
        Thread.sleep(3000);

        onView(withText("Singapore (GMT+8)")).perform(longClick());
        Thread.sleep(2000);
        onView(withText("Yes")).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.recyclerView)).check(matches(not(hasDescendant(withText("Singapore (GMT+8)")))));
    }

}
