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

public class ManageProductsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private TableLayout productsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        db = openOrCreateDatabase("USER_DB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCTS (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, quantity INTEGER, price REAL)");

        productsTable = findViewById(R.id.products_table);
        Button addButton = findViewById(R.id.add_product_button);
        Button updateButton = findViewById(R.id.update_product_button);
        Button deleteButton = findViewById(R.id.delete_product_button);
        Button backButton = findViewById(R.id.Back_product_button);

        loadProducts();
        addButton.setOnClickListener(v -> showProductDialog("Add", -1));
        updateButton.setOnClickListener(v -> {
            int id = getSelectedProductId();
            if (id != -1) showProductDialog("Update", id);
            else Toast.makeText(this, "Select a product to update", Toast.LENGTH_SHORT).show();
        });
        deleteButton.setOnClickListener(v -> {
            int id = getSelectedProductId();
            if (id != -1) deleteProduct(id);
            else Toast.makeText(this, "Select a product to delete", Toast.LENGTH_SHORT).show();
        });
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ManageProductsActivity.this, AdminPage.class));
            finish();
        });
    }

    private void loadProducts() {
        productsTable.removeAllViews();
        Cursor cursor = db.rawQuery("SELECT id, name, quantity, price FROM PRODUCTS", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            TableRow row = new TableRow(this);
            row.setPadding(16, 24, 16, 24);
            row.setClickable(true);
            row.addView(createStyledTextView(String.valueOf(id), false));
            row.addView(createStyledTextView(cursor.getString(1), false));
            row.addView(createStyledTextView(String.valueOf(cursor.getInt(2)), false));
            row.addView(createStyledTextView(String.valueOf(cursor.getDouble(3)), false));
            row.setOnClickListener(v -> {
                for (int i = 0; i < productsTable.getChildCount(); i++)
                    productsTable.getChildAt(i).setBackgroundColor(0x00000000);
                row.setBackgroundColor(0xFFEEEEEE);
                row.setTag(id);
            });
            productsTable.addView(row);
        }
        cursor.close();
    }

    private TextView createStyledTextView(String text, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(24, 12, 24, 12);
        tv.setGravity(isHeader ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_TEXT_START);
        tv.setTextSize(isHeader ? 18 : 16);
        tv.setTextColor(isHeader ? 0xFF000000 : 0xFF333333);
        return tv;
    }

    private void showProductDialog(String action, int productId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(action + " Product");
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_product, null);
        builder.setView(dialogView);
        EditText name = dialogView.findViewById(R.id.dialog_product_name);
        EditText quantity = dialogView.findViewById(R.id.dialog_product_quantity);
        EditText price = dialogView.findViewById(R.id.dialog_product_price);

        if ("Update".equals(action) && productId != -1) {
            Cursor c = db.rawQuery("SELECT name, quantity, price FROM PRODUCTS WHERE id=?",
                    new String[]{String.valueOf(productId)});
            if (c.moveToFirst()) {
                name.setText(c.getString(0));
                quantity.setText(String.valueOf(c.getInt(1)));
                price.setText(String.valueOf(c.getDouble(2)));
            }
            c.close();
        }

        builder.setPositiveButton(action, (dialog, which) -> {
            String n = name.getText().toString().trim();
            String q = quantity.getText().toString().trim();
            String p = price.getText().toString().trim();
            if (!n.isEmpty() && !q.isEmpty() && !p.isEmpty()) {
                try {
                    if ("Add".equals(action)) addProduct(n, Integer.parseInt(q), Double.parseDouble(p));
                    else updateProduct(productId, n, Integer.parseInt(q), Double.parseDouble(p));
                } catch (NumberFormatException ex) {
                    Toast.makeText(this, "Enter a valid quantity and price", Toast.LENGTH_SHORT).show();
                }
            } else Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void addProduct(String name, int quantity, double price) {
        ContentValues values = new ContentValues();
        values.put("name", name); values.put("quantity", quantity); values.put("price", price);
        db.insert("PRODUCTS", null, values);
        loadProducts();
        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
    }

    private void updateProduct(int id, String name, int quantity, double price) {
        ContentValues values = new ContentValues();
        values.put("name", name); values.put("quantity", quantity); values.put("price", price);
        db.update("PRODUCTS", values, "id=?", new String[]{String.valueOf(id)});
        loadProducts();
        Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteProduct(int id) {
        db.delete("PRODUCTS", "id=?", new String[]{String.valueOf(id)});
        loadProducts();
        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
    }

    private int getSelectedProductId() {
        for (int i = 0; i < productsTable.getChildCount(); i++) {
            View row = productsTable.getChildAt(i);
            if (row.getTag() != null) return (int) row.getTag();
        }
        return -1;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
