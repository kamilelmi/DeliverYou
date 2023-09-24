package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.deliveryou.databinding.ActivitySpecificCarpoolBinding;
import com.example.deliveryou.models.Activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Specific_Carpool extends AppCompatActivity
{

    Button start_proc;
    ActivitySpecificCarpoolBinding binding;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_specific_carpool);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        activity = (Activity) getIntent().getSerializableExtra("activity");

        binding.customerName.setText(activity.getRequestedBy().getUsername());
        binding.tvPhone.setText(activity.getRequestedBy().getPhone_no());


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


            binding.pickupLoc.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.pickupLoc.setText("Location not found");
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

            binding.destinationLoc.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.destinationLoc.setText("Location not found");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(activity.getTimestamp());
        binding.pickTime.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));


        if(activity.getRequestedBy().getImg_url() != null)
        {
            Glide.with(this).load(activity.getRequestedBy().getImg_url())
                    .into(binding.customerPic);

        }
        binding.SeeMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String pickupLatitude = activity.getPickup_loc().split(",")[0];
                String pickupLongitude = activity.getPickup_loc().split(",")[1];
                String dropoffLatitude = activity.getDestination_loc().split(",")[0];
                String dropoffLongitude = activity.getDestination_loc().split(",")[1];

                String uri = "https://www.google.com/maps/dir/?api=1&origin=" + pickupLatitude + "," + pickupLongitude +
                        "&destination=" + dropoffLatitude + "," + dropoffLongitude;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        double pickupLatitude = Double.parseDouble(activity.getPickup_loc().split(",")[0]);
        double pickupLongitude = Double.parseDouble(activity.getPickup_loc().split(",")[1]);
        double dropoffLatitude = Double.parseDouble(activity.getDestination_loc().split(",")[0]);
        double dropoffLongitude = Double.parseDouble(activity.getDestination_loc().split(",")[1]);

        binding.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", calculateDistance(pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude)) + " Km");


        start_proc = findViewById(R.id.Start_process);


        start_proc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Specific_Carpool.this, Carpool_Chat.class);
                i.putExtra("activity", activity);
                startActivity(i);
            }
        });
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