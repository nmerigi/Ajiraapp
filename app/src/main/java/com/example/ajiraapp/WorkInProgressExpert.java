package com.example.ajiraapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WorkInProgressExpert extends AppCompatActivity {
    private DatabaseReference jobsRef;
    private ValueEventListener statusListener;
    Button  sosButton;
    private static final int SMS_PERMISSION_CODE = 1;
    private static final String EMERGENCY_CONTACT = "0727989671";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress_expert);

        sosButton = findViewById(R.id.sos_button);

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

        sosButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(WorkInProgressExpert.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WorkInProgressExpert.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            } else {
                fetchExpertDetailsAndSendMessage(expertPhoneNumber);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up the listener when the activity is destroyed
        if (statusListener != null && jobsRef != null) {
            jobsRef.removeEventListener(statusListener);
        }
    }

    private void fetchExpertDetailsAndSendMessage(String expertPhoneNumber) {
        DatabaseReference expertsRef = FirebaseDatabase.getInstance().getReference("experts");
        expertsRef.orderByChild("phonenumber").equalTo(expertPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot expertSnapshot = dataSnapshot.getChildren().iterator().next();
                            String expertName = expertSnapshot.child("firstname").getValue(String.class);
                            String location = expertSnapshot.child("location").getValue(String.class);
                            sendEmergencyMessage(expertName, location, expertPhoneNumber);
                        } else {
                            Toast.makeText(WorkInProgressExpert.this, "Expert details not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(WorkInProgressExpert.this, "Failed to retrieve expert details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmergencyMessage(String expertName, String location, String expertPhoneNumber) {
        if (expertName == null) expertName = "Client Name";
        if (location == null) location = "Unknown location";

        String message = "Emergency! " + expertName + " at " + location + " (Phone: " + expertPhoneNumber + ") is in danger or distress.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(EMERGENCY_CONTACT, null, message, null, null);
            Toast.makeText(this, "SOS message sent", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SOS message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String clientPhoneNumber = getIntent().getStringExtra("EXPERT_PHONE_NUMBER");
                fetchExpertDetailsAndSendMessage(clientPhoneNumber);
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
