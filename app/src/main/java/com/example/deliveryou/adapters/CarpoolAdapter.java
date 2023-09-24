package com.example.deliveryou.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.deliveryou.R;
import com.example.deliveryou.Specific_Carpool;
import com.example.deliveryou.Specific_Delivery;
import com.example.deliveryou.databinding.LiActivityBinding;
import com.example.deliveryou.databinding.LiCarpoolRequestBinding;
import com.example.deliveryou.models.Activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class CarpoolAdapter extends ArrayAdapter<Activity>
{
    private ArrayList<Activity> activities;
    private Context context;

    public CarpoolAdapter(@NonNull Context context, ArrayList<Activity> activities)
    {
        super(context, R.layout.li_carpool_request, activities);
        this.context = context;
        this.activities = activities;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LiCarpoolRequestBinding binding;

        if (convertView == null)
        {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.li_carpool_request, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else
        {
            binding = (LiCarpoolRequestBinding) convertView.getTag();
        }

        Activity activity = activities.get(position);

        if(activity.getRequestedBy() != null && activity.getRequestedBy().getImg_url() != null)
        {
            if (!activity.getRequestedBy().getImg_url().isEmpty())
            {
                Glide.with(context).load(activity.getRequestedBy().getImg_url())
                        .into(binding.ivActivity);
            } else
            {
                binding.ivActivity.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }
        }



        Calendar calendar = Calendar.getInstance();
        if(activity.getActivityType().equals("Delivery"))
        {
            calendar.setTimeInMillis(activity.getPickup_time());

            if(activity.isRideToStart())
            {
                binding.tvStart.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.tvStart.setVisibility(View.GONE);
            }
        }
        else
        {
            calendar.setTimeInMillis(activity.getTimestamp());
        }
        binding.tvPickupTime.setText("Pickup Time: " + new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));

        Geocoder coder = new Geocoder(context);
        try
        {
            List<Address> addressList = coder
                    .getFromLocation(
                            Double.parseDouble(activity.getPickup_loc().split(",")[0]),
                            Double.parseDouble(activity.getPickup_loc().split(",")[1]),
                            1
                    );

            String address = "";


            binding.tvPickup.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.tvPickup.setText("Location not found");
        }

        try
        {
            List<Address> addressList = coder
                    .getFromLocation(
                            Double.parseDouble(activity.getDestination_loc().split(",")[0]),
                            Double.parseDouble(activity.getDestination_loc().split(",")[1]),
                            1
                    );

            String address = "";

            if (addressList.get(0).getThoroughfare() != null)
            {
                address = addressList.get(0).getThoroughfare() + ", ";
            }


            if (addressList.get(0).getSubAdminArea() != null)
            {
                address = address + addressList.get(0).getSubAdminArea();
            }

            binding.tvDropoff.setText(addressList.get(0).getAddressLine(0));

        } catch (IOException e)
        {
            binding.tvDropoff.setText("Location not found");
        }


        binding.tvRequestedBy.setText(activity.getRequestedBy().getUsername());


        binding.getRoot().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i;
                if(activity.getActivityType().equals("Delivery"))
                {
                    i = new Intent(context, Specific_Delivery.class);
                }
                else
                {
                    i = new Intent(context, Specific_Carpool.class);
                }
                i.putExtra("activity", activity);
                context.startActivity(i);
            }
        });

        binding.tvStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i;
                if(activity.getActivityType().equals("Delivery"))
                {
                    i = new Intent(context, Specific_Delivery.class);
                }
                else
                {
                    i = new Intent(context, Specific_Carpool.class);
                }
                i.putExtra("activity", activity);
                context.startActivity(i);
            }
        });


        return convertView;
    }
}
