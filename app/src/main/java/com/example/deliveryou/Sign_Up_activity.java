package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Sign_Up_activity extends AppCompatActivity {

    RadioButton userradio,driverradio;
    TextView login_btn, adminBtn;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        adminBtn = findViewById(R.id.admin_btn);
        signup = findViewById(R.id.sign_up_btn);
        login_btn = findViewById(R.id.login_btn);
        userradio = findViewById(R.id.Customer);
        driverradio = findViewById(R.id.Driver);
        userradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userradio.isSelected()) {
                    userradio.setChecked(true);
                    userradio.setSelected(true);
                    driverradio.setChecked(false);
                    driverradio.setSelected(false);
                } else {
                    userradio.setChecked(false);
                    userradio.setSelected(false);
                }
            }
        });

        driverradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!driverradio.isSelected()) {
                    userradio.setChecked(false);
                    userradio.setSelected(false);
                    driverradio.setChecked(true);
                    driverradio.setSelected(true);
                } else {
                    driverradio.setChecked(false);
                    driverradio.setSelected(false);
                }
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sign_Up_activity.this,Login.class);
                i.putExtra("is_admin", true);
                startActivity(i);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sign_Up_activity.this,Login.class);
                i.putExtra("is_admin", false);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userradio.isSelected())
                {
                    Intent i = new Intent(Sign_Up_activity.this,Signup_customer.class);
                    i.putExtra("is_customer", true);
                    startActivity(i);
                    finish();
                }
                else if (driverradio.isSelected())
                {
                    Intent i = new Intent(Sign_Up_activity.this,Signup_customer.class);
                    i.putExtra("is_customer", false);
                    startActivity(i);
                }

            }
        });
    }
}