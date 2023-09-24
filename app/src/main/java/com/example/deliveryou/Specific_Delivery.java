package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.deliveryou.databinding.ActivitySpecificDeliveryBinding;
import com.example.deliveryou.models.Activity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Specific_Delivery extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    Button start_proc;
    ActivitySpecificDeliveryBinding binding;
    Activity activity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_specific_delivery);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        activity = (Activity) getIntent().getSerializableExtra("activity");

        start_proc = findViewById(R.id.Start_process_driver);
        start_proc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Specific_Delivery.this, Carpool_Chat.class);
                i.putExtra("activity", activity);
                startActivity(i);
            }
        });

        if(activity.isRideToStart())
        {
            start_proc.setVisibility(View.GONE);
            binding.llScanner.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.scan_pickup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCameraPermission())
                    startActivity(new Intent(Specific_Delivery.this, QRActivity.class));
            }
        });

        findViewById(R.id.scan_dest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCameraPermission())
                {
                    activity.getActivityType();
                    Intent intent = new Intent(Specific_Delivery.this, QRActivity.class);
                    intent.putExtra("activity", activity);
                    intent.putExtra("type", "dest");
                    startActivity(intent);
                }
            }
        });



        Glide.with(Specific_Delivery.this).load(activity.getRequestedBy().getImg_url()).placeholder(R.drawable.deliveryou_full_logo)
                .into((ImageView) findViewById(R.id.customer_pic_driver));


        binding.customerNameDriver.setText(activity.getRequestedBy().getUsername() == null ? "" : activity.getRequestedBy().getUsername());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(activity.getPickup_time());
        binding.pickTimeDriver.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));

        binding.Weigth.setText(activity.getWeight());
        binding.Width.setText(activity.getWidth());
        binding.Height.setText(activity.getHeight());
        binding.Dimensions.setText(activity.getWeight());

        double pickupLatitude = Double.parseDouble(activity.getPickup_loc().split(",")[0]);
        double pickupLongitude = Double.parseDouble(activity.getPickup_loc().split(",")[1]);
        double dropoffLatitude = Double.parseDouble(activity.getDestination_loc().split(",")[0]);
        double dropoffLongitude = Double.parseDouble(activity.getDestination_loc().split(",")[1]);


        binding.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", calculateDistance(pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude)) + " Km");


        Geocoder coder = new Geocoder(this);
        try
        {
            List<Address> addressList = coder
                    .getFromLocation(
                            Double.parseDouble(activity.getPickup_loc().split(",")[0]),
                            Double.parseDouble(activity.getPickup_loc().split(",")[1]),
                            1
                    );

            String address = "";


            binding.pickupLocDriver.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.pickupLocDriver.setText("Location not found");
        }

        try
        {
            List<Address> addressList = coder
                    .getFromLocation(
                            Double.parseDouble(activity.getDestination_loc().split(",")[0]),
                            Double.parseDouble(activity.getDestination_loc().split(",")[1]),
                            1
                    );

            String address = "";

            if (addressList.get(0).getThoroughfare() != null)
            {
                address = addressList.get(0).getThoroughfare() + ", ";
            }


            if (addressList.get(0).getSubAdminArea() != null)
            {
                address = address + addressList.get(0).getSubAdminArea();
            }

            binding.destinationLocDriver.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.destinationLocDriver.setText("Location not found");
        }


    }

    private boolean checkCameraPermission()
    {
        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // Request the camera permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            } else
            {
                Toast.makeText(this, "Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371.0;

    public double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double latDistance = Math.toRadians(endLatitude - startLatitude);
        double lonDistance = Math.toRadians(endLongitude - startLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = AVERAGE_RADIUS_OF_EARTH_KM * c;

        return distance;
    }

}