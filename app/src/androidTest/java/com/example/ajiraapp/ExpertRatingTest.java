package com.example.ajiraapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExpertRatingTest {

    @Before
    public void launchActivity() {
        // Prepare the intent with necessary data
        Intent intent = new Intent();
        intent.putExtra("EXPERT_CONTACT", "1234567890");  // Correct key for expert phone
        intent.putExtra("CLIENT_PHONE_NUMBER", "9876543210");  // Correct key for client phone
        intent.putExtra("JOB_ID", "job123");  // Correct key for job ID

        // Launch the Expert_Rating activity with the intent
        ActivityScenario<Expert_Rating> scenario = ActivityScenario.launch(Expert_Rating.class);
        scenario.onActivity(activity -> activity.getIntent().putExtras(intent.getExtras())); // Set the intent extras
    }

    @Test
    public void testRatingSubmissionFlow() {
        // Select a rating
        onView(withId(R.id.rating)).perform(click());

        // Enter feedback
        onView(withId(R.id.expert_comment)).perform(typeText("Great service!"));

        // Close keyboard if necessary
        onView(withId(R.id.expert_comment)).perform(ViewActions.closeSoftKeyboard());

        // Submit the review
        onView(withId(R.id.submit_review)).perform(click());

        // Print to the run output
        System.out.println("Review submission test completed.");
    }
}
