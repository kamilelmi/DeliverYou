package com.example.deliveryou.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager
{

    private static SharedPreferenceManager manager = new SharedPreferenceManager();

    private SharedPreferenceManager() {}

    public static SharedPreferenceManager getInstance()
    {
        return manager;
    }

    public static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences("salonify_pref", Context.MODE_PRIVATE);
    }

    public static void addDataToSharedPreferences(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDataFromSharedPreferences(Context context, String key)
    {
        return getSharedPreferences(context).getString(key,"");
    }

    public static int getNotificationNumber(Context context)
    {
        String num = getSharedPreferences(context).getString("notification_number", "");

        assert num != null;
        if(num.equals(""))
        {
            propagateNumber("0", context);
            return 0;
        }else
        {
            propagateNumber(num, context);
        }

        return Integer.parseInt(num);

    }

    private static void propagateNumber(String num, Context context) {
        int numToAdd = Integer.parseInt(num);
        numToAdd++;
        addDataToSharedPreferences(context, "notification_number", String.valueOf(numToAdd));
    }

}

