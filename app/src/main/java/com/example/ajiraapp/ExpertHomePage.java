package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ExpertHomePage extends AppCompatActivity {
    private RecyclerView recyclerViewJobs;
    private JobRequestAdapter jobAdapter;
    private List<Jobs> jobList;
    private DatabaseReference jobsRef;
    private DatabaseReference clientsRef;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home_page);

        recyclerViewJobs = findViewById(R.id.recyclerView);
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobRequestAdapter(jobList,this);
        recyclerViewJobs.setAdapter(jobAdapter);
        logout= findViewById(R.id.logout);

        // Get expert phone number from Intent
        Intent intent = getIntent();
        String expertPhoneNumber = intent.getStringExtra("EXPERT_PHONE_NUMBER");
        if (expertPhoneNumber != null) {
            Log.d("ExpertHomePage", "Expert phone number: " + expertPhoneNumber);
        } else {
            Log.d("ExpertHomePage", "Expert phone number is null");
        }

        // Initialize Firebase Database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("Jobs");
        clientsRef = database.getReference("clients");

        // Fetch job requests for the expert
        fetchJobRequests(expertPhoneNumber);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpertHomePage.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    private void fetchJobRequests(String expertPhoneNumber) {
        jobsRef.orderByChild("expertPhoneNumber").equalTo(expertPhoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        jobList.clear();

                        // Check if any jobs exist for the given expert phone number
                        if (snapshot.exists()) {
                            Log.d("ExpertHomePage", "Jobs found for expert: " + expertPhoneNumber);

                            for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                                Jobs job = jobSnapshot.getValue(Jobs.class);
                                if (job != null) {
                                    jobList.add(job);
                                }
                            }
                            jobAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("ExpertHomePage", "No jobs found for expert: " + expertPhoneNumber);
                            // Optionally, you can show a message to the user
                            Toast.makeText(ExpertHomePage.this, "No jobs found for this expert.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ExpertHomePage", "Error fetching job requests: " + error.getMessage());
                        Toast.makeText(ExpertHomePage.this, "Failed to load job requests.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
