package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Confirm_delivery extends AppCompatActivity {


    ImageView qr1, qr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        qr1 = findViewById(R.id.qr1);
        qr2 = findViewById(R.id.qr2);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(getIntent().getStringExtra("p"),
                null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(getResources().getColor(R.color.theme));
        qrgEncoder.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();
        // Setting Bitmap to ImageView
        qr1.setImageBitmap(bitmap);


        QRGEncoder qrgEncoder1 = new QRGEncoder(getIntent().getStringExtra("d"),
                null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder1.setColorBlack(getResources().getColor(R.color.theme));
        qrgEncoder1.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        Bitmap bitmap2 = qrgEncoder1.getBitmap();
        // Setting Bitmap to ImageView
        qr2.setImageBitmap(bitmap2);

        findViewById(R.id.go_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Confirm_delivery.this,Customer_Delivery_Option.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                Confirm_delivery.this.finish();
            }
        });
    }
}