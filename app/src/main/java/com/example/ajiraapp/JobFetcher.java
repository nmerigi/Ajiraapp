package com.example.ajiraapp;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class JobFetcher {

    public interface JobFetchCallback {
        void onJobsFetched(List<Jobs> jobs);
        void onError(String errorMessage);
    }

    private DatabaseReference jobsRef;

    public JobFetcher(DatabaseReference jobsRef) {
        this.jobsRef = jobsRef;
    }

    public void fetchJobsForExpert(String expertPhoneNumber, JobFetchCallback callback) {
        jobsRef.orderByChild("expertPhoneNumber").equalTo(expertPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Jobs> jobList = new ArrayList<>();
                        if (snapshot.exists()) {
                            for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                                Jobs job = jobSnapshot.getValue(Jobs.class);
                                if (job != null) {
                                    jobList.add(job);
                                }
                            }
                            callback.onJobsFetched(jobList);
                        } else {
                            callback.onJobsFetched(jobList); // Empty list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }
}
