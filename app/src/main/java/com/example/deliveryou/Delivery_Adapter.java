package com.example.deliveryou;

import android.annotation.SuppressLint;
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

public class Delivery_Adapter extends RecyclerView.Adapter<Delivery_Adapter.MyViewHolder> {
    List<Delivery_Info> ls;
    Context c;

    public Delivery_Adapter(List<Delivery_Info> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.delivery_request_card,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull Delivery_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int Rating;
        holder.name.setText(ls.get(position).getCustomer_Name());
        holder.pickup_loc.setText(ls.get(position).getPickup());
        holder.destination_loc.setText(ls.get(position).getDropoff());
        Picasso.get().load(ls.get(position).getCustomer_Pic()).into(holder.profile_pic);
        Rating = ls.get(position).getRating()*2;
        holder.rating.setProgress(Rating);
        String ID = ls.get(position).getC_ID();

        holder.carpool_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,Specific_Delivery.class);
                i.putExtra("customer_name", ls.get(position).Customer_Name);
                i.putExtra("img_url", ls.get(position).Customer_Pic);
                i.putExtra("pickup_loc", ls.get(position).getPickup());
                i.putExtra("destination_loc", ls.get(position).getDropoff());
                i.putExtra("pickup_time", ls.get(position).getPickup_Time());
                i.putExtra("weight", ls.get(position).getWeight());
                i.putExtra("length", ls.get(position).getLength());
                i.putExtra("width", ls.get(position).getWidth());
                i.putExtra("height", ls.get(position).getHeight());
                i.putExtra("id", ls.get(position).getC_ID());
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
            carpool_layout = itemView.findViewById(R.id.delivery_layout);
            profile_pic = itemView.findViewById(R.id.customer_picture);
            name = itemView.findViewById(R.id.customer_name_delivery);
            rating = itemView.findViewById(R.id.ratingBar_delivery);
            pickup_loc = itemView.findViewById(R.id.pickup_location_delivery);
            destination_loc = itemView.findViewById(R.id.destination_location_delivery);
        }
    }
}
