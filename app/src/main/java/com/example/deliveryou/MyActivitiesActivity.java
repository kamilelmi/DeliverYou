package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deliveryou.adapters.ActivityAdapter;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivitiesActivity extends AppCompatActivity
{

    private ListView actLv;

    private ArrayList<Activity> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);


        actLv = findViewById(R.id.activities_lv_act);

        loadActivities();



        actLv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(activities.get(i).isActive())
                {
                    Intent intent = new Intent(MyActivitiesActivity.this, MyMsgsActivity.class);
                    intent.putExtra("activity", activities.get(i));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MyActivitiesActivity.this, "Activity is completed already!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyActivitiesActivity.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.ll_messages).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyActivitiesActivity.this, MyMsgsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyActivitiesActivity.this, Customer_Delivery_Option.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_activities).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyActivitiesActivity.this, MyActivitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadActivities()
    {
        Utils.showProgressDialog(MyActivitiesActivity.this, "Getting your activities\nPlease wait");
        activities = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Constants.sessionUser.getId())
                .child("my_carpool_requests")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot activitySnap: snapshot.getChildren())
                            {
                                Log.d("MLK", activitySnap.getValue().toString());
                                Activity activity = activitySnap.getValue(Activity.class);
                                activity.setId(activitySnap.getKey());
                                activity.setActivityType("Carpool");
                                if(activitySnap.child("isActive").exists())
                                {
                                    activity.setActive(activitySnap.child("isActive").getValue(Boolean.class));
                                }
                                else
                                {
                                    activity.setActive(true);
                                }
                                Log.d("MLK", "SA: " + activity.isActive());
                                activities.add(activity);
                            }
                        }


                        loadDeliveryRequests();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }

    private void loadDeliveryRequests()
    {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Constants.sessionUser.getId())
                .child("delivery_requests")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot activitySnap: snapshot.getChildren())
                            {
                                Activity activity = activitySnap.getValue(Activity.class);
                                activity.setId(activitySnap.getKey());
                                activity.setActivityType("Delivery");
                                if(activitySnap.child("isActive").exists())
                                {
                                    activity.setActive(activitySnap.child("isActive").getValue(Boolean.class));
                                }
                                else
                                {
                                    activity.setActive(true);
                                }
                                activities.add(activity);
                            }
                        }


                        Utils.dismissProgressDialog();

                        ActivityAdapter adapter = new ActivityAdapter(MyActivitiesActivity.this, activities);
                        actLv.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }
}