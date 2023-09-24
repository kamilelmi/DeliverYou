package com.example.deliveryou;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Calendar;

public class Signup_Driver1 extends AppCompatActivity {

    RadioButton male_radio,female_radio;
    EditText DOB;
    Button signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_driver1);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        signup = findViewById(R.id.Sign_up_data_driver);
        male_radio = findViewById(R.id.Male_driver);
        female_radio = findViewById(R.id.Female_driver);
        DOB = findViewById(R.id.dob_driver);
        male_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!male_radio.isSelected()) {
                    male_radio.setChecked(true);
                    male_radio.setSelected(true);
                    female_radio.setChecked(false);
                    female_radio.setSelected(false);
                } else {
                    male_radio.setChecked(false);
                    male_radio.setSelected(false);
                }
            }
        });
        female_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!female_radio.isSelected()) {
                    male_radio.setChecked(false);
                    male_radio.setSelected(false);
                    female_radio.setChecked(true);
                    female_radio.setSelected(true);
                } else {
                    female_radio.setChecked(false);
                    female_radio.setSelected(false);
                }
            }
        });
        DOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    openDatePickerDialog(view);
                }
            }
        });
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(view);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup_Driver1.this,Phone_verification.class);
                i.putExtra("ID",2);
                startActivity(i);
            }
        });
    }


    public void openDatePickerDialog(final View v) {

        Calendar cal = Calendar.getInstance();
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    switch (v.getId()) {
                        case R.id.dob:
                            ((EditText)v).setText(selectedDate);
                            break;
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }
}