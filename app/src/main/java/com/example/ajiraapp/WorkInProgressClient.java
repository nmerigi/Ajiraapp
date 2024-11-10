package com.example.ajiraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkInProgressClient extends AppCompatActivity {
    private DatabaseReference jobsRef;
    Button jobDone, sosButton;
    private static final int SMS_PERMISSION_CODE = 1;
    private static final String EMERGENCY_CONTACT = "0727989671";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress);

        jobDone = findViewById(R.id.job_done);
        sosButton = findViewById(R.id.sos_button);

        Intent intent = getIntent();
        String expertPhoneNumber = intent.getStringExtra("EXPERT_CONTACT");
        String clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");
        String jobId = intent.getStringExtra("JOB_ID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("Jobs");

        jobDone.setOnClickListener(v -> {
            if (jobId != null) {
                jobsRef.child(jobId).child("status").setValue("Completed")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("WorkInProgressClient", "Job status updated to 'Completed'");
                                Intent intent2 = new Intent(WorkInProgressClient.this, Client_RatingandPayment.class);
                                intent2.putExtra("JOB_ID", jobId);
                                intent2.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                                intent2.putExtra("EXPERT_CONTACT", expertPhoneNumber);
                                startActivity(intent2);
                            } else {
                                Log.e("WorkInProgressClient", "Failed to update job status.");
                            }
                        });
            } else {
                Log.e("WorkInProgressClient", "Job ID is null, cannot update job status.");
            }
        });

        sosButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(WorkInProgressClient.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WorkInProgressClient.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            } else {
                fetchClientDetailsAndSendMessage(clientPhoneNumber);
            }
        });
    }

    private void fetchClientDetailsAndSendMessage(String clientPhoneNumber) {
        DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");
        clientsRef.orderByChild("phonenumber").equalTo(clientPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot clientSnapshot = dataSnapshot.getChildren().iterator().next();
                            String clientName = clientSnapshot.child("firstname").getValue(String.class);
                            String location = clientSnapshot.child("location").getValue(String.class);
                            sendEmergencyMessage(clientName, location, clientPhoneNumber);
                        } else {
                            Toast.makeText(WorkInProgressClient.this, "Client details not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(WorkInProgressClient.this, "Failed to retrieve client details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmergencyMessage(String clientName, String location, String clientPhoneNumber) {
        if (clientName == null) clientName = "Client Name";
        if (location == null) location = "Unknown location";

        String message = "Emergency! " + clientName + " at " + location + " (Phone: " + clientPhoneNumber + ") is in danger or distress.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(EMERGENCY_CONTACT, null, message, null, null);
            Toast.makeText(this, "SOS message sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SOS message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String clientPhoneNumber = getIntent().getStringExtra("CLIENT_PHONE_NUMBER");
                fetchClientDetailsAndSendMessage(clientPhoneNumber);
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
