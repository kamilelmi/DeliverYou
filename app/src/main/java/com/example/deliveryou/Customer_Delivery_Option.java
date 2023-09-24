package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.deliveryou.databinding.ActivityCustomerDeliveryOptionBinding;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.StripeHelper;

public class Customer_Delivery_Option extends AppCompatActivity {

    ActivityCustomerDeliveryOptionBinding binding;
    RadioButton carpoolradio,deliveryradio;
    TextView continue_btn;
    ImageView profile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_delivery_option);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        binding.username.setText(Constants.sessionUser.getUsername());

        continue_btn = findViewById(R.id.contnue_btn);
        carpoolradio = findViewById(R.id.Carpool);
        deliveryradio = findViewById(R.id.Small_Goods);
        profile = findViewById(R.id.profile_pic);
        carpoolradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!carpoolradio.isSelected()) {
                    carpoolradio.setChecked(true);
                    carpoolradio.setSelected(true);
                    deliveryradio.setChecked(false);
                    deliveryradio.setSelected(false);
                } else {
                    carpoolradio.setChecked(false);
                    carpoolradio.setSelected(false);
                }
            }
        });
        deliveryradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deliveryradio.isSelected()) {
                    carpoolradio.setChecked(false);
                    carpoolradio.setSelected(false);
                    deliveryradio.setChecked(true);
                    deliveryradio.setSelected(true);
                } else {
                    deliveryradio.setChecked(false);
                    deliveryradio.setSelected(false);
                }
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carpoolradio.isSelected())
                {
                    Intent i = new Intent(Customer_Delivery_Option.this,Customer_date_time.class);
                    startActivity(i);
                }
                else if (deliveryradio.isSelected())
                {
                    Intent i = new Intent(Customer_Delivery_Option.this,Customer_Small_Goods.class);
                    i.putExtra("customer_name", getIntent().getStringExtra("customer_name"));
                    i.putExtra("img_url", getIntent().getStringExtra("img_url"));
                    startActivity(i);
                }

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Customer_Delivery_Option.this,Profile.class);
                startActivity(i);
            }
        });

        new StripeHelper().getStripeCustomerId(this);

        findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Delivery_Option.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Delivery_Option.this, MyMsgsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Delivery_Option.this, Customer_Delivery_Option.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_activities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_Delivery_Option.this, MyActivitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        findViewById(R.id.check_msgs).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Customer_Delivery_Option.this,MyMsgsActivity.class);
//                i.putExtra("customer_name", getIntent().getStringExtra("customer_name"));
//                i.putExtra("img_url", getIntent().getStringExtra("img_url"));
//                startActivity(i);
//            }
//        });
    }
}