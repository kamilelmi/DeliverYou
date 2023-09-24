package com.example.deliveryou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.deliveryou.R;
import com.example.deliveryou.databinding.LiChatBinding;
import com.example.deliveryou.models.Chat;
import com.example.deliveryou.utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class DriverChatAdapter extends ArrayAdapter<Chat>
{
    private Context context;
    private List<Chat> chats;


    public DriverChatAdapter(@NonNull Context context, List<Chat> chats)
    {
        super(context, R.layout.li_chat, chats);
        this.context = context;
        this.chats = chats;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LiChatBinding binding;

        if (convertView == null)
        {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.li_chat, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else
        {
            binding = (LiChatBinding) convertView.getTag();
        }

        Chat chat = chats.get(position);

        if(Constants.sessionUser.getUsername().equals("admin"))
        {
            binding.tvName.setText(chat.getUser_details().getUsername());
        }
        else
        {
            binding.tvName.setText(chat.getDriver_details().getUsername());
        }

        binding.tvLastMessage.setText(chat.getMessages().get(chat.getMessages().size() -1).getText());

        if(Constants.sessionUser.getUsername().equals("admin"))
        {
            if(chat.getUser_details().getImg_url() != null)
            {
                Glide.with(context).load(chat.getUser_details().getImg_url())
                        .into(binding.ivProfile);
            }
        }
        else
        {
            if(chat.getDriver_details().getImg_url() != null)
            {
                Glide.with(context).load(chat.getDriver_details().getImg_url())
                        .into(binding.ivProfile);
            }
        }



        return convertView;
    }
}
