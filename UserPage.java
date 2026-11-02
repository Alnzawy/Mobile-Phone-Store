package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class UserPage extends AppCompatActivity {
    private SQLiteDatabase db;
    private TableLayout productsTable;
    private List<Product> cartItems;
    private int selectedProductId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        db = openOrCreateDatabase("USER_DB", MODE_PRIVATE, null);
        productsTable = findViewById(R.id.products_table);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        Button checkoutButton = findViewById(R.id.checkout_button);
        cartItems = new ArrayList<>();

        loadProducts();
        addToCartButton.setOnClickListener(v -> {
            if (selectedProductId != -1) showQuantityDialog(selectedProductId);
            else Toast.makeText(this, "Please select a product first", Toast.LENGTH_SHORT).show();
        });
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(UserPage.this, CheckoutPage.class);
                intent.putParcelableArrayListExtra("cart_items", new ArrayList<>(cartItems));
                startActivity(intent);
            }
        });
        findViewById(R.id.logout_button).setOnClickListener(v -> {
            startActivity(new Intent(UserPage.this, MainActivity.class));
            finish();
        });
    }

    private void loadProducts() {
        productsTable.removeAllViews();
        TableRow header = new TableRow(this);
        header.setPadding(16, 24, 16, 24);
        header.addView(createStyledTextView("Name", true));
        header.addView(createStyledTextView("Available", true));
        header.addView(createStyledTextView("Price", true));
        productsTable.addView(header);

        Cursor cursor = db.rawQuery("SELECT id, name, quantity, price FROM PRODUCTS WHERE quantity > 0", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            double price = cursor.getDouble(3);
            TableRow row = new TableRow(this);
            row.setPadding(16, 24, 16, 24);
            row.addView(createStyledTextView(name, false));
            row.addView(createStyledTextView(String.valueOf(quantity), false));
            row.addView(createStyledTextView("$" + price, false));
            row.setClickable(true);
            row.setOnClickListener(v -> {
                for (int i = 1; i < productsTable.getChildCount(); i++)
                    productsTable.getChildAt(i).setBackgroundColor(0x00000000);
                row.setBackgroundColor(0xFFE0E0E0);
                selectedProductId = id;
            });
            row.setTag(new Product(id, name, price, quantity));
            productsTable.addView(row);
        }
        cursor.close();
    }

    private TextView createStyledTextView(String text, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text); tv.setPadding(16, 8, 16, 8);
        tv.setTextSize(isHeader ? 18 : 16);
        tv.setTextColor(isHeader ? 0xFF000000 : 0xFF333333);
        return tv;
    }

    private void showQuantityDialog(int productId) {
        Cursor cursor = db.rawQuery("SELECT name, quantity, price FROM PRODUCTS WHERE id=?",
                new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            int availableQuantity = cursor.getInt(1);
            double price = cursor.getDouble(2);
            Product product = new Product(productId, name, price, availableQuantity);

            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Enter Quantity");
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_quantity, null);
            builder.setView(dialogView);
            EditText quantityEditText = dialogView.findViewById(R.id.dialog_quantity);
            builder.setPositiveButton("Add to Cart", (dialog, which) -> {
                String input = quantityEditText.getText().toString();
                if (!input.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(input);
                        if (quantity > 0 && quantity <= product.getQuantity()) {
                            ContentValues values = new ContentValues();
                            values.put("quantity", product.getQuantity() - quantity);
                            db.update("PRODUCTS", values, "id=?", new String[]{String.valueOf(productId)});
                            cartItems.add(new Product(productId, name, price, quantity));
                            Toast.makeText(this, name + " added to cart", Toast.LENGTH_SHORT).show();
                            loadProducts();
                            selectedProductId = -1;
                        } else Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        } else Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
