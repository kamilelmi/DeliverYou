package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deliveryou.adapters.DriverChatAdapter;
import com.example.deliveryou.databinding.ActivityAdminChatsBinding;
import com.example.deliveryou.models.Chat;
import com.example.deliveryou.models.Message;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Utils;
import com.fasterxml.jackson.databind.DatabindContext;
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
import java.util.List;

public class AdminChatsActivity extends AppCompatActivity
{

    ActivityAdminChatsBinding binding;
    List<Chat> chats = new ArrayList<>();
    DriverChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_chats);


        Utils.showProgressDialog(AdminChatsActivity.this, "Getting chats\nPlease wait");

        binding.lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(AdminChatsActivity.this, Support_Chat.class);
                intent.putExtra("user", chats.get(i).getUser_details());
                startActivity(intent);
            }
        });


        loadChats();
    }

    private void loadChats()
    {
        FirebaseDatabase.getInstance().getReference()
                .child("support")
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
                            chat.setUser_details(chatSnap.child("user_details").getValue(User.class));
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
                            adapter = new DriverChatAdapter(AdminChatsActivity.this, chats);
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
                        Toast.makeText(AdminChatsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}