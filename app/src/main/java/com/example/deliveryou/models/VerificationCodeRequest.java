package com.example.deliveryou.models;

import com.google.gson.annotations.SerializedName;

public class VerificationCodeRequest {
    @SerializedName("To")
    private String To;

    @SerializedName("Code")
    private String Code;

    public VerificationCodeRequest(String to, String code) {
        this.To = to;
        this.Code = code;
    }

    public String getTo()
    {
        return To;
    }

    public void setTo(String to)
    {
        this.To = to;
    }

    public String getCode()
    {
        return Code;
    }

    public void setCode(String code)
    {
        this.Code = code;
    }
}
