package com.example.ajiraapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;

public class JobFetcherTest {

    private DatabaseReference mockJobsRef;
    private Query mockQuery;
    private JobFetcher jobFetcher;

    @Before
    public void setUp() {
        mockJobsRef = mock(DatabaseReference.class);
        mockQuery = mock(Query.class);
        jobFetcher = new JobFetcher(mockJobsRef);

        when(mockJobsRef.orderByChild("expertPhoneNumber")).thenReturn(mockQuery);
        when(mockQuery.equalTo(anyString())).thenReturn(mockQuery);
    }

    @Test
    public void testFetchJobsForExpert_successful() {
        String expertPhone = "0712345678";
        JobFetcher.JobFetchCallback callback = mock(JobFetcher.JobFetchCallback.class);

        jobFetcher.fetchJobsForExpert(expertPhone, callback);

        // Capture listener
        ArgumentCaptor<ValueEventListener> captor = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockQuery).addListenerForSingleValueEvent(captor.capture());
        ValueEventListener listener = captor.getValue();

        // Simulate Firebase snapshot
        DataSnapshot mockSnapshot = mock(DataSnapshot.class);
        DataSnapshot mockChild = mock(DataSnapshot.class);
        Jobs mockJob = new Jobs(); // Your Jobs class should have a public no-args constructor

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getChildren()).thenReturn(List.of(mockChild));
        when(mockChild.getValue(Jobs.class)).thenReturn(mockJob);

        // Trigger onDataChange
        listener.onDataChange(mockSnapshot);

        verify(callback).onJobsFetched(anyList());
    }

    @Test
    public void testFetchJobsForExpert_error() {
        String expertPhone = "0712345678";
        JobFetcher.JobFetchCallback callback = mock(JobFetcher.JobFetchCallback.class);

        jobFetcher.fetchJobsForExpert(expertPhone, callback);

        ArgumentCaptor<ValueEventListener> captor = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockQuery).addListenerForSingleValueEvent(captor.capture());
        ValueEventListener listener = captor.getValue();

        // Simulate Firebase error
        DatabaseError mockError = mock(DatabaseError.class);
        when(mockError.getMessage()).thenReturn("Firebase error");

        listener.onCancelled(mockError);

        verify(callback).onError("Firebase error");
    }
}
