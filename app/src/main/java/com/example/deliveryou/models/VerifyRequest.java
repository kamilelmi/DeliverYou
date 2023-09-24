package com.example.deliveryou.models;

import com.google.gson.annotations.SerializedName;

public class VerifyRequest {
    @SerializedName("To")
    private String To;

    @SerializedName("Channel")
    private String Channel;

    public VerifyRequest(String to, String channel) {
        this.To = to;
        this.Channel = channel;
    }

    public String getTo()
    {
        return To;
    }

    public void setTo(String to)
    {
        this.To = to;
    }

    public String getChannel()
    {
        return Channel;
    }

    public void setChannel(String channel)
    {
        this.Channel = channel;
    }
}