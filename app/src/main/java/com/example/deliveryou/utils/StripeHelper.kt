package com.example.deliveryou.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deliveryou.StripeResultCallback
import com.example.deliveryou.api.StripeAPI
import com.example.deliveryou.interfaces.StripeCallback
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class StripeHelper()
{
    lateinit var paymentSheet: PaymentSheet

    fun initPaymentSheet(activity: AppCompatActivity, stripeResultCallBack: StripeResultCallback)
    {
        paymentSheet = PaymentSheet(activity){
            when(it) {
                is PaymentSheetResult.Canceled -> {
                    Log.d("MKL","Canceled")
                    stripeResultCallBack.onTransactionFailed()
                }
                is PaymentSheetResult.Failed -> {
                    Log.d("MKL","Error: ${it.error}")
                    Toast.makeText(activity,"Transaction Failed! Please try again or use any other payment mode", Toast.LENGTH_SHORT)
                        .show()
                    stripeResultCallBack.onTransactionFailed()
                }
                is PaymentSheetResult.Completed -> {
                    // Display e.g., an order confirmation screen
                    stripeResultCallBack.onTransactionSuccess()
                    Log.d("MKL","Completed")
                }
            }
        }
    }

    fun getStripeCustomerId(context: Context) {

        val stripeId = SharedPreferenceManager.getDataFromSharedPreferences(context, "stripe_id")

        if(stripeId.isEmpty())
        {
            val client = OkHttpClient.Builder().addInterceptor(object: Interceptor
            {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${Constants.STRIPE_KEY}")
                        .build()
                    return chain.proceed(newRequest)
                }
            })
                .build()


            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.STRIPE_BASE_URL)
                .build()


            val call = retrofit.create(StripeAPI::class.java).createCustomer(
                "test_user"
            )

            Log.d("MKL", "Triggering customer request")

            call.enqueue(object : Callback<ResponseBody>
            {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if(response.isSuccessful)
                    {
                        val res = response.body()!!.string()
                        Log.d("MKL",res)

                        val json = JSONObject(res)

                        //                    FirebaseDatabase.getInstance().reference
                        //                        .child("customer")
                        //                        .child(Session.USER.id.toString())
                        //                        .child("stripe_id")
                        //                        .setValue(json.getString("id"))

                        Constants.STRIPE_ID = json.getString("id")
                    }
                    else
                    {
                        val res = response.errorBody()!!.string()
                        Log.d("MKL", "Response not successful with reason\n $res")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("MKL", "CAll Failed with exception: ${t.message}")
                }
            })
        }
        else
        {
            Constants.STRIPE_ID = stripeId
        }



    }

    fun getEphemeralKey(callBack: StripeCallback)
    {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${Constants.STRIPE_KEY}")
                .addHeader("Stripe-Version", "2020-08-27").build()
            chain.proceed(newRequest)
        }).build()


        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.STRIPE_BASE_URL)
            .build()


        val call = retrofit.create(StripeAPI::class.java).createCustomerKey(
            Constants.STRIPE_ID
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful)
                {
                    val res = response.body()!!.string()
                    Log.d("MKL",res)

                    val json = JSONObject(res)

                    callBack.onKeyDownloaded(json.getString("secret"))

                }
                else
                {
                    val res = response.errorBody()!!.string()
                    Log.d("MKL", "Response not successful with reason\n $res")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("MKL", "CAll Failed with exception: ${t.message}")
            }
        })

    }

    fun runStripe(amount: Double, callBack: StripeCallback) {


        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${Constants.STRIPE_KEY}").build()
            chain.proceed(newRequest)
        }).build()


        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.STRIPE_BASE_URL)
            .build()

        val amountToSend = amount * 100

        val call = retrofit.create(StripeAPI::class.java).createPaymentIntent(
            amountToSend.toInt(),
            "gbp",
            Constants.STRIPE_ID
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful)
                {
                    val res = response.body()!!.string()
                    Log.d("MKL",res)

                    val json = JSONObject(res)

                    val paymentIntentClientSecret = json.getString("client_secret")
                    callBack.onKeyDownloaded(paymentIntentClientSecret)

                }
                else
                {
                    val res = response.errorBody()!!.string()
                    Log.d("MKL", "Response not successful with reason\n $res")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("MKL", "CAll Failed with exception: ${t.message}")
            }
        })


    }


}