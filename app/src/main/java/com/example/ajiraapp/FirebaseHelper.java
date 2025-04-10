package com.example.ajiraapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FirebaseHelper {

    private final DatabaseReference expertRef;

    public FirebaseHelper(DatabaseReference expertRef) {
        this.expertRef = expertRef;
    }

    // Method to calculate and update expert's rating based on all reviews
    public void updateExpertRating(String clientPhoneNumber) {
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("ClientReviews");
        reviewRef.orderByChild("clientPhoneNumber").equalTo(clientPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        float totalRating = 0;

                        // Calculate total rating and count reviews
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Float rating = snapshot.child("rating").getValue(Float.class);
                            if (rating != null) {
                                totalRating += rating;
                                count++;
                            }
                        }

                        // Calculate average rating
                        float averageRating = (count == 0) ? 0 : totalRating / count;

                        // Round to two decimal places using BigDecimal
                        BigDecimal roundedRating = new BigDecimal(averageRating).setScale(2, RoundingMode.HALF_UP);
                        float finalRating = roundedRating.floatValue();

                        // Update the rating in the "clients" table based on phone number
                        DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");
                        clientsRef.orderByChild("phonenumber").equalTo(clientPhoneNumber)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot expertSnapshot) {
                                        if (expertSnapshot.exists()) {
                                            // Get the first matching entry (assuming unique phone numbers)
                                            DataSnapshot expertDetails = expertSnapshot.getChildren().iterator().next();
                                            String generatedId = expertDetails.getKey();

                                            // Update the expert's rating in the database
                                            clientsRef.child(generatedId).child("rating").setValue(finalRating);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle failure
                                        // Log error or display a message
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle failure to retrieve reviews
                        // Log error or display a message
                    }
                });
    }
}
