package com.example.deliveryou;

public interface StripeResultCallback
{
    void onTransactionSuccess();
    void onTransactionFailed();
}
