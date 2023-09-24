package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.deliveryou.interfaces.StripeCallback;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.StripeHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

public class Payment_Screens extends AppCompatActivity
{

    CardView visa, master;
    StripeHelper helper;
    String empKey = "";
    String secret = "";
    double transactionAmount;
    Activity activity;
    User driver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screens);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        transactionAmount = getIntent().getExtras().getDouble("price");
        activity = (Activity) getIntent().getExtras().getSerializable("activity");
        driver = (User) getIntent().getExtras().getSerializable("driver");

        visa = findViewById(R.id.visa_payment);
        master = findViewById(R.id.master_payment);

        helper = new StripeHelper();
        helper.initPaymentSheet(this, new StripeResultCallback()
        {
            @Override
            public void onTransactionSuccess()
            {
                Toast.makeText(Payment_Screens.this, "Success! Your carpool is booked.", Toast.LENGTH_SHORT).show();

                FirebaseDatabase.getInstance().getReference()
                        .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                        .child(activity.getId())
                        .child("booking_price")
                        .setValue(transactionAmount);

                FirebaseDatabase.getInstance().getReference()
                        .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                        .child(activity.getId())
                        .child("assigned_driver")
                        .setValue(driver);

                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(activity.getRequestedBy().getId())
                        .child(activity.getActivityType().equals("Carpool") ? "my_carpool_requests" : "delivery_requests")
                        .child(activity.getId())
                        .child("isActive")
                        .setValue(false);

                FirebaseDatabase.getInstance().getReference()
                        .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                        .child(activity.getId())
                        .child("isActive")
                        .setValue(false)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Intent intent = new Intent(Payment_Screens.this, Customer_Delivery_Option.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

            }

            @Override
            public void onTransactionFailed()
            {
                Toast.makeText(Payment_Screens.this, "Couldn't Process your transaction. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        helper.getEphemeralKey(new StripeCallback()
        {
            @Override
            public void onKeyDownloaded(String ephemeralKey)
            {
                empKey = ephemeralKey;
            }
        });


        visa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                proceedToPayment();
            }
        });

        master.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                proceedToPayment();
            }
        });
    }

    private void proceedToPayment()
    {
        helper.runStripe(transactionAmount, new StripeCallback()
        {
            @Override
            public void onKeyDownloaded(String clientSecret)
            {
                secret = clientSecret;
                PaymentConfiguration.init(Payment_Screens.this, Constants.PUBLISHABLE_KEY);

                presentPaymentSheet();
            }
        });
    }

    private void presentPaymentSheet()
    {
        helper.paymentSheet.presentWithPaymentIntent(
                secret,
                new PaymentSheet.Configuration(
                        "Deliver You",
                        new PaymentSheet.CustomerConfiguration(
                                Constants.STRIPE_ID,
                                empKey
                        )
                )
        );
    }

}