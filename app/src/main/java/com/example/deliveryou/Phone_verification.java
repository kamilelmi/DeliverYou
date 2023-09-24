package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.deliveryou.api.StripeAPI;
import com.example.deliveryou.models.VerificationCodeRequest;
import com.example.deliveryou.models.VerifyRequest;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Phone_verification extends AppCompatActivity {

    Button verify;

    private FirebaseAuth mAuth;
    private PinView pinView;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        getSupportActionBar().hide();



        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        mAuth = FirebaseAuth.getInstance();

        pinView = findViewById(R.id.firstPinView);

        phoneNumber = getIntent().getStringExtra("phone_no");

        sendVerificationCode(phoneNumber);

        verify = findViewById(R.id.verify_code);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(pinView.getText().toString());
            }
        });
    }

    private void sendVerificationCode(String number) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TWILIO_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("MLK", "Number: " + number);

        String to = number;  // The phone number to send the verification code to
        String channel = "sms";  // The channel to use (e.g., sms, call, email)

        VerifyRequest request = new VerifyRequest(to, channel);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("To", request.getTo())
                .addFormDataPart("Channel", request.getChannel())
                .build();

        Call<ResponseBody> call = retrofit.create(StripeAPI.class)
                .createVerificationRequest(requestBody);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if(response.isSuccessful())
                {

                    try
                    {
                        Log.d("MLK", response.body().string());
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(Phone_verification.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        Log.d("MLK", response.errorBody().string());
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(Phone_verification.this, "Some error occurred while sending otp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {

            }
        });

    }

    private void verifyCode(String code) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TWILIO_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VerificationCodeRequest request = new VerificationCodeRequest(phoneNumber, code);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("To", request.getTo())
                .addFormDataPart("Code", request.getCode())
                .build();

        Call<ResponseBody> call = retrofit.create(StripeAPI.class)
                .verifyCode(requestBody);

        Utils.showProgressDialog(this,"Verifying Code\nPlease Wait");

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                Utils.dismissProgressDialog();
                if(response.isSuccessful())
                {

                    try
                    {
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("isNumberVerified")
                                .setValue(true);

                        if (Constants.sessionUser.getIs_customer())
                        {
                            Intent i = new Intent(Phone_verification.this, Customer_Delivery_Option.class);
                            startActivity(i);
                            finish();
                        } else
                        {
                            if (Constants.sessionUser.getIs_approved())
                            {
                                Intent intent = new Intent(Phone_verification.this, Driver_Options.class);
                                startActivity(intent);
                                finish();
                            } else
                            {
                                Utils.showToast(Phone_verification.this, "Please wait for the approval from the admin");
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(Phone_verification.this, Signup_Driver2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                        Log.d("MLK", response.body().string());
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }

                }
                else
                {
                    try
                    {
                        Log.d("MLK", response.errorBody().string());
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(Phone_verification.this, "Some error occurred while sending otp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {

            }
        });

    }
}