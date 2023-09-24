package com.example.deliveryou.models;

import java.util.List;

public class Chat
{
    private User driver_details;
    private User user_details;
    private List<Message> messages;

    public Chat()
    {

    }


    public User getUser_details()
    {
        return user_details;
    }

    public void setUser_details(User user_details)
    {
        this.user_details = user_details;
    }

    public User getDriver_details()
    {
        return driver_details;
    }

    public void setDriver_details(User driver_details)
    {
        this.driver_details = driver_details;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }
}
