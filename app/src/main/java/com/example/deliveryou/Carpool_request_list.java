package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deliveryou.adapters.CarpoolAdapter;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.utils.Constants;
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

public class Carpool_request_list extends AppCompatActivity {

    ListView carpool_rv;
    ArrayList<Activity> carpool_list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_request_list);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        carpool_rv = findViewById(R.id.carpool_request);

        loadActivities();

    }


    private void loadActivities()
    {
        Utils.showProgressDialog(Carpool_request_list.this, "Getting carpool requests\nPlease wait");
        carpool_list = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("carpool_requests")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Utils.dismissProgressDialog();

                        if(snapshot.exists())
                        {
                            for(DataSnapshot activitySnap: snapshot.getChildren())
                            {
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

                                if(activity.isActive())
                                {
                                    carpool_list.add(activity);
                                }
                            }
                        }


                        carpool_rv.setAdapter(new CarpoolAdapter(Carpool_request_list.this, carpool_list));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }
}