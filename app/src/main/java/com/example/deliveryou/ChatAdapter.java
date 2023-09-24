package com.example.deliveryou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;



public class ChatAdapter extends ArrayAdapter<String> {
    boolean isDriver;
    public ChatAdapter(Context context, ArrayList<String> msgs, boolean isDriver) {
        super(context, 0, msgs);
        this.isDriver = isDriver;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String msg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);

        // Lookup view for data population
        TextView rightTv = (TextView) convertView.findViewById(R.id.msg_right);
        TextView leftTv = (TextView) convertView.findViewById(R.id.msg_left);

        CardView rightCv = convertView.findViewById(R.id.msg_right_card);
        CardView leftCv = convertView.findViewById(R.id.msg_left_card);

//        if(isDriver){
//            if(msg.contains("D#")){
//                leftCv.setVisibility(View.GONE);
//                rightCv.setVisibility(View.VISIBLE);
//                rightTv.setText(msg.replace("D#",""));
//            }else{
//                rightCv.setVisibility(View.GONE);
//                leftCv.setVisibility(View.VISIBLE);
//                leftTv.setText(msg.replace("C#",""));
//            }
//        }else{
//            if(msg.contains("A#")){
//                leftCv.setVisibility(View.GONE);
//                rightCv.setVisibility(View.VISIBLE);
//                rightTv.setText(msg.replace("A#",""));
//            }else{
//                rightCv.setVisibility(View.GONE);
//                leftCv.setVisibility(View.VISIBLE);
//                leftTv.setText(msg.replace("C#",""));
//            }
//        }


        if(isDriver){
            if(msg.contains("D#")){
                leftCv.setVisibility(View.GONE);
                rightCv.setVisibility(View.VISIBLE);
                rightTv.setText(msg.replace("D#",""));
            } else if(msg.contains("A#")){
                leftCv.setVisibility(View.GONE);
                rightCv.setVisibility(View.VISIBLE);
                rightTv.setText(msg.replace("A#",""));
            }else{
                rightCv.setVisibility(View.GONE);
                leftCv.setVisibility(View.VISIBLE);
                leftTv.setText(msg.replace("C#",""));
            }
        }else{
            if(msg.contains("D#")){
                leftCv.setVisibility(View.VISIBLE);
                rightCv.setVisibility(View.GONE);
                leftTv.setText(msg.replace("D#",""));
            } else if(msg.contains("A#")){
                leftCv.setVisibility(View.VISIBLE);
                rightCv.setVisibility(View.GONE);
                leftTv.setText(msg.replace("A#",""));
            }else{
                rightCv.setVisibility(View.VISIBLE);
                leftCv.setVisibility(View.GONE);
                rightTv.setText(msg.replace("C#",""));
            }
        }

        return convertView;
    }
}