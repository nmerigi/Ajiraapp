package com.example.ajiraapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class JobRequestAdapter extends RecyclerView.Adapter<JobRequestAdapter.JobRequestViewHolder> {
    private List<Jobs> jobRequests;
    private DatabaseReference clientsRef;
    private Context context;

    // Constructor accepting a list of JobRequest
    public JobRequestAdapter(List<Jobs> jobRequests , Context context) {
        this.jobRequests = jobRequests;
        this.context = context;
        this.clientsRef = FirebaseDatabase.getInstance().getReference("clients");
    }

    @NonNull
    @Override
    public JobRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientinfo, parent, false);
        return new JobRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobRequestViewHolder holder, int position) {
        Jobs jobRequest = jobRequests.get(position);

        clientsRef.orderByChild("phonenumber").equalTo(jobRequest.getClientPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            DataSnapshot clientSnapshot = snapshot.getChildren().iterator().next();
                            String clientFName = clientSnapshot.child("firstname").getValue(String.class);
                            String clientLName = clientSnapshot.child("lastname").getValue(String.class);
                            String fullName = clientFName + " " + clientLName;

                            String clientLocation = clientSnapshot.child("location").getValue(String.class);
                            Long ratingValue = clientSnapshot.child("rating").getValue(Long.class);
                            String clientRating = ratingValue != null ? String.valueOf(ratingValue) : "Unknown";

                            holder.clientName.setText(fullName);
                            holder.clientLocation.setText(clientLocation);
                            holder.clientRating.setText(clientRating);

                        } else {
                            holder.clientName.setText("Unknown");
                            holder.clientLocation.setText("Unknown");
                            holder.clientRating.setText("Unknown");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Log possible errors
                        Log.e("JobRequestsAdapter", "Error fetching client data: " + error.getMessage());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return jobRequests.size();
    }

    public static class JobRequestViewHolder extends RecyclerView.ViewHolder {
        TextView clientName, clientLocation, clientRating;

        public JobRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.ClientName);
            clientLocation = itemView.findViewById(R.id.Location);
            clientRating = itemView.findViewById(R.id.Rating);
        }
    }
}
