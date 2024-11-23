package com.example.ajiraapp;

import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    // Declare Firebase components and form elements
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private EditText clientFirstname, clientLastname, clientDob, clientEmail, clientPhonenumber, clientLocation, clientPassword;
    private EditText expertFirstname, expertLastname, expertDob, expertEmail, expertServiceCharge, expertPhonenumber, expertLocation, expertPassword;
    private RadioButton clientRadioMale, clientRadioFemale, clientRadioOther;
    private RadioButton expertRadioMale, expertRadioFemale, expertRadioOther;
    private RadioGroup clientGenderRadioGroup, expertGenderRadioGroup;
    private RadioGroup signupRadioGroup;
    private RadioButton radioClient, radioExpert;
    private Button signupButton, client_upload_id_button, client_upload_good_conduct_button, expert_upload_id_button, expert_upload_good_conduct_button;
    private TextView clientIdFilename, clientGoodConductFilename, loginRedirectText;
    private TextView expertIdFilename, expertGoodConductFilename;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autocompleteTextView;

    private Uri clientUploadIdUri, clientUploadGoodConductUri, expertUploadIdUri, expertUploadGoodConductUri;
    private int FILE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Storage and Database
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        // Client form container
        LinearLayout clientFormContainer = findViewById(R.id.client_signup_container);

        // Expert form container
        LinearLayout expertFormContainer = findViewById(R.id.expert_signup_container);

        // Initialize UI elements
        initializeElements();

        // Set the initial visibility of the containers
        clientFormContainer.setVisibility(View.VISIBLE);
        expertFormContainer.setVisibility(View.GONE);

        // Setup the signup group listener for client/expert ......shows form depending on what they pick
        signupRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_client) {
                clientFormContainer.setVisibility(View.VISIBLE);
                expertFormContainer.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_expert) {
                clientFormContainer.setVisibility(View.GONE);
                expertFormContainer.setVisibility(View.VISIBLE);
            }
        });

        // Handle file upload buttons
        client_upload_id_button.setOnClickListener(v -> startFileSelection(1));
        client_upload_good_conduct_button.setOnClickListener(v -> startFileSelection(2));
        expert_upload_id_button.setOnClickListener(v -> startFileSelection(3));
        expert_upload_good_conduct_button.setOnClickListener(v -> startFileSelection(4));

        // Handle signup button click
        signupButton.setOnClickListener(v -> handleSignup());

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    private void startFileSelection(int requestCode) {
        FILE_REQUEST_CODE = requestCode;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            switch (requestCode) {
                case 1: uploadFile(fileUri, "client_id_images/", clientIdFilename); break;
                case 2: uploadFile(fileUri, "client_good_conduct_images/", clientGoodConductFilename); break;
                case 3: uploadFile(fileUri, "expert_id_images/", expertIdFilename); break;
                case 4: uploadFile(fileUri, "expert_good_conduct_images/", expertGoodConductFilename); break;
            }
        }
    }

    private void uploadFile(Uri fileUri, String path, TextView fileNameView) {
        String fileName = fileUri.getLastPathSegment();
        StorageReference fileRef = storageReference.child(path + fileName);
        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Extract the file name from the URL
                String[] urlParts = uri.toString().split("/");
                String shortFileName = urlParts[urlParts.length - 1].split("\\?")[0]; // Get only the file name

                // Set the shortened file name in the TextView
                fileNameView.setText(shortFileName);
                Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();

            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "File upload failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleSignup() {
        if (radioClient.isChecked()) {
            if (!validateClientFields()) return;

            String firstname = clientFirstname.getText().toString();
            String lastname = clientLastname.getText().toString();
            String gender = getSelectedGender(clientGenderRadioGroup);
            String email = clientEmail.getText().toString().trim();
            String dob = clientDob.getText().toString();
            String phonenumber = clientPhonenumber.getText().toString();
            String location = clientLocation.getText().toString();
            String password = clientPassword.getText().toString().trim();
            String userid_upload = clientIdFilename.getText().toString();
            String goodconduct_upload = clientGoodConductFilename.getText().toString();

            Client client = new Client(firstname, lastname, email, gender, dob, phonenumber, location, password, userid_upload, goodconduct_upload, 5.0);

            reference = database.getReference("clients");
            //kwa 'client' push client instance that we just created
            reference.push().setValue(client).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(SignUp.this, Verification_Notification.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (!validateExpertFields()) return;

            String firstname = expertFirstname.getText().toString();
            String lastname = expertLastname.getText().toString();
            String gender = getSelectedGender(expertGenderRadioGroup);
            String email = expertEmail.getText().toString();
            String dob = expertDob.getText().toString();
            String phonenumber = expertPhonenumber.getText().toString();
            String location = expertLocation.getText().toString();
            String password = expertPassword.getText().toString();
            String service = autocompleteTextView.getText().toString();
            String servicecharge = expertServiceCharge.getText().toString();
            String userid_upload = expertIdFilename.getText().toString();
            String goodconduct_upload = expertGoodConductFilename.getText().toString();

            Expert expert = new Expert(firstname, lastname, email, gender, dob, phonenumber, location, password, service, servicecharge, userid_upload, goodconduct_upload,5.0);

            reference = database.getReference("experts");
            reference.push().setValue(expert).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(SignUp.this, Verification_Notification.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateClientFields() {
        if (isFieldEmpty(clientFirstname) || isFieldEmpty(clientLastname) || isFieldEmpty(clientEmail) || isFieldEmpty(clientDob)
                || isFieldEmpty(clientPhonenumber) || isFieldEmpty(clientLocation) || isFieldEmpty(clientPassword)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (clientPassword.getText().toString().length() < 7) {
            Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (clientPhonenumber.getText().toString().length() != 10) {
            Toast.makeText(this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check age
        int age = calculateAge(clientDob.getText().toString());
        if (age < 18) {
            Toast.makeText(this, "You must be at least 18 years old", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private boolean validateExpertFields() {
        if (isFieldEmpty(expertFirstname) || isFieldEmpty(expertLastname) || isFieldEmpty(expertEmail) || isFieldEmpty(expertDob)
                || isFieldEmpty(expertPhonenumber) || isFieldEmpty(expertLocation) || isFieldEmpty(expertPassword)
                || isFieldEmpty(expertServiceCharge) || isFieldEmpty(autocompleteTextView)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (expertPassword.getText().toString().length() < 7) {
            Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (expertPhonenumber.getText().toString().length() != 10) {
            Toast.makeText(this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        int age = calculateAge(expertDob.getText().toString());
        if (age < 18) {
            Toast.makeText(this, "You must be at least 18 years old", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;



}
    // Calculate age based on date of birth
    private int calculateAge(String dob) {
        // Define the expected date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);  // Ensure strict parsing

        try {
            // Parse the dob string to a Date object
            Date birthDate = dateFormat.parse(dob);

            // Get the birth year, month, and day
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            int birthYear = birthCalendar.get(Calendar.YEAR);
            int birthMonth = birthCalendar.get(Calendar.MONTH);
            int birthDay = birthCalendar.get(Calendar.DAY_OF_MONTH);

            // Get the current year, month, and day
            Calendar currentCalendar = Calendar.getInstance();
            int currentYear = currentCalendar.get(Calendar.YEAR);
            int currentMonth = currentCalendar.get(Calendar.MONTH);
            int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

            // Calculate the age
            int age = currentYear - birthYear;

            // Adjust age if the birth date hasn't occurred yet this year
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age--;
            }

            return age;
        } catch (ParseException | java.text.ParseException e) {
            Toast.makeText(this, "Invalid date format. Use dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    // Method to initialize the UI elements
    private void initializeElements() {
        clientFirstname = findViewById(R.id.client_firstname);
        clientLastname = findViewById(R.id.client_lastname);
        clientDob = findViewById(R.id.client_dob);
        clientEmail = findViewById(R.id.client_email);
        clientPhonenumber = findViewById(R.id.client_phonenumber);
        clientLocation = findViewById(R.id.client_location);
        clientPassword = findViewById(R.id.client_Password);

        expertFirstname = findViewById(R.id.firstname);
        expertLastname = findViewById(R.id.lastname);
        expertDob = findViewById(R.id.dob);
        expertEmail = findViewById(R.id.email);
        expertServiceCharge = findViewById(R.id.service_charge);
        expertPhonenumber = findViewById(R.id.phonenumber);
        expertLocation = findViewById(R.id.location);
        expertPassword = findViewById(R.id.Password);

        clientRadioMale = findViewById(R.id.client_radio_male);
        clientRadioFemale = findViewById(R.id.client_radio_female);
        clientRadioOther = findViewById(R.id.client_radio_other);

        expertRadioMale = findViewById(R.id.radio_male);
        expertRadioFemale = findViewById(R.id.radio_female);
        expertRadioOther = findViewById(R.id.radio_other);

        clientGenderRadioGroup = findViewById(R.id.client_gender_radio_group);
        expertGenderRadioGroup = findViewById(R.id.gender_radio_group);

        signupRadioGroup = findViewById(R.id.signup_radio_group);
        radioClient = findViewById(R.id.radio_client);
        radioExpert = findViewById(R.id.radio_expert);

        signupButton = findViewById(R.id.signup_button);
        loginRedirectText= findViewById(R.id.loginRedirectText);

        clientIdFilename = findViewById(R.id.client_id_filename);
        clientGoodConductFilename = findViewById(R.id.client_good_conduct_filename);
        expertIdFilename = findViewById(R.id.id_filename);
        expertGoodConductFilename = findViewById(R.id.good_conduct_filename);
        client_upload_id_button=findViewById(R.id.client_upload_id_button);
        client_upload_good_conduct_button=findViewById(R.id.client_upload_good_conduct_button);
        expert_upload_id_button=findViewById(R.id.upload_id_button);
        expert_upload_good_conduct_button=findViewById(R.id.upload_good_conduct_button);

        textInputLayout = findViewById(R.id.textinputLayout);
        autocompleteTextView = findViewById(R.id.autocompleteTextView);

        String[] services_array = getResources().getStringArray(R.array.services_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown, services_array);
        autocompleteTextView.setAdapter(arrayAdapter);
    }

    private String getSelectedGender(RadioGroup genderRadioGroup) {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedGenderRadioButton = findViewById(selectedId);
            return selectedGenderRadioButton.getText().toString();
        }
        return "Not Selected";
    }
}
