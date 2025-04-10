package com.example.ajiraapp;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class SignUpTest_Expert {

    @Rule
    public ActivityTestRule<SignUp> activityRule = new ActivityTestRule<>(SignUp.class);

    // Initialize Intents before each test
    @Before
    public void setUp() {
        Intents.init();
    }

    // Release Intents after each test
    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testExpertSignUp() {
        // Open the SignUp screen (assumed setup)

        // Select the Expert option
        Espresso.onView(ViewMatchers.withId(R.id.radio_expert)).perform(ViewActions.click());

        // Fill in the form with valid expert details
        Espresso.onView(ViewMatchers.withId(R.id.firstname)).perform(ViewActions.typeText("Jane"));
        Espresso.onView(ViewMatchers.withId(R.id.lastname)).perform(ViewActions.typeText("Doe"));
        Espresso.onView(ViewMatchers.withId(R.id.radio_male)).perform(ViewActions.click()); // Select male gender
        Espresso.onView(ViewMatchers.withId(R.id.dob)).perform(ViewActions.typeText("01/01/1990"));
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("janedoe@example.com"));
        // Click to open the dropdown menu
        Espresso.onView(ViewMatchers.withId(R.id.autocompleteTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        // Select the "Consulting" item from the dropdown
        Espresso.onData(org.hamcrest.Matchers.equalTo("Cooking"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click());
        // Scroll to the EditText first
        Espresso.onView(ViewMatchers.withId(R.id.service_charge))
                .perform(ViewActions.scrollTo()) // ensures it's in view
                .check(matches(isDisplayed()))
                .perform(ViewActions.click()) // brings up the keyboard
                .perform(ViewActions.typeText("1000-3000"));

        Espresso.onView(ViewMatchers.withId(R.id.phonenumber))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234567890"));

        Espresso.onView(ViewMatchers.withId(R.id.location))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("New York"));

        Espresso.onView(ViewMatchers.withId(R.id.Password))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Password123"));

        // Step 2: Trigger the ID image upload button
        Espresso.onView(ViewMatchers.withId(R.id.upload_id_button))
                .perform(ViewActions.scrollTo())  // Ensure the view is scrolled into view
                .check(matches(isDisplayed()))   // Verify it is displayed
                .perform(ViewActions.click());   // Click the button

// Step 3: Simulate selecting the ID image
// Create a temporary file for testing
        File tempFileForId = new File(InstrumentationRegistry.getInstrumentation().getTargetContext().getCacheDir(), "fake_id_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFileForId)) {
            fos.write(new byte[1024]); // Write some dummy data
        } catch (IOException e) {
            e.printStackTrace();
        }

// Use the file URI for the ID image
        Uri fileUriForId = Uri.fromFile(tempFileForId);

        Intent resultDataForId = new Intent();
        resultDataForId.setData(fileUriForId);
        Instrumentation.ActivityResult resultForId = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultDataForId);

// Stub the intent to simulate selecting the ID image
        intending(allOf(hasAction(Intent.ACTION_PICK), hasType("image/*"))).respondWith(resultForId);

// Step 4: Verify the ID file name is displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.id_filename))
                .perform(ViewActions.scrollTo())  // Ensure the view is scrolled into view
                .check(matches(withText("expert_id_images%2F1000001521")));  // Verify the text is displayed

// Add a delay of 2 seconds
        try {
            Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Step 5: Trigger the Good Conduct image upload button
        Log.d("TEST", "Before clicking Good Conduct button");
        Espresso.onView(ViewMatchers.withId(R.id.upload_good_conduct_button))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .perform(ViewActions.click());
        Log.d("TEST", "After clicking Good Conduct button");

        // Wait until the UI thread is idle to confirm actions are complete
        Espresso.onIdle();

// Step 6: Simulate selecting the Good Conduct image
// Create a temporary file for testing
        File tempFileForGoodConduct = new File(InstrumentationRegistry.getInstrumentation().getTargetContext().getCacheDir(), "fake_good_conduct_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFileForGoodConduct)) {
            fos.write(new byte[1024]); // Write some dummy data
        } catch (IOException e) {
            e.printStackTrace();
        }

// Use the file URI for the Good Conduct image
        Uri fileUriForGoodConduct = Uri.fromFile(tempFileForGoodConduct);

        Intent resultDataForGoodConduct = new Intent();
        resultDataForGoodConduct.setData(fileUriForGoodConduct);
        Instrumentation.ActivityResult resultForGoodConduct = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultDataForGoodConduct);

// Stub the intent to simulate selecting the Good Conduct image
        intending(allOf(hasAction(Intent.ACTION_PICK), hasType("image/*")))
                .respondWith(resultForGoodConduct);

// Step 7: Verify the Good Conduct file name is displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.good_conduct_filename))
                .perform(ViewActions.scrollTo())  // Ensure the view is scrolled into view
                .check(matches(withText("expert_good_conduct_images%2Ffake_id_image.jpg")));  // Verify the text is displayed
// Add logs if needed to debug
        Espresso.onView(ViewMatchers.withId(R.id.good_conduct_filename))
                .check((view, noViewFoundException) -> {
                    String text = ((TextView) view).getText().toString();
                    Log.d("TEST", "Good Conduct File Text: " + text); // Log the file name
                });


// Tap on the "Sign Up" button
        Espresso.onView(ViewMatchers.withId(R.id.signup_button))
                .perform(ViewActions.scrollTo())  // Ensure the view is scrolled into view
                .perform(ViewActions.click());   // Click the "Sign Up" button


        // Wait for Firebase to save the data (not ideal, but acceptable for now)
        try {
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Firebase test: Wait for the Firebase update to complete
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("experts");

        // Assuming you have a unique expert ID or some logic to handle that
        String expectedPhoneNumber = "1234567890";

        reference.child("phonenumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the expert data is saved correctly
                assertNotNull(dataSnapshot);
                assertEquals("Jane", dataSnapshot.child("firstname").getValue(String.class));
                assertEquals("Doe", dataSnapshot.child("lastname").getValue(String.class));
                assertEquals("01/01/1991", dataSnapshot.child("dob").getValue(String.class));
                assertEquals("janedoe@example.com", dataSnapshot.child("email").getValue(String.class));
                assertEquals("Cooking", dataSnapshot.child("service").getValue(String.class));
                assertEquals("1000-3000", dataSnapshot.child("service_charge").getValue(String.class));
                assertEquals("1234567890", dataSnapshot.child("phonenumber").getValue(String.class));
                assertEquals("New York", dataSnapshot.child("location").getValue(String.class));
                assertEquals("Password123", dataSnapshot.child("password").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Optional Helper function to check Firebase for expert
    private void checkFirebaseForExpert(String name) {
        // Get Firebase instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("experts");

        // This method will check if the expert data with the given name exists in Firebase
        ref.orderByChild("firstname").equalTo(name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // The expert data was found in Firebase
                        assert task.getResult().exists();
                    } else {
                        // Data was not found in Firebase, which means the test failed
                        assert false : "Expert data not saved to Firebase";
                    }
                });
    }
}
