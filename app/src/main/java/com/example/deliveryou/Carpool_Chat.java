package com.example.deliveryou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.deliveryou.adapters.CarpoolMessagingAdapter;
import com.example.deliveryou.databinding.ActivityCarpoolChatBinding;
import com.example.deliveryou.databinding.DialogOfferLayoutBinding;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.models.Chat;
import com.example.deliveryou.models.Message;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Constants;
import com.example.deliveryou.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carpool_Chat extends AppCompatActivity
{
    EditText msgEt;

    RecyclerView chatLv;
    CardView headerCv;

    Activity activity;

    ArrayList<Message> messages;

    CarpoolMessagingAdapter adapter;

    DatabaseReference chatRef;
    User driver;

    ActivityCarpoolChatBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carpool_chat);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(0xFFFFFFFF);
        window.setStatusBarColor(0xFFFFFFFF);


        activity = (Activity) getIntent().getExtras().getSerializable("activity");

        if(Constants.sessionUser.getIs_customer())
        {
            driver = (User) getIntent().getExtras().getSerializable("driver");
        }

        chatLv = findViewById(R.id.chat_lv);
        headerCv = findViewById(R.id.cardView2);
        chatRef = FirebaseDatabase.getInstance().getReference()
                .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                .child(activity.getId())
                .child("chat")
                .child(Constants.sessionUser.getIs_customer() ? driver.getId() : Constants.sessionUser.getId())
                .child("messages");


        binding.AcceptCarpool.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Carpool_Chat.this);
                DialogOfferLayoutBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(Carpool_Chat.this), R.layout.dialog_offer_layout, null, false);
                builder.setView(dialogBinding.getRoot());
                AlertDialog dialog = builder.create();

                dialogBinding.btnSend.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (Utils.isEmpty(dialogBinding.etPrice))
                        {
                            Toast.makeText(Carpool_Chat.this, "Please enter offer price", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double price = Double.parseDouble(dialogBinding.etPrice.getText().toString());

                        String key = chatRef.push().getKey();

                        Message message = new Message();
                        message.setId(key);
                        message.setText("YOU SENT OFFER OF £" + price);
                        message.setType("offer");
                        message.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        message.setSender(Constants.sessionUser);
                        message.setOfferPrice(price);
                        message.setStatus("pending");

                        assert key != null;
                        chatRef.child(key).setValue(message);

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        msgEt = findViewById(R.id.carpool_et_msg);

        adapter = new CarpoolMessagingAdapter(this, activity);
        chatLv.setLayoutManager(new LinearLayoutManager(this));
        chatLv.setHasFixedSize(true);
        chatLv.setAdapter(adapter);


        loadChatData();

        if (!Constants.sessionUser.getIs_customer())
        {
            binding.customerName.setText(activity.getRequestedBy().getUsername());
            binding.AcceptCarpool.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.customerName.setText(driver.getUsername());

            binding.AcceptCarpool.setVisibility(View.GONE);
        }


        findViewById(R.id.support_send_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Utils.isEmpty(msgEt))
                {
                    Toast.makeText(Carpool_Chat.this, "Please type message", Toast.LENGTH_SHORT).show();
                    return;
                }

                String key = chatRef.push().getKey();

                Message message = new Message();
                message.setId(key);
                message.setText(msgEt.getText().toString().trim());
                message.setType("text");
                message.setTimestamp(Calendar.getInstance().getTimeInMillis());
                message.setSender(Constants.sessionUser);

                assert key != null;
                chatRef.child(key).setValue(message);

                if(!Constants.sessionUser.getIs_customer())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                            .child(activity.getId())
                            .child("chat")
                            .child(Constants.sessionUser.getId())
                            .child("driver_details")
                            .setValue(Constants.sessionUser);
                }



                msgEt.setText("");


            }
        });


        // if (getIntent().getBooleanExtra("is_customer", false))
        // {
        //     headerCv.setVisibility(View.GONE);
        //     start_proc.setVisibility(View.GONE);
        //
        //     findViewById(R.id.support_send_btn).setOnClickListener(new View.OnClickListener()
        //     {
        //         @Override
        //         public void onClick(View view)
        //         {
        //             Map<String, Object> msg = new HashMap<>();
        //             msg.put("msg", "C#" + msgEt.getText().toString());
        //             msg.put("customer_name", getIntent().getStringExtra("customer_name"));
        //             msg.put("time", FieldValue.serverTimestamp());
        //             msg.put("post_id", getIntent().getStringExtra("post_id"));
        //             FirebaseFirestore.getInstance().collection("deliveryou_msgs").add(msg);
        //
        //         }
        //     });
        //
        //     getMsgs(false);
        // } else
        {

            // if (getIntent().getStringExtra("post_id").equals("admin"))
            // {
            //     headerCv.setVisibility(View.GONE);
            //     start_proc.setVisibility(View.GONE);
            //     if (getIntent().getBooleanExtra("is_admin", false))
            //     {
            //         findViewById(R.id.support_send_btn).setOnClickListener(new View.OnClickListener()
            //         {
            //             @Override
            //             public void onClick(View view)
            //             {
            //                 Map<String, Object> msg = new HashMap<>();
            //                 msg.put("msg", "A#" + msgEt.getText().toString());
            //                 msg.put("customer_name", getIntent().getStringExtra("customer_name"));
            //                 msg.put("time", FieldValue.serverTimestamp());
            //                 FirebaseFirestore.getInstance().collection("deliveryou_admin_msgs").add(msg);
            //
            //             }
            //         });
            //
            //         getAdminMsgs(true, getIntent().getStringExtra("customer_name"));
            //     } else
            //     {
            //         findViewById(R.id.support_send_btn).setOnClickListener(new View.OnClickListener()
            //         {
            //             @Override
            //             public void onClick(View view)
            //             {
            //                 Map<String, Object> msg = new HashMap<>();
            //                 msg.put("msg", "C#" + msgEt.getText().toString());
            //                 msg.put("customer_name", Utils.getPref(Carpool_Chat.this, "customer_name", ""));
            //                 msg.put("time", FieldValue.serverTimestamp());
            //                 FirebaseFirestore.getInstance().collection("deliveryou_admin_msgs").add(msg);
            //
            //             }
            //         });
            //
            //         getAdminMsgs(false, Utils.getPref(Carpool_Chat.this, "customer_name", ""));
            //     }
            //
            // } else
            // {
            //     ((TextView) findViewById(R.id.customer_name)).setText(getIntent().getStringExtra("customer_name"));
            //     Glide.with(Carpool_Chat.this).load(getIntent().getStringExtra("img_url")).placeholder(R.drawable.deliveryou_full_logo)
            //             .into((ImageView) findViewById(R.id.customer_pic));
            //
            //     start_proc.setOnClickListener(new View.OnClickListener()
            //     {
            //         @Override
            //         public void onClick(View v)
            //         {
            //             Intent i = new Intent(Carpool_Chat.this, Payment_Screens.class);
            //             startActivity(i);
            //         }
            //     });
            //
            //     findViewById(R.id.support_send_btn).setOnClickListener(new View.OnClickListener()
            //     {
            //         @Override
            //         public void onClick(View view)
            //         {
            //             Map<String, Object> msg = new HashMap<>();
            //             msg.put("msg", "D#" + msgEt.getText().toString());
            //             msg.put("customer_name", getIntent().getStringExtra("customer_name"));
            //             msg.put("time", FieldValue.serverTimestamp());
            //             msg.put("post_id", getIntent().getStringExtra("post_id"));
            //             FirebaseFirestore.getInstance().collection("deliveryou_msgs").add(msg);
            //
            //         }
            //     });
            //
            //     getMsgs(true);
            // }

        }


    }

    private void loadChatData()
    {
        Utils.showProgressDialog(this, "Loading Messages\nPlease Wait");

        FirebaseDatabase.getInstance().getReference()
                .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                .child(activity.getId())
                .child("chat")
                .child(Constants.sessionUser.getIs_customer() ? driver.getId() : Constants.sessionUser.getId())
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
                            chatLv.scrollToPosition(messages.size() - 1);

                        } else
                        {
                            Toast.makeText(Carpool_Chat.this, "No Messages", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }

    // public void getAdminMsgs(boolean isAdmin, String customerName)
    // {
    //     FirebaseFirestore.getInstance().collection("deliveryou_admin_msgs")
    //             .whereEqualTo("customer_name", customerName)
    //             .orderBy("time", Query.Direction.ASCENDING)
    //             .addSnapshotListener(new EventListener<QuerySnapshot>()
    //             {
    //                 @Override
    //                 public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
    //                 {
    //                     if (error != null)
    //                     {
    //                         Utils.showToast(Carpool_Chat.this, error.getMessage());
    //                         return;
    //                     }
    //
    //                     ArrayList<String> msgs = new ArrayList<>();
    //                     for (QueryDocumentSnapshot doc : value)
    //                     {
    //                         if (doc.get("msg") != null)
    //                         {
    //                             msgs.add(doc.getString("msg"));
    //                         }
    //                     }
    //
    //                     chatLv.invalidateViews();
    //                     ChatAdapter chatAdapter = new ChatAdapter(Carpool_Chat.this, msgs, isAdmin);
    //                     chatLv.setAdapter(chatAdapter);
    //                 }
    //             });
    // }
    //
    // public void getMsgs(boolean isDriver)
    // {
    //     FirebaseFirestore.getInstance().collection("deliveryou_msgs")
    //             .whereEqualTo("post_id", getIntent().getStringExtra("post_id"))
    //             .orderBy("time", Query.Direction.ASCENDING)
    //             .addSnapshotListener(new EventListener<QuerySnapshot>()
    //             {
    //                 @Override
    //                 public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
    //                 {
    //                     if (error != null)
    //                     {
    //                         Utils.showToast(Carpool_Chat.this, error.getMessage());
    //                         return;
    //                     }
    //
    //                     ArrayList<String> msgs = new ArrayList<>();
    //                     for (QueryDocumentSnapshot doc : value)
    //                     {
    //                         if (doc.get("msg") != null)
    //                         {
    //                             msgs.add(doc.getString("msg"));
    //                         }
    //                     }
    //
    //                     chatLv.invalidateViews();
    //                     ChatAdapter chatAdapter = new ChatAdapter(Carpool_Chat.this, msgs, isDriver);
    //                     chatLv.setAdapter(chatAdapter);
    //                 }
    //             });
    // }
}