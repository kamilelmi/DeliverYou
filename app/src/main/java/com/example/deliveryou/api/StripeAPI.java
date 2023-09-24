package com.example.deliveryou.api;

import com.example.deliveryou.models.VerificationCodeRequest;
import com.example.deliveryou.models.VerifyRequest;
import com.example.deliveryou.utils.Constants;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StripeAPI
{
    //Stripe API
    @FormUrlEncoded
    @POST("/v1/payment_intents")
    Call<ResponseBody> createPaymentIntent(
            @Field("amount") int amount,
            @Field("currency") String currency,
            @Field("customer") String customerId
    );

    @FormUrlEncoded
    @POST("/v1/customers")
    Call<ResponseBody> createCustomer(
            @Field("name") String name
    );


    @FormUrlEncoded
    @POST("/v1/ephemeral_keys")
    Call<ResponseBody> createCustomerKey(
            @Field("customer") String customerId
    );


    @Headers({
            "Authorization: Basic " + Constants.TWILIO_AUTH_KEY
    })
    @POST("Services/VA040b18210563f4d017a7f5f4bdc5de50/Verifications")
    Call<ResponseBody> createVerificationRequest(
            @Body RequestBody body
            );

    @Headers({
            "Authorization: Basic " + Constants.TWILIO_AUTH_KEY
    })
    @POST("Services/VA040b18210563f4d017a7f5f4bdc5de50/VerificationCheck")
    Call<ResponseBody> verifyCode(
            @Body RequestBody body
    );


}
