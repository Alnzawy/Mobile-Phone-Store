package com.example.finalproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Signup extends AppCompatActivity {
    private EditText nameEditText, passwordEditText;
    private Button signupButton, backButton;
    private Spinner typeSpinner;
    private SQLiteDatabase database;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS LOGIN (id INTEGER PRIMARY KEY, Name VARCHAR, Password VARCHAR, Type VARCHAR)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameEditText = findViewById(R.id.Signup_Username);
        passwordEditText = findViewById(R.id.Signup_Password);
        typeSpinner = findViewById(R.id.Signup_RoleSpinner);
        signupButton = findViewById(R.id.Signup_signupButton);
        backButton = findViewById(R.id.Signup_backButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"User", "Admin"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        database = openOrCreateDatabase("USER_DB", MODE_PRIVATE, null);
        database.execSQL(CREATE_USER_TABLE);
        setupListeners();
    }

    private void setupListeners() {
        signupButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String type = typeSpinner.getSelectedItem().toString();
            if (!name.isEmpty() && !password.isEmpty()) addUser(generateRandomId(), name, password, type);
            else Toast.makeText(Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        });
        backButton.setOnClickListener(v -> finish());
    }

    private void addUser(int id, String name, String password, String type) {
        ContentValues values = new ContentValues();
        values.put("id", id); values.put("Name", name);
        values.put("Password", password); values.put("Type", type);
        try {
            long result = database.insert("LOGIN", null, values);
            if (result != -1) {
                Toast.makeText(Signup.this, "Signup successful! Your ID: " + id, Toast.LENGTH_SHORT).show();
                nameEditText.setText(""); passwordEditText.setText("");
            } else Toast.makeText(Signup.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Signup.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private int generateRandomId() {
        return new Random().nextInt(900000) + 100000;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (database != null) database.close();
    }
}
