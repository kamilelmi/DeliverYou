package com.example.deliveryou.models;

public class Message
{
    private String id;
    private String text;
    private Long timestamp;
    private User sender;
    private String type;
    private double offerPrice;
    private String status;

    public Message()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public User getSender()
    {
        return sender;
    }

    public void setSender(User sender)
    {
        this.sender = sender;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public double getOfferPrice()
    {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice)
    {
        this.offerPrice = offerPrice;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
