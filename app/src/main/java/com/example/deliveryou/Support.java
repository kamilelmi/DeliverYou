package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.deliveryou.databinding.ActivitySupportBinding;
import com.example.deliveryou.databinding.ActivitySupportChatBinding;
import com.example.deliveryou.utils.Constants;

public class Support extends AppCompatActivity {
    TextView support_chat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        support_chat = findViewById(R.id.support_chat);
        support_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Support.this, Support_Chat.class);
                intent.putExtra("user", Constants.sessionUser);
                startActivity(intent);
            }
        });
    }
}