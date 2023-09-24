package com.example.deliveryou.models;

import java.io.Serializable;

public class Activity implements Serializable
{

    private User assigned_driver;
    private String id;
    private String destination_loc;
    private String pickup_loc;
    private Long pickup_time;
    private String instructions;
    private String length;
    private String type;
    private String height;
    private String weight;
    private String width;
    private User requestedBy;
    private boolean isActive = true;
    private Long timestamp;
    private String lat_lng;
    private String activityType;

    private boolean rideToStart;


    public Activity()
    {

    }

    public boolean isRideToStart()
    {
        return rideToStart;
    }

    public void setRideToStart(boolean rideToStart)
    {
        this.rideToStart = rideToStart;
    }

    public User getAssigned_driver()
    {
        return assigned_driver;
    }

    public void setAssigned_driver(User assigned_driver)
    {
        this.assigned_driver = assigned_driver;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDestination_loc()
    {
        return destination_loc;
    }

    public void setDestination_loc(String destination_loc)
    {
        this.destination_loc = destination_loc;
    }

    public String getPickup_loc()
    {
        return pickup_loc;
    }

    public void setPickup_loc(String pickup_loc)
    {
        this.pickup_loc = pickup_loc;
    }

    public Long getPickup_time()
    {
        return pickup_time;
    }

    public void setPickup_time(Long pickup_time)
    {
        this.pickup_time = pickup_time;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public String getLength()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length = length;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public User getRequestedBy()
    {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy)
    {
        this.requestedBy = requestedBy;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getLat_lng()
    {
        return lat_lng;
    }

    public void setLat_lng(String lat_lng)
    {
        this.lat_lng = lat_lng;
    }

    public String getActivityType()
    {
        return activityType;
    }

    public void setActivityType(String activityType)
    {
        this.activityType = activityType;
    }
}
