package com.vertigrowth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ThankYouActivity extends AppCompatActivity {
    private TextView orderNumberTextView;
    private MaterialButton returnHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        try {
            // Initialize UI components
            orderNumberTextView = findViewById(R.id.orderNumberTextView);
            returnHomeButton = findViewById(R.id.returnHomeButton);

            // Generate a random order number (in a real app, this would come from the backend)
            String orderNumber = "VG" + String.format("%06d", (int) (Math.random() * 1000000));
            orderNumberTextView.setText("Order #: " + orderNumber);

            // Set up return home button
            returnHomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Create an intent to return to HomeActivity
                        Intent intent = new Intent(ThankYouActivity.this, HomeActivity.class);
                        // Clear the activity stack so the user can't go back to the order flow
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Close this activity
                    } catch (Exception e) {
                        showError("Error returning to home: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            showError("Error initializing thank you page: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // Override back button to ensure users go to home screen
        try {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            showError("Error handling back press: " + e.getMessage());
            super.onBackPressed();
        }
    }
}