package com.example.deliveryou;

public class DriverInfo {

    public String car_url, license_url, car_model, number_plate, license_no, id;

    public DriverInfo(String id, String car_url, String license_url, String car_model, String number_plate, String license_no) {
        this.id = id;
        this.car_url = car_url;
        this.license_url = license_url;
        this.car_model = car_model;
        this.number_plate = number_plate;
        this.license_no = license_no;
    }

    public DriverInfo() {
    }
}
