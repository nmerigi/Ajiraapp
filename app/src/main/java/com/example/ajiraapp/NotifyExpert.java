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

import java.util.UUID;

public class NotifyExpert extends AppCompatActivity {
    TextView expert_nameview, expert_serviceview, service_chargeview;
    Button create_jobButton;
    DatabaseReference jobsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notify_expert);

        expert_nameview = findViewById(R.id.expert_name);
        expert_serviceview = findViewById(R.id.expert_service);
        service_chargeview = findViewById(R.id.service_charge);
        create_jobButton = findViewById(R.id.create_jobButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

    private void createJob(String expertPhoneNumber,String clientPhoneNumber, String expertName){
        String jobId = UUID.randomUUID().toString();
        String status = "Pending";

        Jobs job = new Jobs(jobId, clientPhoneNumber, expertPhoneNumber, status);

        jobsDatabase.child(jobId).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, expertName + " has been notified of your job request", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NotifyExpert.this, "Failed to create job.", Toast.LENGTH_LONG).show();
            }
        });

    }

}
