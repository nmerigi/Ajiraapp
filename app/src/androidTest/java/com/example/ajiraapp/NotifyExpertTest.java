package com.example.ajiraapp;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NotifyExpertTest {

    @Test
    public void testJobRequestCreation() throws InterruptedException {
        String expertName = "John Doe";
        String expertService = "Moving";
        String serviceCharge = "1000-2000";
        String expertPhone = "0712345678";
        String clientPhone = "0799999999";

        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, NotifyExpert.class);
        intent.putExtra("EXPERT_NAME", expertName);
        intent.putExtra("EXPERT_SERVICE", expertService);
        intent.putExtra("EXPERT_CHARGE", serviceCharge);
        intent.putExtra("EXPERT_CONTACT", expertPhone);
        intent.putExtra("CLIENT_PHONE_NUMBER", clientPhone);

        try (ActivityScenario<NotifyExpert> scenario = ActivityScenario.launch(intent)) {

            // Wait a bit for activity to initialize
            Thread.sleep(1000);

            // Simulate user clicking the Notify button
            onView(withId(R.id.create_jobButton)).perform(click());

            // Wait for Firebase to write
            Thread.sleep(3000);

            DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");

            jobsRef.get().addOnCompleteListener(task -> {
                boolean jobFound = false;
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String dbClient = child.child("clientPhoneNumber").getValue(String.class);
                        String dbExpert = child.child("expertPhoneNumber").getValue(String.class);
                        String dbStatus = child.child("status").getValue(String.class);

                        if (clientPhone.equals(dbClient) && expertPhone.equals(dbExpert) && "Pending".equals(dbStatus)) {
                            jobFound = true;
                            break;
                        }
                    }
                }
                assertTrue("Job request should be found in Firebase", jobFound);
            });

            Thread.sleep(4000); // wait for async Firebase
        }
    }
}
