package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity
{

    CardView support;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);
        support = findViewById(R.id.help_Support);
        support.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Settings.this, Support.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.ll_messages).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.this, MyMsgsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.this, Customer_Delivery_Option.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_activities).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.this, MyActivitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.faqs).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showFaqAlert();
            }
        });

        findViewById(R.id.changepassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shoeChangePasswordALert();
            }
        });


        findViewById(R.id.legal).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAlert("Legal", "Legal text will be here");
            }
        });

        findViewById(R.id.about_us).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAlert("About us", "About us text will be here\nYou can contact on +123-456-789");
            }
        });

        findViewById(R.id.deleteaccount_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDeleteAlert("Delete account", "Are you sure that you want to delete the account?");
            }
        });

        findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Utils.setPref(Settings.this, "user", "");
                Intent intent = new Intent(Settings.this, Splash_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void shoeChangePasswordALert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

        View viewInflated = LayoutInflater.from(Settings.this).inflate(R.layout.passwordchange
                , (ViewGroup) getWindow().getDecorView().getRootView()
                , false);

        final EditText newPassword = viewInflated.findViewById(R.id.newpassword);
        final EditText confirmPassword = viewInflated.findViewById(R.id.confirmpassword);

        builder.setView(viewInflated);


        builder.setPositiveButton("CHANGE PASSWORD", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (newPassword.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    Utils.showProgressDialog(Settings.this, "Changing password\nPlease wait");
                    String uid = Utils.getPref(Settings.this, "uid", "");
                    FirebaseFirestore.getInstance().collection("deliveryou_users").document(uid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                {
                                    if (task.isSuccessful())
                                    {

                                        String username = task.getResult().getString("username");
                                        String phone_no = task.getResult().getString("phone_no");
                                        String dob = task.getResult().getString("dob");
                                        String password = newPassword.getText().toString();
                                        Boolean is_customer = task.getResult().getBoolean("is_customer");
                                        String img_url = task.getResult().getString("img_url");


                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", username);
                                        user.put("phone_no", phone_no);
                                        user.put("dob", dob);
                                        user.put("password", password);
                                        user.put("is_customer", is_customer);
                                        user.put("img_url", img_url);
                                        if (!is_customer)
                                        {
                                            user.put("is_approved", true);
                                        }

                                        FirebaseFirestore.getInstance().collection("deliveryou_users").document(uid)
                                                .delete();
                                        FirebaseFirestore.getInstance().collection("deliveryou_users").document(username + "#" + password)
                                                .set(user);
                                        Utils.dismissProgressDialog();
                                        Utils.setPref(Settings.this, "uid", username + "#" + password);
                                        dialog.cancel();
                                        Utils.showToast(Settings.this, "Password changed successfully");

                                    }
                                }
                            });

                } else
                {
                    Utils.showToast(Settings.this, "Password must be same");
                }


            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void showFaqAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);


        View viewInflated = LayoutInflater.from(Settings.this).inflate(R.layout.faq
                , (ViewGroup) getWindow().getDecorView().getRootView()
                , false);

//        final CheckBox incomesCb = viewInflated.findViewById(R.id.dialog_accfilter_cb_income);


        builder.setView(viewInflated);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showAlert(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Settings.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    public void showDeleteAlert(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Settings.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Utils.showProgressDialog(Settings.this, "Deleting Account\nPlease Wait");

                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Utils.dismissProgressDialog();
                                        if (task.isSuccessful())
                                        {
                                            Intent intent = new Intent(Settings.this, Splash_Screen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                            Toast.makeText(Settings.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                                        } else
                                        {
                                            Toast.makeText(Settings.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}