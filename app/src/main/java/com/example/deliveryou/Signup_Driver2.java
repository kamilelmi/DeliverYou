package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Signup_Driver2 extends AppCompatActivity
{

    private Uri carImg, licenseImg = null;

    String[] LicenseType = {"Easy", "Medium", "Difficult"};
    Button submit_btn, addCarBtn, addLicenseBtn;
    ImageView carIv, licenseIv;
    EditText carModelEt, numberPlateEt, licenseNoEt;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_driver2);
        getSupportActionBar().hide();

        carModelEt = findViewById(R.id.car_model);
        numberPlateEt = findViewById(R.id.vehicle_number);
        licenseNoEt = findViewById(R.id.license_number);

        carIv = findViewById(R.id.driver2_iv_car);
        licenseIv = findViewById(R.id.driver2_iv_license);

        addCarBtn = findViewById(R.id.add_car_btn);
        addLicenseBtn = findViewById(R.id.add_licence_btn);


        addCarBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickImage(11);
            }
        });

        addLicenseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickImage(22);
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        submit_btn = findViewById(R.id.car_info_submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (carImg == null)
                {
                    Toast.makeText(Signup_Driver2.this, "Please add car image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (licenseImg == null)
                {
                    Toast.makeText(Signup_Driver2.this, "Please add license image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Utils.isEmpty(carModelEt))
                {
                    Toast.makeText(Signup_Driver2.this, "Please enter car model", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Utils.isEmpty(numberPlateEt))
                {
                    Toast.makeText(Signup_Driver2.this, "Please enter number plate", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Utils.isEmpty(licenseNoEt))
                {
                    Toast.makeText(Signup_Driver2.this, "Please enter license number", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.showProgressDialog(Signup_Driver2.this, "Adding info\nPlease wait");
                Map<String, Object> driverInfo = new HashMap<>();


                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("deliveryou_images");

                final StorageReference carImageRef = ImageFolder.child("Images" + carImg.getLastPathSegment());
                final StorageReference licenseImgRef = ImageFolder.child("Images" + carImg.getLastPathSegment());

                carImageRef.putFile(carImg).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                carImageRef.getDownloadUrl().addOnSuccessListener(
                                        new OnSuccessListener<Uri>()
                                        {
                                            @Override
                                            public void onSuccess(Uri uri)
                                            {
                                                driverInfo.put("car_url", uri.toString());

                                                licenseImgRef.putFile(licenseImg).addOnSuccessListener(
                                                        new OnSuccessListener<UploadTask.TaskSnapshot>()
                                                        {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                            {
                                                                licenseImgRef.getDownloadUrl().addOnSuccessListener(
                                                                        new OnSuccessListener<Uri>()
                                                                        {
                                                                            @Override
                                                                            public void onSuccess(Uri uri)
                                                                            {

                                                                                driverInfo.put("license_url", uri.toString());
                                                                                driverInfo.put("car_model", carModelEt.getText().toString());
                                                                                driverInfo.put("number_plate", numberPlateEt.getText().toString());
                                                                                driverInfo.put("license_no", licenseNoEt.getText().toString());

                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("users")
                                                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                        .updateChildren(driverInfo)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>()
                                                                                        {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused)
                                                                                            {
                                                                                                Utils.dismissProgressDialog();
                                                                                                Utils.showToast(Signup_Driver2.this, "Info added successfully");
                                                                                                Intent i = new Intent(Signup_Driver2.this, Verify_lisence.class);
                                                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                startActivity(i);
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener()
                                                                                        {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e)
                                                                                            {
                                                                                                Utils.dismissProgressDialog();
                                                                                                Utils.showToast(Signup_Driver2.this, e.getLocalizedMessage());
                                                                                            }
                                                                                        });

                                                                            }
                                                                        }
                                                                );
                                                            }
                                                        }
                                                );


                                            }
                                        }
                                );
                            }
                        }
                );


            }
        });


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void pickImage(int code)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ALL)
        {

            pickImage(11);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 11)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                carImg = data.getData();
                carIv.setImageURI(carImg);
            }
        } else if (requestCode == 22)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                licenseImg = data.getData();
                licenseIv.setImageURI(licenseImg);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}