package com.example.deliveryou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.deliveryou.adapters.DriverChatAdapter;
import com.example.deliveryou.databinding.ActivityMyMsgsBinding;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.models.Chat;
import com.example.deliveryou.models.Message;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MyMsgsActivity extends AppCompatActivity {


    ActivityMyMsgsBinding binding;
    List<Chat> chats = new ArrayList<>();
    Activity activity;
    DriverChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_msgs);

        if(getIntent().hasExtra("activity"))
        {

            activity = (Activity) getIntent().getExtras().getSerializable("activity");


            Utils.showProgressDialog(MyMsgsActivity.this, "Getting chats\nPlease wait");

            binding.lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    Intent intent = new Intent(MyMsgsActivity.this, Carpool_Chat.class);
                    intent.putExtra("activity", activity);
                    intent.putExtra("driver", chats.get(i).getDriver_details());
                    startActivity(intent);
                }
            });


            loadChats();
        }
        else
        {
            binding.tvSelectActivity.setVisibility(View.VISIBLE);
            binding.clRoot.setVisibility(View.GONE);
        }

        findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyMsgsActivity.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyMsgsActivity.this, MyMsgsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyMsgsActivity.this, Customer_Delivery_Option.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.ll_activities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyMsgsActivity.this, MyActivitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private void loadChats()
    {
        FirebaseDatabase.getInstance().getReference()
                .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                .child(activity.getId())
                .child("chat")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        chats = new ArrayList<>();
                        Utils.dismissProgressDialog();

                        for(DataSnapshot chatSnap: snapshot.getChildren())
                        {
                            Chat chat = new Chat();
                            chat.setDriver_details(chatSnap.child("driver_details").getValue(User.class));
                            List<Message> messages = new ArrayList<>();

                            for(DataSnapshot msgSnap: chatSnap.child("messages").getChildren())
                            {
                                messages.add(msgSnap.getValue(Message.class));
                            }

                            chat.setMessages(messages);

                            chats.add(chat);
                        }

                        if(adapter == null)
                        {
                            adapter = new DriverChatAdapter(MyMsgsActivity.this, chats);
                        }
                        else
                        {
                            adapter.clear();
                            adapter.addAll(chats);
                        }


                        binding.lvChats.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(MyMsgsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}