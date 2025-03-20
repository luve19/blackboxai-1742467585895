package com.vertigrowth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmationActivity extends AppCompatActivity {
    private TextView statusTextView;
    private TextView errorTextView;
    private TextView totalPriceTextView;
    private LinearLayout orderItemsContainer;
    private MaterialButton trackOrderButton;
    private Handler handler;

    // Dummy order items for demonstration
    private static class OrderItem {
        String name;
        int quantity;
        double price;

        OrderItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize UI components
        statusTextView = findViewById(R.id.statusTextView);
        errorTextView = findViewById(R.id.errorTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        orderItemsContainer = findViewById(R.id.orderItemsContainer);
        trackOrderButton = findViewById(R.id.trackOrderButton);
        handler = new Handler();

        try {
            // Set initial status
            statusTextView.setText("Being Processed");
            
            // Load and display order details
            loadOrderDetails();

            // Simulate status update after 3 seconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateOrderStatus("Successfully Processed");
                }
            }, 3000);

            // Set up track order button
            trackOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderConfirmationActivity.this, TrackOrderActivity.class));
                }
            });

        } catch (Exception e) {
            showError("Error loading order details: " + e.getMessage());
        }
    }

    private void loadOrderDetails() {
        // Create dummy order items
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("Hydroponic Nutrients", 2, 29.99));
        orderItems.add(new OrderItem("pH Meter", 1, 49.99));
        orderItems.add(new OrderItem("Growing Medium", 3, 19.99));

        // Calculate total price
        double totalPrice = 0;

        // Inflate and add order items to container
        LayoutInflater inflater = LayoutInflater.from(this);
        for (OrderItem item : orderItems) {
            View itemView = inflater.inflate(R.layout.order_item_layout, orderItemsContainer, false);
            
            TextView nameTextView = itemView.findViewById(R.id.itemNameTextView);
            TextView quantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
            TextView priceTextView = itemView.findViewById(R.id.itemPriceTextView);

            nameTextView.setText(item.name);
            quantityTextView.setText("Quantity: " + item.quantity);
            priceTextView.setText(String.format("$%.2f", item.price * item.quantity));

            orderItemsContainer.addView(itemView);
            
            totalPrice += item.price * item.quantity;
        }

        // Update total price
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void updateOrderStatus(String status) {
        try {
            statusTextView.setText(status);
            if (status.equals("Successfully Processed")) {
                statusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                trackOrderButton.setEnabled(true);
            }
        } catch (Exception e) {
            showError("Error updating status: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}