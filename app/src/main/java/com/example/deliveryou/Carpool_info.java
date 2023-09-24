package com.example.deliveryou;

import android.widget.ImageView;

public class Carpool_info {


    String C_ID,Customer_Name,Pickup,Date_time;
    String Customer_Pic;
    int Rating;

    public Carpool_info(String c_ID, String customer_Name, String customer_Pic, String pickup, String date_time, int rating) {
        C_ID = c_ID;
        Customer_Name = customer_Name;
        Customer_Pic = customer_Pic;
        Pickup = pickup;
        Date_time = date_time;
        Rating = rating;
    }

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String  getCustomer_Pic() {
        return Customer_Pic;
    }

    public void setCustomer_Pic(String customer_Pic) {
        Customer_Pic = customer_Pic;
    }

    public String getPickup() {
        return Pickup;
    }

    public void setPickup(String pickup) {
        Pickup = pickup;
    }


    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }
}

