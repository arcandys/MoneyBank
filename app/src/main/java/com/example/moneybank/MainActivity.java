package com.example.moneybank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ComponentActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private DatabaseReference myRef;

    private List<Operation> operationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Firebase database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://moneybank-3c887-default-rtdb.europe-west1.firebasedatabase.app/");

        // Get the login form elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Get a reference to the "my_data" node
        myRef = database.getReference("data/user1");

        // Handle the login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the data from "my_data" node
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if the "my_data" node exists
                        if (dataSnapshot.exists()) {
                            String storedUsername = dataSnapshot.child("login").getValue(String.class);
                            String storedPassword = dataSnapshot.child("password").getValue(String.class);

                            String enteredUsername = username.getText().toString();
                            String enteredPassword = password.getText().toString();

                            if (storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword)) {
                                // Login successful
                                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                openMainScreen();
                            } else {
                                // Login failed
                                Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // "my_data" node does not exist
                            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any database error
                        Toast.makeText(MainActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Start the MainScreen activity
    private void openMainScreen() {
        // Create an Intent to start the MainScreen activity
        Intent intent = new Intent(this, MainScreen.class);

        // Start the MainScreen activity
        startActivity(intent);
    }

}
