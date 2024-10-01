package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageReference;

public class LogIn extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;

    EditText login_email, login_password;
    Button login_button;
    TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database= FirebaseDatabase.getInstance();
        reference =database.getReference();

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= login_email.getText().toString().trim();
                String password= login_password.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LogIn.this, "Please enter both email and password",Toast.LENGTH_SHORT).show();
                }else{
                    authenticateUser(email, password);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String email, String password) {
        reference.child("clients").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Login", "Checking clients");
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                                Client client = clientSnapshot.getValue(Client.class);
                                if (client != null) {
                                    Log.d("Login", "Client found: " + client.getEmail());
                                    if (client.getPassword().equals(password)) {
                                        Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogIn.this, ClientHomePage.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                }
                            }
                        }

                        // If client login fails, check experts
                        reference.child("experts").orderByChild("email").equalTo(email)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot expertSnapshot) {
                                        Log.d("Login", "Checking experts");
                                        if (expertSnapshot.exists()) {
                                            for (DataSnapshot expert : expertSnapshot.getChildren()) {
                                                Expert expertUser = expert.getValue(Expert.class);
                                                if (expertUser != null) {
                                                    Log.d("Login", "Expert found: " + expertUser.getEmail());
                                                    if (expertUser.getPassword().equals(password)) {
                                                        Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LogIn.this, ExpertHomePage.class);
                                                        startActivity(intent);
                                                        finish();
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                        Toast.makeText(LogIn.this, "Login Failed: Invalid email or password", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(LogIn.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LogIn.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
