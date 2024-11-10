package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class WorkInProgressExpert extends AppCompatActivity {
    private DatabaseReference jobsRef;
    private ValueEventListener statusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress_expert);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("Jobs");

        Intent intent = getIntent();
        String expertPhoneNumber = intent.getStringExtra("EXPERT_CONTACT");
        String clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");
        String jobId = intent.getStringExtra("JOB_ID");

        if (jobId != null) {
            DatabaseReference jobStatusRef = jobsRef.child(jobId).child("status");

            // Set up a real-time listener to detect status changes
            statusListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String status = snapshot.getValue(String.class);

                    if ("Completed".equals(status)) {
                        // Remove listener to avoid re-triggering the event
                        jobStatusRef.removeEventListener(statusListener);

                        // Navigate to the rating screen
                        Intent intent2 = new Intent(WorkInProgressExpert.this, Expert_Rating.class);
                        intent2.putExtra("JOB_ID", jobId);
                        intent2.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                        intent2.putExtra("EXPERT_CONTACT", expertPhoneNumber);
                        startActivity(intent2);
                        finish(); // Close the current activity
                    } else {
                        Log.d("WorkInProgressExpert", "Job is not completed yet. Current status: " + status);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("WorkInProgressExpert", "Failed to read job status: " + error.getMessage());
                }
            };

            // Attach the listener to monitor changes in real-time
            jobStatusRef.addValueEventListener(statusListener);
        } else {
            Log.e("WorkInProgressExpert", "Job ID is null.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up the listener when the activity is destroyed
        if (statusListener != null && jobsRef != null) {
            jobsRef.removeEventListener(statusListener);
        }
    }
}
