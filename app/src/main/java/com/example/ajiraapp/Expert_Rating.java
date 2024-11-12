package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Expert_Rating extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_rating);

        ratingBar = findViewById(R.id.rating);
        commentEditText = findViewById(R.id.expert_comment);
        submitReviewButton = findViewById(R.id.submit_review);


        submitReviewButton.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        // Get the rating and comment from the user
        float rating = ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();

        Intent intent = getIntent();
        String expertPhoneNumber = intent.getStringExtra("EXPERT_CONTACT");
        String clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");
        String jobId = intent.getStringExtra("JOB_ID");

        // Check if necessary data is available
        if (expertPhoneNumber == null || clientPhoneNumber == null || jobId == null) {
            Toast.makeText(this, "Missing data for review submission", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique ID for each review in the Review table
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("ClientReviews").push();

        // Save review data
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("jobId", jobId);
        reviewData.put("clientPhoneNumber", clientPhoneNumber);
        reviewData.put("expertPhoneNumber", expertPhoneNumber);
        reviewData.put("rating", rating);
        reviewData.put("review", comment);

        reviewRef.setValue(reviewData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                updateExpertRating(clientPhoneNumber);
            } else {
                Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateExpertRating(String clientPhoneNumber) {
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("ClientReviews");
        reviewRef.orderByChild("clientPhoneNumber").equalTo(clientPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        float totalRating = 0;

                        // Calculate the total rating and count of reviews
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Float rating = snapshot.child("rating").getValue(Float.class);
                            if (rating != null) {
                                totalRating += rating;
                                count++;
                            }
                        }

                        // Calculate average rating
                        float averageRating;
                        if (count == 0) {
                            averageRating = 0;
                        } else {
                            averageRating = totalRating / count;
                        }
                        // Round to two decimal places using BigDecimal
                        BigDecimal roundedRating = new BigDecimal(averageRating)
                                .setScale(2, RoundingMode.HALF_UP);
                        float finalRating = roundedRating.floatValue();


                        // Reference to the experts table, querying by phone number
                        DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");
                        clientsRef.orderByChild("phonenumber").equalTo(clientPhoneNumber)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot expertSnapshot) {
                                        if (expertSnapshot.exists()) {
                                            // Get the first matching entry (assuming unique phone numbers)
                                            DataSnapshot expertDetails = expertSnapshot.getChildren().iterator().next();
                                            String generatedId = expertDetails.getKey();

                                            // Update the rating under this generated ID
                                            clientsRef.child(generatedId).child("rating").setValue(finalRating);
                                            Intent intent= new Intent(Expert_Rating.this, ExpertHomePage.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Expert entry not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Failed to update expert rating", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Failed to retrieve expert reviews", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}