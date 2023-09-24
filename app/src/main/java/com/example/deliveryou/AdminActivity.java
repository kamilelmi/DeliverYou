package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity
{

    private ListView listView;

    private ArrayList<User> driverInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        findViewById(R.id.admin_btn_supportchat).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdminActivity.this, AdminChatsActivity.class));
            }
        });


        listView = findViewById(R.id.admin_listview);

        Utils.showProgressDialog(AdminActivity.this, "Getting details\nPlease wait");

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        Utils.dismissProgressDialog();

                        if (snapshot.exists())
                        {
                            for (DataSnapshot userSnap : snapshot.getChildren())
                            {
                                try
                                {
                                    User user = userSnap.getValue(User.class);

                                    if(!user.getIs_customer() && !user.getIs_approved())
                                    {
                                        user.setId(userSnap.getKey());
                                        driverInfos.add(user);
                                    }

                                } catch (Exception e)
                                {
                                    Log.d("MLK", e.getMessage());
                                }

                            }

                            DriverInfoAdapter driverInfoAdapter = new DriverInfoAdapter(AdminActivity.this, driverInfos);
                            listView.setAdapter(driverInfoAdapter);

                        }
                        else
                        {
                            Toast.makeText(AdminActivity.this, "No Driver Requests!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });


    }

}