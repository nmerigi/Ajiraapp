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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress_expert); // Ensure you set the correct layout

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("Jobs");

        Intent intent = getIntent();
        String jobId = intent.getStringExtra("JOB_ID");

        if (jobId != null) {
            jobsRef.child(jobId).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String status = snapshot.getValue(String.class);

                    if ("Completed".equals(status)) {

                        Intent intent2 = new Intent(WorkInProgressExpert.this, Expert_Rating.class);
                        startActivity(intent2);
                    } else {
                        Log.d("WorkInProgressExpert", "Job is not completed yet. Current status: " + status);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("WorkInProgressExpert", "Failed to read job status: " + error.getMessage());
                }
            });
        } else {
            Log.e("WorkInProgressExpert", "Job ID is null.");
        }
    }
}
