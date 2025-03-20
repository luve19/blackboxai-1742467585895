package com.vertigrowth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class TrackOrderActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    
    private GoogleMap mMap;
    private TextView errorTextView;
    private TextView deliveryStatusTextView;
    private TextView estimatedTimeTextView;
    private ProgressBar deliveryProgressBar;
    private MaterialButton proceedButton;

    // Dummy delivery location (you would get this from your backend in a real app)
    private final LatLng DELIVERY_LOCATION = new LatLng(37.7749, -122.4194); // San Francisco coordinates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        // Initialize UI components
        errorTextView = findViewById(R.id.errorTextView);
        deliveryStatusTextView = findViewById(R.id.deliveryStatusTextView);
        estimatedTimeTextView = findViewById(R.id.estimatedTimeTextView);
        deliveryProgressBar = findViewById(R.id.deliveryProgressBar);
        proceedButton = findViewById(R.id.proceedButton);

        try {
            // Initialize map fragment
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapFragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            } else {
                showError("Error initializing map");
            }

            // Set up proceed button
            proceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrackOrderActivity.this, ThankYouActivity.class));
                }
            });

            // Update delivery status (in a real app, this would be dynamic)
            updateDeliveryStatus("On the way", 50);
            estimatedTimeTextView.setText("Estimated delivery: 30 mins");

        } catch (Exception e) {
            showError("Error initializing tracking: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Check and request location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                setupMap();
            }
        } catch (Exception e) {
            showError("Error setting up map: " + e.getMessage());
        }
    }

    private void setupMap() {
        try {
            if (mMap != null) {
                // Enable my location button if permission is granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }

                // Add delivery location marker
                mMap.addMarker(new MarkerOptions()
                        .position(DELIVERY_LOCATION)
                        .title("Delivery Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                // Move camera to delivery location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DELIVERY_LOCATION, 15f));
            }
        } catch (Exception e) {
            showError("Error configuring map: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                showError("Location permission denied. Some features may be limited.");
            }
        }
    }

    private void updateDeliveryStatus(String status, int progress) {
        try {
            deliveryStatusTextView.setText(status);
            deliveryProgressBar.setProgress(progress);
        } catch (Exception e) {
            showError("Error updating delivery status: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}