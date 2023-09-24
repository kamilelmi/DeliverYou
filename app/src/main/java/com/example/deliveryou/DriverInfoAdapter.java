package com.example.deliveryou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.deliveryou.models.User;
import com.example.deliveryou.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DriverInfoAdapter extends ArrayAdapter<User> {

    public DriverInfoAdapter(Context context, ArrayList<User> driverInfos) {
        super(context, 0, driverInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User driverInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_driver, parent, false);
        }

        TextView infoTv = convertView.findViewById(R.id.item_info);
        ImageView carIv = convertView.findViewById(R.id.item_car);
        ImageView licenseIv = convertView.findViewById(R.id.item_license);

        Button approveBtn = convertView.findViewById(R.id.item_approve);

        infoTv.setText("Car model: " + driverInfo.getCar_model()
        + "\nLicense no: " + driverInfo.getLicense_no()
        +"\nNumber plate: " + driverInfo.getNumber_plate());

        Glide.with(getContext()).load(driverInfo.getCar_url()).placeholder(R.drawable.deliveryou_full_logo)
                .into(carIv);
        Glide.with(getContext()).load(driverInfo.getLicense_url()).placeholder(R.drawable.deliveryou_full_logo)
                .into(licenseIv);

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressDialog(getContext(), "Approving driver info\nPlease wait");

                Map<String, Object> update = new HashMap<>();
                update.put("is_approved", true);

                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(driverInfo.getId())
                        .child("is_approved")
                        .setValue(true)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Utils.dismissProgressDialog();

                                if(task.isSuccessful())
                                {
                                    remove(driverInfo);
                                    Toast.makeText(getContext(), "Driver Approved!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return convertView;
    }
}
