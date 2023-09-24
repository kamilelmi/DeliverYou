package com.example.deliveryou.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("isNumberVerified")
    @Expose
    private Boolean isNumberVerified;
    @SerializedName("is_customer")
    @Expose
    private Boolean is_customer;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone_no")
    @Expose
    private String phone_no;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("imgUrl")
    @Expose
    private String img_url;

    @SerializedName("is_approved")
    @Expose
    private Boolean is_approved;

    @SerializedName("car_url")
    @Expose
    private String car_url;

    @SerializedName("license_url")
    @Expose
    private String license_url;

    @SerializedName("license_no")
    @Expose
    private String license_no;

    @SerializedName("number_plate")
    @Expose
    private String number_plate;

    @SerializedName("car_model")
    @Expose
    private String car_model;


    public String getCar_url()
    {
        return car_url;
    }

    public void setCar_url(String car_url)
    {
        this.car_url = car_url;
    }

    public String getLicense_url()
    {
        return license_url;
    }

    public void setLicense_url(String license_url)
    {
        this.license_url = license_url;
    }

    public String getLicense_no()
    {
        return license_no;
    }

    public void setLicense_no(String license_no)
    {
        this.license_no = license_no;
    }

    public String getNumber_plate()
    {
        return number_plate;
    }

    public void setNumber_plate(String number_plate)
    {
        this.number_plate = number_plate;
    }

    public String getCar_model()
    {
        return car_model;
    }

    public void setCar_model(String car_model)
    {
        this.car_model = car_model;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Boolean getIs_approved()
    {
        return is_approved;
    }

    public void setIs_approved(Boolean approved)
    {
        is_approved = approved;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Boolean getIsNumberVerified() {
        return isNumberVerified;
    }

    public void setIsNumberVerified(Boolean isNumberVerified) {
        this.isNumberVerified = isNumberVerified;
    }

    public Boolean getIs_customer() {
        return is_customer;
    }

    public void setIs_customer(Boolean is_customer) {
        this.is_customer = is_customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg_url() {
        return img_url == null? "": img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}