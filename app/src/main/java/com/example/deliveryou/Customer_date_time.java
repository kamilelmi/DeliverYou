package com.example.deliveryou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.adevinta.leku.LocationPickerActivity;
import com.example.deliveryou.databinding.ActivityCustomerDateTimeBinding;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.databinding.DataBindingUtil;
import okhttp3.internal.Util;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

public class Customer_date_time extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    ActivityCustomerDateTimeBinding binding;
    TextView textView;
    Button button, cont_btn, pickUpBtn, dropOffBtn;
    int day, month, year, hour, minute;

    String pickUpAddress = "";
    String dropOffAddress = "";
    String formattedTime = "";

    String textString = "";

    Calendar selectedDate;
    double pickUpLat = 0, pickUpLng = 0, dropOffLat = 0, dropOffLng = 0;

    ActivityResultLauncher<Intent> pickUpLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
    {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onActivityResult(ActivityResult result)
        {
            Intent data = result.getData();

            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
            {

                pickUpLat = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d("LATITUDE****", String.valueOf(pickUpLat));
                pickUpLng = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d("LONGITUDE****", String.valueOf(pickUpLng));
                String address = data.getStringExtra("location_address");
                Log.d("ADDRESS***", address);

                if(address.isEmpty())
                {

                }
                pickUpAddress = address;


                binding.tvPickup.setVisibility(View.VISIBLE);
                binding.tvPickup.setText(("Pickup: " + pickUpAddress));

            }
            if (result.getResultCode() == Activity.RESULT_CANCELED)
            {
                Log.d("RESULT****", "CANCELLED");
            }
        }
    });

    ActivityResultLauncher<Intent> dropOffLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
    {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onActivityResult(ActivityResult result)
        {
            Intent data = result.getData();

            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
            {

                dropOffLat = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d("LATITUDE****", String.valueOf(dropOffLat));
                dropOffLng = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d("LONGITUDE****", String.valueOf(dropOffLng));
                String address = data.getStringExtra("location_address");
                Log.d("ADDRESS***", address);
                dropOffAddress = address;

                binding.tvDropoff.setVisibility(View.VISIBLE);
                binding.tvDropoff.setText("Drop off: " + dropOffAddress);

            }
            if (result.getResultCode() == Activity.RESULT_CANCELED)
            {
                Log.d("RESULT****", "CANCELLED");
            }
        }
    });

    private void loadLocationIntent(double lat, double lng, ActivityResultLauncher<Intent> launcher)
    {

        Intent activity = new LocationPickerActivity.Builder()
                .withGeolocApiKey(getString(R.string.GEO_API_KEY))
                .withGooglePlacesApiKey(getString(R.string.GEO_API_KEY))
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withLocation(lat, lng)
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(getApplicationContext());

        launcher.launch(activity);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_date_time);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        textView = findViewById(R.id.textView);
        button = findViewById(R.id.btnPick);
        cont_btn = findViewById(R.id.btn_continue);


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Customer_date_time.this, Customer_date_time.this, year, month, day);
                datePickerDialog.show();
            }
        });

        cont_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(pickUpLng == 0 || pickUpLat == 0)
                {
                    Toast.makeText(Customer_date_time.this, "Please select pick up location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dropOffLng == 0 || dropOffLat == 0)
                {
                    Toast.makeText(Customer_date_time.this, "Please select drop off location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedDate == null)
                {
                    Toast.makeText(Customer_date_time.this, "Please select date and time for pick up", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.showProgressDialog(Customer_date_time.this, "Adding carpool request\nPlease wait");

                Map<String, Object> carpoolRequest = new HashMap<>();
                carpoolRequest.put("timestamp", selectedDate.getTimeInMillis());
                carpoolRequest.put("pickup_loc", pickUpLat + "," + pickUpLng);
                carpoolRequest.put("destination_loc", dropOffLat + "," + dropOffLng);
                carpoolRequest.put("requestedBy", Constants.sessionUser);
                carpoolRequest.put("isActive", true);

                String key = FirebaseDatabase.getInstance().getReference()
                        .child("carpool_requests")
                        .push().getKey();

                carpoolRequest.put("id", key);


                FirebaseDatabase.getInstance().getReference()
                        .child("carpool_requests")
                        .child(Objects.requireNonNull(key))
                        .setValue(carpoolRequest)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                Utils.dismissProgressDialog();

                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("my_carpool_requests")
                                        .child(Objects.requireNonNull(key))
                                        .setValue(carpoolRequest);

                                Intent i = new Intent(Customer_date_time.this, Waiting_driver.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Utils.dismissProgressDialog();
                                Utils.showToast(Customer_date_time.this, e.getLocalizedMessage());
                            }
                        });


            }
        });

        findViewById(R.id.btnPickupLocation).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadLocationIntent(pickUpLat, pickUpLng, pickUpLauncher);
            }
        });


        findViewById(R.id.btnDropOffLocation).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadLocationIntent(dropOffLat, dropOffLng, dropOffLauncher);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.YEAR, year);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate.set(Calendar.MONTH, month);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(Customer_date_time.this, Customer_date_time.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {

        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.set(Calendar.MINUTE, minute);

        formattedTime = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(selectedDate.getTime());


        binding.tvTime.setVisibility(View.VISIBLE);
        binding.tvTime.setText("Your pickup time: " + formattedTime);

    }
}