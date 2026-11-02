package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signupText;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.Main_Username);
        passwordEditText = findViewById(R.id.Main_Password);
        loginButton = findViewById(R.id.Main_loginButton);
        signupText = findViewById(R.id.signup_text);

        db = openOrCreateDatabase("USER_DB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN (id INTEGER PRIMARY KEY, Name VARCHAR, Password VARCHAR, Type VARCHAR)");

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }
            authenticateUser(username, password);
        });

        signupText.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, Signup.class)));
    }

    private void authenticateUser(String username, String password) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT Type FROM LOGIN WHERE Name=? AND Password=?",
                    new String[]{username, password});
            if (cursor.moveToFirst()) {
                String userType = cursor.getString(0);
                if ("User".equalsIgnoreCase(userType)) {
                    startActivity(new Intent(MainActivity.this, UserPage.class));
                    Toast.makeText(this, "Welcome, User!", Toast.LENGTH_SHORT).show();
                } else if ("Admin".equalsIgnoreCase(userType)) {
                    startActivity(new Intent(MainActivity.this, AdminPage.class));
                    Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while authenticating", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
