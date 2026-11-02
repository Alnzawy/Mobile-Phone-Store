package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        findViewById(R.id.manage_users_card).setOnClickListener(v ->
                startActivity(new Intent(AdminPage.this, ManageUsersActivity.class)));

        findViewById(R.id.manage_products_card).setOnClickListener(v ->
                startActivity(new Intent(AdminPage.this, ManageProductsActivity.class)));

        findViewById(R.id.logout_button).setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, MainActivity.class));
            finish();
        });
    }
}
