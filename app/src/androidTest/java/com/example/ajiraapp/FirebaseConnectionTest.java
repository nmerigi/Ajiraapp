package com.example.ajiraapp;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class FirebaseConnectionTest {

    @Test
    public void testFirebaseConnection() throws InterruptedException {
        DatabaseReference testRef = FirebaseDatabase.getInstance().getReference("test_connection");
        CountDownLatch latch = new CountDownLatch(1);

        // Write test data
        testRef.setValue("connected").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                latch.countDown(); // Signal success
            }
        });

        // Wait up to 5 seconds for the task to complete
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Firebase write did not complete in time or failed", completed);
    }
}
