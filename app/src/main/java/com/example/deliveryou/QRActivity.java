package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class QRActivity extends AppCompatActivity
{

    private CodeScanner mCodeScanner;

    Activity phone;
    Activity activity;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        activity = (Activity) getIntent().getSerializableExtra("activity");
        type = getIntent().getStringExtra("type");

        mCodeScanner.setDecodeCallback(new DecodeCallback()
        {
            @Override
            public void onDecoded(@NonNull final Result result)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        Utils.showToast(QRActivity.this, "QR scanned successfully for the location - " + result.getText());
                        if (phone != null)
                        {
                            sendMessage();
                        } else
                        {
                            if(type != null)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(QRActivity.this);
                                builder.setMessage("Sent from your Twilio trial account - Your delivery request has been completed from DeliveryYou." +
                                        "\nThank you for choosing us!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.dismiss();
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("delivery_requests")
                                                .child(activity.getId())
                                                .setValue(null);
                                        Intent intent = new Intent(QRActivity.this, Driver_Options.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                builder.show();

                            }


                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause()
    {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    void sendMessage()
    {
        final String ACCOUNT_SID = "ACba03b6827dfd682d16b549d0fafd9252";
        final String AUTH_TOKEN = "de2d0dc0fe42fbad7c25ebe4766ba6d3";

        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send a text message
        String fromPhoneNumber = "+12542766632";
        String toPhoneNumber = phone.getRequestedBy().getPhone_no();
        String messageBody = "Sent from your Twilio trial account - Your delivery request has been completed from DeliveryYou." +
                "\nThank you for choosing us!";

        String msgToShow = "";

        try
        {
            Message message = Message.creator(new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody).create();

            msgToShow = message.toString();
        } catch (Exception e)
        {
            msgToShow = messageBody;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(QRActivity.this);
        builder.setMessage(msgToShow);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
                finish();
            }
        });

        builder.show();


    }
}