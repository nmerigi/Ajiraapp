package com.example.ajiraapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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
    private DatabaseReference expertsRef;
    private Context context;


    // accepting a list of JobRequest
    public JobRequestAdapter(List<Jobs> jobRequests , Context context) {
        this.jobRequests = jobRequests;
        this.context = context;
        this.clientsRef = FirebaseDatabase.getInstance().getReference("clients");
        this.expertsRef = FirebaseDatabase.getInstance().getReference("experts");

        // Filter for jobs with "Pending" status
        filterPendingJobs();
    }

    private void filterPendingJobs() {
        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");
        jobsRef.orderByChild("status").equalTo("Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobRequests.clear();  // Clear the list before adding new data
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Jobs job = jobSnapshot.getValue(Jobs.class);
                    jobRequests.add(job);  // Add only pending jobs
                }
                notifyDataSetChanged();  // Refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("JobRequestAdapter", "Error fetching jobs: " + error.getMessage());
            }
        });
    }



    @NonNull
    @Override
    public JobRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientinfo, parent, false);
        return new JobRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobRequestViewHolder holder, int position) {
        Jobs jobRequest = jobRequests.get(position);  //sooo in jobs list access job in a certain position eg. position 4

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
                            String clientPhoneNumber = jobRequest.getClientPhoneNumber();
                            String expertPhoneNumber = jobRequest.getExpertPhoneNumber();


                            holder.clientName.setText(fullName);
                            holder.clientLocation.setText(clientLocation);
                            holder.clientContact.setText(clientPhoneNumber);
                            holder.clientRating.setText(clientRating);


                            //for notifications letsssss get the client token
                            //String clientFCMToken = clientSnapshot.child("fcmToken").getValue(String.class);

                            expertsRef.orderByChild("phonenumber").equalTo(jobRequest.getExpertPhoneNumber())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot expertSnapshot) {
                                            if (expertSnapshot.exists()) {
                                                DataSnapshot expertDetails = expertSnapshot.getChildren().iterator().next();
                                                String expertName = expertDetails.child("firstname").getValue(String.class);

                                                // Set accept button click listener with expert's name
                                                holder.accept_button.setOnClickListener(v -> {
                                                    String jobId = jobRequest.getjobId();
                                                    DatabaseReference jobRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId);

                                                    // Update the status to "Accepted"
                                                    jobRef.child("status").setValue("Accepted").addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Job status updated to 'Accepted'", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(context, WorkInProgressExpert.class);
                                                            intent.putExtra("JOB_ID", jobId);
                                                            intent.putExtra("EXPERT_NAME", expertName);
                                                            intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                                                            intent.putExtra("EXPERT_CONTACT", expertPhoneNumber);
                                                            context.startActivity(intent);

                                                            Log.d("JobRequestAdapter", "Job status updated to 'Accepted'");
                                                        } else {
                                                            Log.e("JobRequestAdapter", "Failed to update job status.");
                                                        }
                                                    });


                                                });

                                            } else {
                                                Log.e("JobRequestAdapter", "Expert not found.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("JobRequestAdapter", "Error fetching expert data: " + error.getMessage());
                                        }
                                    });


                        } else {
                            holder.clientName.setText("Unknown");
                            holder.clientLocation.setText("Unknown");
                            holder.clientRating.setText("Unknown");
                            holder.clientContact.setText("Unknown");
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
        TextView clientName, clientLocation, clientRating, clientContact;
        Button accept_button;

        public JobRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.ClientName);
            clientLocation = itemView.findViewById(R.id.Location);
            clientRating = itemView.findViewById(R.id.Rating);
            clientContact = itemView.findViewById(R.id.Contact);
            accept_button= itemView.findViewById(R.id.accept_button);
        }
    }

}
