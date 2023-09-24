package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.my_map_api));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    try
                    {
                        User user = new Gson().fromJson(Utils.getPref(Splash_Screen.this, "user", ""), User.class);
                        user.setId(FirebaseAuth.getInstance().getUid());

                        Constants.sessionUser = user;
                        Intent intent;


                        if(user.getIs_customer())
                        {
                            intent = new Intent(Splash_Screen.this, Customer_Delivery_Option.class);
                        }
                        else
                        {
                            if(user.getLicense_no() == null)
                            {
                                FirebaseAuth.getInstance().signOut();
                                Utils.setPref(Splash_Screen.this, "user", "");
                                intent = new Intent(Splash_Screen.this, Sign_Up_activity.class);
                            }
                            else
                            {
                                intent = new Intent(Splash_Screen.this, Driver_Options.class);
                            }
                        }
                        startActivity(intent);
                        finish();


                    }
                    catch(Exception e)
                    {
                        Intent intent = new Intent(Splash_Screen.this, Sign_Up_activity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                {
                    Intent intent = new Intent(Splash_Screen.this, Sign_Up_activity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000); // 5000 milliseconds = 5 seconds


    }
}