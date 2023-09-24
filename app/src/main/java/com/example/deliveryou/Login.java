package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Login extends AppCompatActivity
{

    Button sign_in;
    TextView sign_up;
    EditText emailEt, passwordEt;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xFFFFFFFF);
        window.setNavigationBarColor(0xFFFFFFFF);

        emailEt = findViewById(R.id.Email_signin);
        passwordEt = findViewById(R.id.password_signin);

        sign_in = findViewById(R.id.SIGN_IN);
        sign_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (getIntent().getBooleanExtra("is_admin", true))
                {
                    if (emailEt.getText().toString().equals("admin") && passwordEt.getText().toString().equals("admin"))
                    {
                        Intent i = new Intent(Login.this, AdminActivity.class);
                        Constants.sessionUser = new User();
                        Constants.sessionUser.setId("admin");
                        Constants.sessionUser.setUsername("admin");
                        startActivity(i);
                    } else
                    {
                        Utils.showToast(Login.this, "Wrong credentials");
                    }
                } else
                {
                    Utils.showProgressDialog(Login.this, "Signing in\nPlease wait");

                    if (Utils.isEmpty(emailEt))
                    {
                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isEmpty(passwordEt))
                    {
                        Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEt.getText().toString().trim(), passwordEt.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                            {
                                @Override
                                public void onSuccess(AuthResult authResult)
                                {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users")
                                            .child(authResult.getUser().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener()
                                            {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                                {
                                                    try
                                                    {

                                                        Utils.dismissProgressDialog();
                                                        Constants.sessionUser = snapshot.getValue(User.class);
                                                        Constants.sessionUser.setId(authResult.getUser().getUid());


                                                        if(Constants.sessionUser.getIsNumberVerified())
                                                        {
                                                            if (Constants.sessionUser.getIs_customer())
                                                            {
                                                                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(Login.this, Customer_Delivery_Option.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                Utils.setPref(Login.this, "user", new Gson().toJson(Constants.sessionUser));
                                                                startActivity(i);
                                                                finish();
                                                            } else
                                                            {
                                                                if(Constants.sessionUser.getLicense_no() != null)
                                                                {
                                                                    if(Constants.sessionUser.getIs_approved() == null)
                                                                    {
                                                                        Constants.sessionUser.setIs_approved(snapshot.child("is_approved").getValue(Boolean.class));
                                                                    }
                                                                    if (Constants.sessionUser.getIs_approved())
                                                                    {
                                                                        Utils.setPref(Login.this, "user", new Gson().toJson(Constants.sessionUser));
                                                                        Intent intent = new Intent(Login.this, Driver_Options.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else
                                                                    {
                                                                        FirebaseAuth.getInstance().signOut();
                                                                        Utils.showToast(Login.this, "Please wait for the approval from the admin");
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    Intent intent = new Intent(Login.this, Signup_Driver2.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                }

                                                            }
                                                        }
                                                        else
                                                        {
                                                            Intent i = new Intent(Login.this, Phone_verification.class);
                                                            i.putExtra("phone_no", Constants.sessionUser.getPhone_no());
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                    catch(Exception e)
                                                    {
                                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error)
                                                {
                                                    Toast.makeText(Login.this, "Error while logging in!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Utils.dismissProgressDialog();
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
        sign_up = findViewById(R.id.signup_text);
        sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();

            }
        });
    }
}