package com.example.ajiraapp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MovingList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    ExpertList_adapter expertListAdapter;
    ArrayList<Expert> list;
    String clientPhoneNumber;
    Button searchbutton;
    EditText maxbudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_moving_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler);
        database = FirebaseDatabase.getInstance().getReference("experts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        maxbudget= findViewById(R.id.max_budget_input);
        searchbutton= findViewById(R.id.search_button);

        // Retrieve clientPhoneNumber from Intent
        Intent intent = getIntent();
        clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");
        expertListAdapter= new ExpertList_adapter(list,this,clientPhoneNumber);
        recyclerView.setAdapter(expertListAdapter);

        fetchExperts();

        searchbutton.setOnClickListener(v -> filterExpertsByBudget());
    }
    private void fetchExperts() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Expert expert = dataSnapshot.getValue(Expert.class);
                    if (expert != null && "Cleaning".equals(expert.getService())) {
                        list.add(expert);
                    }
                }
                expertListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MovingList.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterExpertsByBudget() {
        String budgetText = maxbudget.getText().toString().trim();
        if (TextUtils.isEmpty(budgetText)) {
            Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show();
            return;
        }

        double maxBudget;
        try {
            maxBudget = Double.parseDouble(budgetText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid budget input", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Expert> filteredList = new ArrayList<>();
        for (Expert expert : list) {
            String serviceChargeRange = expert.getServicecharge();
            String[] rangeParts = serviceChargeRange.split("-");

            // Validate and parse the range if it has two parts.
            if (rangeParts.length == 2) {
                try {
                    double minCharge = Double.parseDouble(rangeParts[0].trim());
                    double maxCharge = Double.parseDouble(rangeParts[1].trim());


                    if  ((maxBudget >= minCharge && maxBudget <= maxCharge) || maxCharge <= maxBudget) {
                        filteredList.add(expert);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid range format if necessary, e.g., skip this expert
                }
            }
        }

        // Update RecyclerView with the filtered list
        expertListAdapter = new ExpertList_adapter(filteredList, this, clientPhoneNumber);
        recyclerView.setAdapter(expertListAdapter);
    }
}