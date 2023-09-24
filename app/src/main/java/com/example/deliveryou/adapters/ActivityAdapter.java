package com.example.deliveryou.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.deliveryou.R;
import com.example.deliveryou.databinding.LiActivityBinding;
import com.example.deliveryou.models.Activity;
import com.example.deliveryou.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class ActivityAdapter extends ArrayAdapter<Activity>
{
    private ArrayList<Activity> activities;
    private Context context;

    public ActivityAdapter(@NonNull Context context, ArrayList<Activity> activities)
    {
        super(context, R.layout.li_activity, activities);
        this.context = context;
        this.activities = activities;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LiActivityBinding binding;

        if (convertView == null)
        {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.li_activity, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else
        {
            binding = (LiActivityBinding) convertView.getTag();
        }

        Activity activity = activities.get(position);

        if (activity.getActivityType().equals("Carpool"))
        {
            binding.ivActivity.setImageResource(R.drawable.ic_carpooling);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(activity.getTimestamp());
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


        } else
        {
            binding.ivActivity.setImageResource(R.drawable.ic_delivery);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(activity.getPickup_time());
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

            binding.tvDropoff.setVisibility(View.VISIBLE);

        }

        if (activity.isActive())
        {
            binding.tvCancel.setVisibility(View.VISIBLE);
        } else
        {
            binding.tvCancel.setVisibility(View.GONE);
        }


        binding.tvCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(Constants.sessionUser.getId())
                        .child(activity.getActivityType().equals("Carpool") ? "my_carpool_requests" : "delivery_requests")
                        .child(activity.getId())
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                FirebaseDatabase.getInstance().getReference()
                                        .child(activity.getActivityType().equals("Carpool") ? "carpool_requests" : "delivery_requests")
                                        .child(activity.getId())
                                        .setValue(null);
                                Toast.makeText(context, "Request Cancelled!", Toast.LENGTH_SHORT).show();
                                remove(activity);

                            }
                        });
            }
        });

        return convertView;
    }
}
