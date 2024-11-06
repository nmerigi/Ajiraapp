package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class WorkInProgressClient extends AppCompatActivity {
    private DatabaseReference jobsRef;
    Button jobDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress);

        jobDone = findViewById(R.id.job_done);

        // Get jobId so we can use it to identify the job to change to "Complete"
        Intent intent = getIntent();
        String jobId = intent.getStringExtra("JOB_ID");

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("Jobs");

        // Listen for jobDone button click
        jobDone.setOnClickListener(v -> {
            if (jobId != null) {
                // Update the status of the specific job to "Completed"
                jobsRef.child(jobId).child("status").setValue("Completed")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("WorkInProgressClient", "Job status updated to 'Completed'");
                            } else {
                                Log.e("WorkInProgressClient", "Failed to update job status.");
                            }
                        });

                // After updating the status, go to the next activity (Client_RatingandPayment)
                Intent intent2 = new Intent(WorkInProgressClient.this, Client_RatingandPayment.class);
                startActivity(intent2);
            } else {
                Log.e("WorkInProgressClient", "Job ID is null, cannot update job status.");
            }
        });
    }
}
