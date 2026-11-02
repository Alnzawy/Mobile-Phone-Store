package com.example.finalproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManageUsersActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private TableLayout usersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        db = openOrCreateDatabase("USER_DB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN (id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, Loginid VARCHAR, Password VARCHAR, Type VARCHAR)");

        usersTable = findViewById(R.id.users_table);
        Button add = findViewById(R.id.add_user_button);
        Button update = findViewById(R.id.update_user_button);
        Button delete = findViewById(R.id.delete_user_button);
        Button back = findViewById(R.id.Back_user_button);
        loadUsers();

        add.setOnClickListener(v -> showUserDialog("Add", -1));
        update.setOnClickListener(v -> {
            int id = getSelectedUserId();
            if (id != -1) showUserDialog("Update", id);
            else Toast.makeText(this, "Select a user to update", Toast.LENGTH_SHORT).show();
        });
        delete.setOnClickListener(v -> {
            int id = getSelectedUserId();
            if (id != -1) deleteUser(id);
            else Toast.makeText(this, "Select a user to delete", Toast.LENGTH_SHORT).show();
        });
        back.setOnClickListener(v -> {
            startActivity(new Intent(ManageUsersActivity.this, AdminPage.class));
            finish();
        });
    }

    private void loadUsers() {
        usersTable.removeAllViews();
        Cursor cursor = db.rawQuery("SELECT id, Name, Type FROM LOGIN", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            TableRow row = new TableRow(this);
            row.setPadding(16, 24, 16, 24);
            row.setClickable(true);
            row.addView(createStyledTextView(String.valueOf(id)));
            row.addView(createStyledTextView(cursor.getString(1)));
            row.addView(createStyledTextView(cursor.getString(2)));
            row.setOnClickListener(v -> {
                for (int i = 0; i < usersTable.getChildCount(); i++)
                    usersTable.getChildAt(i).setBackgroundColor(0x00000000);
                row.setBackgroundColor(0xFFEEEEEE);
                row.setTag(id);
            });
            usersTable.addView(row);
        }
        cursor.close();
    }

    private TextView createStyledTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text); tv.setPadding(24, 12, 24, 12);
        tv.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tv.setTextSize(16); tv.setTextColor(0xFF333333);
        return tv;
    }

    private void showUserDialog(String action, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(action + " User");
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user, null);
        builder.setView(dialogView);
        EditText username = dialogView.findViewById(R.id.dialog_user_username);
        EditText password = dialogView.findViewById(R.id.dialog_user_password);
        EditText role = dialogView.findViewById(R.id.dialog_user_role);

        if ("Update".equals(action) && userId != -1) {
            Cursor c = db.rawQuery("SELECT Name, Password, Type FROM LOGIN WHERE id=?",
                    new String[]{String.valueOf(userId)});
            if (c.moveToFirst()) {
                username.setText(c.getString(0)); password.setText(c.getString(1)); role.setText(c.getString(2));
            }
            c.close();
            role.setEnabled(true);
        } else {
            role.setText("User"); role.setEnabled(false);
        }

        builder.setPositiveButton(action, (dialog, which) -> {
            String n = username.getText().toString().trim();
            String p = password.getText().toString().trim();
            if (!n.isEmpty() && !p.isEmpty()) {
                if ("Add".equals(action)) addUser(n, p, "User");
                else updateUser(userId, n, p, role.getText().toString());
            } else Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addUser(String username, String password, String role) {
        ContentValues values = new ContentValues();
        values.put("Name", username); values.put("Password", password); values.put("Type", role);
        db.insert("LOGIN", null, values);
        loadUsers(); Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
    }

    private void updateUser(int id, String username, String password, String role) {
        ContentValues values = new ContentValues();
        values.put("Name", username); values.put("Password", password); values.put("Type", role);
        db.update("LOGIN", values, "id=?", new String[]{String.valueOf(id)});
        loadUsers(); Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteUser(int id) {
        db.delete("LOGIN", "id=?", new String[]{String.valueOf(id)});
        loadUsers(); Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
    }

    private int getSelectedUserId() {
        for (int i = 0; i < usersTable.getChildCount(); i++) {
            View row = usersTable.getChildAt(i);
            if (row.getTag() != null) return (int) row.getTag();
        }
        return -1;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
