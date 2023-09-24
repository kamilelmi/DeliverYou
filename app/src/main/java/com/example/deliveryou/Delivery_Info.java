package com.example.deliveryou;

public class Delivery_Info {

    public String C_ID,Customer_Name,Pickup,Dropoff,Pickup_Time,Estimated_TIme,Total_Time,Length,Width,Height,Weight;
    public String Customer_Pic;

    public Delivery_Info(String c_ID, String customer_Name, String pickup, String dropoff, String pickup_Time, String estimated_TIme, String total_Time, String length, String width, String height, String weight, String customer_Pic, int rating) {
        C_ID = c_ID;
        Customer_Name = customer_Name;
        Pickup = pickup;
        Dropoff = dropoff;
        Pickup_Time = pickup_Time;
        Estimated_TIme = estimated_TIme;
        Total_Time = total_Time;
        Length = length;
        Width = width;
        Height = height;
        Weight = weight;
        Customer_Pic = customer_Pic;
        Rating = rating;
    }

    int Rating;

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

    public String getPickup() {
        return Pickup;
    }

    public void setPickup(String pickup) {
        Pickup = pickup;
    }

    public String getDropoff() {
        return Dropoff;
    }

    public void setDropoff(String dropoff) {
        Dropoff = dropoff;
    }

    public String getPickup_Time() {
        return Pickup_Time;
    }

    public void setPickup_Time(String pickup_Time) {
        Pickup_Time = pickup_Time;
    }

    public String getEstimated_TIme() {
        return Estimated_TIme;
    }

    public void setEstimated_TIme(String estimated_TIme) {
        Estimated_TIme = estimated_TIme;
    }

    public String getTotal_Time() {
        return Total_Time;
    }

    public void setTotal_Time(String total_Time) {
        Total_Time = total_Time;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getCustomer_Pic() {
        return Customer_Pic;
    }

    public void setCustomer_Pic(String customer_Pic) {
        Customer_Pic = customer_Pic;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }
}
