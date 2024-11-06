package com.example.ajiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;

public class ClientHomePage extends AppCompatActivity {
    private CardView cleaningCard, cookingCard, mechanicCard, gardeningCard, movingCard, plumbingCard;
    private String clientPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_home_page);

        cleaningCard = findViewById(R.id.cleaningCard);
        cookingCard = findViewById(R.id.cookingCard);
        mechanicCard = findViewById(R.id.mechanicCard);
        gardeningCard = findViewById(R.id.gardeningCard);
        movingCard = findViewById(R.id.movingCard);
        plumbingCard = findViewById(R.id.plumbingCard);

        //picking the client phone number so i can tell who logged in and is selecting
        Intent intent = getIntent();
        clientPhoneNumber = intent.getStringExtra("CLIENT_PHONE_NUMBER");

        cleaningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, CleaningList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });

        cookingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, CookingList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });

        mechanicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, MechanicList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });

        gardeningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, GardeningList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });

        movingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, MovingList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });

        plumbingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientHomePage.this, PlumbingList.class);
                intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
                startActivity(intent);
            }
        });
    }
}