package com.example.finalproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CheckoutPage extends AppCompatActivity {
    private TableLayout cartTable;
    private ArrayList<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        cartItems = getIntent().getParcelableArrayListExtra("cart_items");
        if (cartItems == null) cartItems = new ArrayList<>();

        cartTable = findViewById(R.id.cart_table);
        Button printBillButton = findViewById(R.id.print_bill_button);
        Button backToCartButton = findViewById(R.id.back_to_cart_button);

        loadCartItems();
        printBillButton.setOnClickListener(v -> printBill());
        backToCartButton.setOnClickListener(v -> finish());
    }

    private void loadCartItems() {
        cartTable.removeAllViews();
        TableRow headerRow = new TableRow(this);
        headerRow.setPadding(16, 16, 16, 16);
        headerRow.addView(createStyledTextView("Name", true));
        headerRow.addView(createStyledTextView("Quantity", true));
        headerRow.addView(createStyledTextView("Price", true));
        headerRow.addView(createStyledTextView("Total", true));
        cartTable.addView(headerRow);

        double grandTotal = 0;
        for (Product product : cartItems) {
            TableRow row = new TableRow(this);
            row.setPadding(16, 16, 16, 16);
            row.addView(createStyledTextView(product.getName(), false));
            row.addView(createStyledTextView(String.valueOf(product.getQuantity()), false));
            row.addView(createStyledTextView("$" + product.getPrice(), false));
            double total = product.getQuantity() * product.getPrice();
            row.addView(createStyledTextView("$" + total, false));
            cartTable.addView(row);
            grandTotal += total;
        }

        TableRow totalRow = new TableRow(this);
        totalRow.setPadding(16, 16, 16, 16);
        totalRow.addView(createStyledTextView("Grand Total", true));
        totalRow.addView(createStyledTextView("", true));
        totalRow.addView(createStyledTextView("", true));
        totalRow.addView(createStyledTextView("$" + grandTotal, true));
        cartTable.addView(totalRow);
    }

    private TextView createStyledTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        textView.setTextSize(isHeader ? 18 : 16);
        textView.setTextColor(isHeader ? 0xFF000000 : 0xFF333333);
        return textView;
    }

    private void printBill() {
        StringBuilder bill = new StringBuilder("BILL SUMMARY\n\n");
        double grandTotal = 0;
        for (Product product : cartItems) {
            double total = product.getQuantity() * product.getPrice();
            bill.append(product.getName()).append(" - ")
                .append(product.getQuantity()).append(" x $")
                .append(product.getPrice()).append(" = $")
                .append(total).append("\n");
            grandTotal += total;
        }
        bill.append("\nGrand Total: $").append(grandTotal);
        Toast.makeText(this, bill.toString(), Toast.LENGTH_LONG).show();
        cartItems.clear();
    }
}
