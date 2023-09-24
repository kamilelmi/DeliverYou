package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliveryou.adapters.CarpoolMessagingAdapter;
import com.example.deliveryou.databinding.ActivitySupportChatBinding;
import com.example.deliveryou.models.Message;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Support_Chat extends AppCompatActivity {

    TextView end;

    User chatUser;

    ArrayList<Message> messages;

    CarpoolMessagingAdapter adapter;

    ActivitySupportChatBinding binding;

    DatabaseReference chatRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_chat);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);

        chatUser = (User) getIntent().getExtras().getSerializable("user");

        chatRef = FirebaseDatabase.getInstance().getReference()
                .child("support")
                .child(chatUser.getId())
                .child("messages");


        adapter = new CarpoolMessagingAdapter(this, null);
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessages.setHasFixedSize(true);
        binding.rvMessages.setAdapter(adapter);


        binding.supportSendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Utils.isEmpty(binding.etSupport))
                {
                    Toast.makeText(Support_Chat.this, "Please type message", Toast.LENGTH_SHORT).show();
                    return;
                }

                String key = chatRef.push().getKey();

                Message message = new Message();
                message.setId(key);
                message.setText(binding.etSupport.getText().toString().trim());
                message.setType("text");
                message.setTimestamp(Calendar.getInstance().getTimeInMillis());
                message.setSender(Constants.sessionUser);

                assert key != null;
                chatRef.child(key).setValue(message);

                if(Constants.sessionUser != null)
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("support")
                            .child(Constants.sessionUser.getId())
                            .child("user_details")
                            .setValue(Constants.sessionUser);
                }



                binding.etSupport.setText("");
            }
        });


        loadChatData();

        end = findViewById(R.id.end_chat);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Support_Chat.this, Customer_Delivery_Option.class);
                startActivity(intent);
            }
        });
    }

    private void loadChatData()
    {
        Utils.showProgressDialog(this, "Loading Messages\nPlease Wait");

        FirebaseDatabase.getInstance().getReference()
                .child("support")
                .child(chatUser.getId())
                .child("messages")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Utils.dismissProgressDialog();
                        messages = new ArrayList<>();


                        if (snapshot.exists())
                        {
                            for (DataSnapshot messageSnap : snapshot.getChildren())
                            {
                                Message message = messageSnap.getValue(Message.class);
                                message.setId(messageSnap.getKey());
                                messages.add(message);
                            }

                            adapter.setMessages(messages);
                            adapter.notifyDataSetChanged();
                            binding.rvMessages.scrollToPosition(messages.size() - 1);

                        } else
                        {
                            Toast.makeText(Support_Chat.this, "No Messages", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }

}