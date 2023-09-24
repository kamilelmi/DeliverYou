package com.example.deliveryou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Carpool_Adapter extends RecyclerView.Adapter<Carpool_Adapter.MyViewHolder> {
    List<Carpool_info> ls;
    Context c;

    public Carpool_Adapter(List<Carpool_info> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.carpool_request_card,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull Carpool_Adapter.MyViewHolder holder, int position) {

        int Rating;
        holder.name.setText(ls.get(position).getCustomer_Name());
        holder.pickup_loc.setText(ls.get(position).getPickup());
        holder.destination_loc.setText(ls.get(position).Date_time);
        Picasso.get().load(ls.get(position).getCustomer_Pic()).into(holder.profile_pic);
        Rating = ls.get(position).getRating()*2;
        holder.rating.setProgress(Rating);
        String ID = ls.get(position).getC_ID();

        holder.carpool_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,Specific_Carpool.class);
                i.putExtra("post_id", ID);
                c.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout carpool_layout;
        ImageView profile_pic;
        RatingBar rating;
        TextView name,pickup_loc,destination_loc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            carpool_layout = itemView.findViewById(R.id.carpool_layout);
            profile_pic = itemView.findViewById(R.id.customer_pic);
            name = itemView.findViewById(R.id.customer_name);
            rating = itemView.findViewById(R.id.ratingBar);
            pickup_loc = itemView.findViewById(R.id.pickup_location);
            destination_loc = itemView.findViewById(R.id.destination_location);
        }
    }
}
