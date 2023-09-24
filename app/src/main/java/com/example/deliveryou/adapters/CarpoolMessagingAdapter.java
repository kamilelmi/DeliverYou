package com.example.deliveryou.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.deliveryou.Payment_Screens;
import com.example.deliveryou.databinding.LiRecievedMessageBinding;
import com.example.deliveryou.databinding.LiSentMessageBinding;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.models.Message;
import com.example.deliveryou.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarpoolMessagingAdapter extends RecyclerView.Adapter
{
    private final Context context;
    private Activity activity;
    private List<Message> messages;

    public CarpoolMessagingAdapter(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == 0)
        {
            LiSentMessageBinding binding = LiSentMessageBinding
                    .inflate(LayoutInflater.from(context), parent, false);
            return new SentMessageViewHolder(binding);
        }
        else
        {
            LiRecievedMessageBinding binding = LiRecievedMessageBinding
                    .inflate(LayoutInflater.from(context), parent, false);
            return new ReceivedMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        int pos = holder.getAbsoluteAdapterPosition();

        if(holder.getItemViewType() == 0)
        {
            SentMessageViewHolder messageViewHolder = (SentMessageViewHolder) holder;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(messages.get(pos).getTimestamp());
            messageViewHolder.binding.tvMessageTime.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));
            messageViewHolder.binding.tvMessage.setText(messages.get(pos).getText());

            if(messages.get(pos).getType().equals("offer"))
            {
                messageViewHolder.binding.tvMessage.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                messageViewHolder.binding.tvMessage.setTypeface(null, Typeface.NORMAL);
            }

        }
        else
        {
            ReceivedMessageViewHolder messageViewHolder = (ReceivedMessageViewHolder) holder;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(messages.get(pos).getTimestamp());
            messageViewHolder.binding.tvMessageTime.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));
            messageViewHolder.binding.tvMessage.setText(messages.get(pos).getText());

            if(messages.get(pos).getType().equals("text"))
            {
                messageViewHolder.binding.llOffer.setVisibility(View.GONE);
            }
            else
            {
                messageViewHolder.binding.llOffer.setVisibility(View.VISIBLE);
                messageViewHolder.binding.tvOffer.setText("Offer Price: " + messages.get(pos).getOfferPrice());
                messageViewHolder.binding.btnAcceptOffer.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(context, Payment_Screens.class);
                        intent.putExtra("activity", activity);
                        intent.putExtra("price", messages.get(pos).getOfferPrice());
                        intent.putExtra("driver", messages.get(pos).getSender());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if(messages == null)
            return 0;
        return messages.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        Message message = messages.get(position);

        if(message.getSender().getId().equals(Constants.sessionUser.getId()))
        {
            return 0;
        }
        else
        {
            return 1;
        }

    }
}

class SentMessageViewHolder extends RecyclerView.ViewHolder
{
    LiSentMessageBinding binding;

    public SentMessageViewHolder(@NonNull LiSentMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
    }
}

class ReceivedMessageViewHolder extends RecyclerView.ViewHolder
{
    LiRecievedMessageBinding binding;

    public ReceivedMessageViewHolder(@NonNull LiRecievedMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
    }
}
