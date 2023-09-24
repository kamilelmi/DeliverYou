package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Signup_customer extends AppCompatActivity
{

    RadioButton male_radio, female_radio;
    EditText DOB, usernameEt, numberEt, passwordEt, confirmPasswordEt, etEmail;
    Button signup;

    ImageView profileIv;
    Uri profileImg = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_customer);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        profileIv = findViewById(R.id.profiledp);

        profileIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickImage(11);
            }
        });
        usernameEt = findViewById(R.id.name);
        etEmail = findViewById(R.id.etEmail);
        numberEt = findViewById(R.id.phone_number);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.cnfrmPass);
        signup = findViewById(R.id.Sign_up_data);
        male_radio = findViewById(R.id.Male);
        female_radio = findViewById(R.id.Female);
        DOB = findViewById(R.id.dob);
        male_radio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!male_radio.isSelected())
                {
                    male_radio.setChecked(true);
                    male_radio.setSelected(true);
                    female_radio.setChecked(false);
                    female_radio.setSelected(false);
                } else
                {
                    male_radio.setChecked(false);
                    male_radio.setSelected(false);
                }
            }
        });
        female_radio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!female_radio.isSelected())
                {
                    male_radio.setChecked(false);
                    male_radio.setSelected(false);
                    female_radio.setChecked(true);
                    female_radio.setSelected(true);
                } else
                {
                    female_radio.setChecked(false);
                    female_radio.setSelected(false);
                }
            }
        });
        DOB.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if (hasFocus)
                {
                    openDatePickerDialog(view);
                }
            }
        });
        DOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDatePickerDialog(view);
            }
        });
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Utils.isEmpty(usernameEt) || Utils.isEmpty(etEmail) || Utils.isEmpty(numberEt)
                        || Utils.isEmpty(DOB) || Utils.isEmpty(passwordEt) || Utils.isEmpty(confirmPasswordEt))
                {
                    Utils.showToast(Signup_customer.this, "Some fields are empty");
                } else
                {
                    if (passwordEt.getText().toString().equals(confirmPasswordEt.getText().toString()))
                    {
                        Utils.showProgressDialog(Signup_customer.this, "Signing up\nPlease wait");


                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString().trim(), passwordEt.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                                {
                                    @Override
                                    public void onSuccess(AuthResult authResult)
                                    {
                                        if (profileImg != null)
                                        {
                                            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("deliveryou_images");

                                            final StorageReference profileImgRef = ImageFolder.child("Images" + System.currentTimeMillis());


                                            profileImgRef.putFile(profileImg).addOnSuccessListener(
                                                    new OnSuccessListener<UploadTask.TaskSnapshot>()
                                                    {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                        {
                                                            profileImgRef.getDownloadUrl().addOnSuccessListener(
                                                                    new OnSuccessListener<Uri>()
                                                                    {
                                                                        @Override
                                                                        public void onSuccess(Uri uri)
                                                                        {


                                                                            boolean isCustomer = getIntent().getBooleanExtra("is_customer", true);

                                                                            Map<String, Object> user = new HashMap<>();
                                                                            user.put("username", usernameEt.getText().toString());
                                                                            user.put("phone_no", numberEt.getText().toString());
                                                                            user.put("dob", DOB.getText().toString());
                                                                            user.put("password", passwordEt.getText().toString());
                                                                            user.put("is_customer", isCustomer);
                                                                            user.put("img_url", uri.toString());
                                                                            user.put("isNumberVerified", false);

                                                                            User userObj = new User();

                                                                            userObj.setUsername(user.get("username").toString());
                                                                            userObj.setPhone_no(user.get("username").toString());
                                                                            userObj.setIs_customer(Boolean.parseBoolean(user.get("username").toString()));
                                                                            userObj.setDob(user.get("username").toString());
                                                                            userObj.setPassword(user.get("username").toString());
                                                                            userObj.setImg_url(user.get("username").toString());
                                                                            userObj.setIsNumberVerified(Boolean.parseBoolean(user.get("username").toString()));


                                                                            if (!isCustomer)
                                                                            {
                                                                                user.put("is_approved", false);
                                                                                userObj.setIs_approved(false);
                                                                            }



                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("users")
                                                                                    .child(authResult.getUser().getUid())
                                                                                    .setValue(user)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                                                    {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {

                                                                                            Utils.dismissProgressDialog();
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                Intent i = new Intent(Signup_customer.this, Phone_verification.class);
                                                                                                if (!isCustomer)
                                                                                                {
                                                                                                    Constants.sessionUser = userObj;
                                                                                                    i.putExtra("phone_no", numberEt.getText().toString());
                                                                                                } else
                                                                                                {
                                                                                                    i.putExtra("phone_no", numberEt.getText().toString());
                                                                                                }
                                                                                                startActivity(i);
                                                                                            } else
                                                                                            {
                                                                                                Utils.showToast(Signup_customer.this, task.getException().getLocalizedMessage());

                                                                                            }
                                                                                        }
                                                                                    });

                                                                        }
                                                                    }
                                                            );
                                                        }
                                                    }
                                            );
                                        } else
                                        {

                                            boolean isCustomer = getIntent().getBooleanExtra("is_customer", true);

                                            Map<String, Object> user = new HashMap<>();
                                            user.put("username", usernameEt.getText().toString());
                                            user.put("phone_no", numberEt.getText().toString());
                                            user.put("dob", DOB.getText().toString());
                                            user.put("password", passwordEt.getText().toString());
                                            user.put("is_customer", isCustomer);
                                            user.put("isNumberVerified", false);

                                            User userObj = new User();

                                            userObj.setUsername(user.get("username").toString());
                                            userObj.setPhone_no(user.get("phone_no").toString());
                                            userObj.setIs_customer(Boolean.parseBoolean(user.get("is_customer").toString()));
                                            userObj.setDob(user.get("dob").toString());
                                            userObj.setPassword(user.get("password").toString());
                                            userObj.setIsNumberVerified(Boolean.parseBoolean(user.get("isNumberVerified").toString()));


                                            if (!isCustomer)
                                            {
                                                user.put("is_approved", false);
                                                userObj.setIs_approved(false);
                                            }

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("users")
                                                    .child(authResult.getUser().getUid())
                                                    .setValue(user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {

                                                            Utils.dismissProgressDialog();
                                                            if (task.isSuccessful())
                                                            {
                                                                Intent i = new Intent(Signup_customer.this, Phone_verification.class);
                                                                i.putExtra("phone_no", numberEt.getText().toString());
                                                                startActivity(i);
                                                            } else
                                                            {
                                                                Utils.showToast(Signup_customer.this, task.getException().getLocalizedMessage());

                                                            }
                                                        }
                                                    });
                                        }
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Utils.dismissProgressDialog();
                                        Toast.makeText(Signup_customer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else
                    {
                        confirmPasswordEt.setError("Password must be same");
                    }
                }


            }
        });
    }

    public void pickImage(int code)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 11)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                profileImg = data.getData();
                profileIv.setImageURI(profileImg);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openDatePickerDialog(final View v)
    {

        Calendar cal = Calendar.getInstance();
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    switch (v.getId())
                    {
                        case R.id.dob:
                            ((EditText) v).setText(selectedDate);
                            break;
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }
}