package com.example.deliveryou;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
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

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

public class Customer_Small_Goods extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    EditText pickup_time;
    Spinner item_type;
    Button delivery_submit;

    Calendar selectedTime;

    int day, month, year, hour, minute;

    EditText sizeEt, weightEt, lengthEt, widthEt, heightEt, pickupLocEt, destinationLocEt, instructionsEt;

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
                pickupLocEt.setText(address);

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
                destinationLocEt.setText(address);

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
        setContentView(R.layout.activity_customer_small_goods);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        sizeEt = findViewById(R.id.size);
        widthEt = findViewById(R.id.width);
        weightEt = findViewById(R.id.weight);
        lengthEt = findViewById(R.id.length);
        heightEt = findViewById(R.id.height);
        pickup_time = findViewById(R.id.pickup_time);
        pickupLocEt = findViewById(R.id.pickup_location);
        destinationLocEt = findViewById(R.id.Destination_Location);
        instructionsEt = findViewById(R.id.any_instruction);


        delivery_submit = findViewById(R.id.confirm_deleviry);
        pickup_time = findViewById(R.id.pickup_time);

        pickup_time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Customer_Small_Goods.this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute)
                            {
                                selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                pickup_time.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.getTime()));
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        pickupLocEt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadLocationIntent(pickUpLat, pickUpLng, pickUpLauncher);
            }
        });

        destinationLocEt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadLocationIntent(dropOffLat, dropOffLng, dropOffLauncher);
            }
        });

        item_type = findViewById(R.id.item_type);

        delivery_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (Utils.isEmpty(weightEt))
                {
                    Toast.makeText(Customer_Small_Goods.this, "Please enter weight of the item", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(weightEt.getText().toString()) > 25)
                {
                    Utils.showToast(Customer_Small_Goods.this, "Maximum weight capacity is 25kg");
                } else
                {

                    if (Utils.isEmpty(sizeEt))
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please enter item size", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isEmpty(weightEt))
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please enter item size", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (selectedTime == null)
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please select time", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pickUpLat == 0 || pickUpLng == 0)
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please select pick up location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dropOffLat == 0 || dropOffLng == 0)
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please select drop off location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isEmpty(lengthEt))
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please enter length of parcel", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isEmpty(widthEt))
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please enter width of parcel", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isEmpty(heightEt))
                    {
                        Toast.makeText(Customer_Small_Goods.this, "Please enter height of parcel", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Utils.showProgressDialog(Customer_Small_Goods.this, "Adding request\nPlease wait");

                    Map<String, Object> deliveryRequest = new HashMap<>();

                    deliveryRequest.put("type", item_type.getSelectedItem().toString());
                    deliveryRequest.put("size", sizeEt.getText().toString());
                    deliveryRequest.put("weight", weightEt.getText().toString());
                    deliveryRequest.put("length", lengthEt.getText().toString());
                    deliveryRequest.put("width", widthEt.getText().toString());
                    deliveryRequest.put("height", heightEt.getText().toString());
                    deliveryRequest.put("pickup_time", selectedTime.getTimeInMillis());
                    deliveryRequest.put("pickup_loc", pickUpLat + "," + pickUpLng);
                    deliveryRequest.put("destination_loc", dropOffLat + "," + dropOffLng);
                    deliveryRequest.put("instructions", instructionsEt.getText().toString().trim());
                    deliveryRequest.put("requestedBy", Constants.sessionUser);

                    String key = FirebaseDatabase.getInstance().getReference()
                            .child("delivery_requests")
                            .push().getKey();


                    FirebaseDatabase.getInstance().getReference()
                            .child("delivery_requests")
                            .child(key)
                            .setValue(deliveryRequest)
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("delivery_requests")
                                            .child(key)
                                            .setValue(deliveryRequest);

                                    Utils.dismissProgressDialog();
                                    Intent i = new Intent(Customer_Small_Goods.this, Confirm_delivery.class);
                                    i.putExtra("p", "https://maps.google.com/?q=" + pickUpLat + "," + pickUpLng);
                                    i.putExtra("d", "https://maps.google.com/?q=" + dropOffLat + "," + dropOffLng);
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Utils.dismissProgressDialog();
                                    Utils.showToast(Customer_Small_Goods.this, e.getLocalizedMessage());
                                }
                            });


                }


            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}