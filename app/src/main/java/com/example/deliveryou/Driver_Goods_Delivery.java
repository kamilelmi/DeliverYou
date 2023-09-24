package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.deliveryou.adapters.CarpoolAdapter;
import com.example.deliveryou.databinding.ActivityDriverGoodsDeliveryBinding;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.models.User;
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
import java.util.Objects;

import okhttp3.internal.Util;

public class Driver_Goods_Delivery extends AppCompatActivity {

    ArrayList<Activity> deliveryList;
    ActivityDriverGoodsDeliveryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_goods_delivery);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        loadActivities();

    }


    private void loadActivities()
    {
        Utils.showProgressDialog(Driver_Goods_Delivery.this, "Getting carpool requests\nPlease wait");
        deliveryList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("delivery_requests")
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
                                activity.setActivityType("Delivery");

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
                                    deliveryList.add(activity);
                                }
                                else
                                {
                                    if(activity.getAssigned_driver() != null)
                                    {
                                        if(Objects.equals(activity.getAssigned_driver().getId(), Constants.sessionUser.getId()))
                                        {
                                            activity.setRideToStart(true);
                                            deliveryList.add(activity);
                                        }
                                    }
                                }
                            }
                        }


                        binding.lvDelivery.setAdapter(new CarpoolAdapter(Driver_Goods_Delivery.this, deliveryList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }
}