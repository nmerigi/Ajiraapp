package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.UUID;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.UUID;

public class NotifyExpert extends AppCompatActivity {
    TextView expert_nameview, expert_serviceview, service_chargeview;
    Button create_jobButton;
    DatabaseReference jobsDatabase;
    String jobId;  // Store the job ID to listen for updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_expert);

        expert_nameview = findViewById(R.id.expert_name);
        expert_serviceview = findViewById(R.id.expert_service);
        service_chargeview = findViewById(R.id.service_charge);
        create_jobButton = findViewById(R.id.create_jobButton);

        Intent intent = getIntent();
        String expertName = intent.getStringExtra("EXPERT_NAME");
        String expertService = intent.getStringExtra("EXPERT_SERVICE");
        String serviceCharge = intent.getStringExtra("EXPERT_CHARGE");
        String expertPhoneNumber = intent.getStringExtra("EXPERT_CONTACT");
        String clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");

        expert_nameview.setText("Expert Name: " + expertName);
        expert_serviceview.setText("Expert Service: " + expertService);
        service_chargeview.setText("Service Charge: " + serviceCharge);

        jobsDatabase = FirebaseDatabase.getInstance().getReference("Jobs");

        create_jobButton.setOnClickListener(v -> {
            boolean isClientPhoneNumberValid = clientPhoneNumber != null && !clientPhoneNumber.isEmpty();
            boolean isExpertPhoneNumberValid = expertPhoneNumber != null && !expertPhoneNumber.isEmpty();

            if (!isClientPhoneNumberValid && !isExpertPhoneNumberValid) {
                Toast.makeText(NotifyExpert.this, "Both Client and Expert phone numbers are missing.", Toast.LENGTH_LONG).show();
            } else if (!isClientPhoneNumberValid) {
                Toast.makeText(NotifyExpert.this, "Client phone number is missing.", Toast.LENGTH_LONG).show();
            } else if (!isExpertPhoneNumberValid) {
                Toast.makeText(NotifyExpert.this, "Expert phone number is missing.", Toast.LENGTH_LONG).show();
            } else {
                createJob(expertPhoneNumber, clientPhoneNumber, expertName);
            }
        });
    }

    private void createJob(String expertPhoneNumber, String clientPhoneNumber, String expertName) {
        jobId = UUID.randomUUID().toString();  // Generate a unique job ID
        String status = "Pending";

        Jobs job = new Jobs(jobId, clientPhoneNumber, expertPhoneNumber, status);

        jobsDatabase.child(jobId).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, expertName + " has been notified of your job request", Toast.LENGTH_LONG).show();

                // Now, let's start listening for job status changes
                listenForJobStatusChanges( expertName);
            } else {
                Toast.makeText(NotifyExpert.this, "Failed to create job.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void listenForJobStatusChanges(String expertName) {
        // Add a listener to the job status
        jobsDatabase.child(jobId).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String jobStatus = snapshot.getValue(String.class);

                if (jobStatus != null && jobStatus.equals("Accepted")) {
                    // If the job status isAccepted, notify the client
                    showJobAcceptedPopup(expertName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if the listener is cancelled
                Toast.makeText(NotifyExpert.this, "Failed to listen for status changes.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showJobAcceptedPopup(String expertName) {
        new AlertDialog.Builder(NotifyExpert.this)
                .setTitle("Job Accepted")
                .setMessage(expertName + " has accepted the job!")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();

                    Intent intent = new Intent(NotifyExpert.this, WorkInProgressClient.class);
                    intent.putExtra("JOB_ID", jobId);
                    startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }


}
