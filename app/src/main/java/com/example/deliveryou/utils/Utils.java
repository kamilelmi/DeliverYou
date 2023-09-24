package com.example.deliveryou.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class Utils {
    private static ProgressDialog progressDialog;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final Calendar myCalendar= Calendar.getInstance();
    public static void updateDateEt(EditText dateEt){
        SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_FORMAT, Locale.US);
        dateEt.setText(dateFormat.format(myCalendar.getTime()));
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean compareDates(String date1, String date2) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate parsedDate1 = LocalDate.parse(date1, formatter);
        LocalDate parsedDate2 = LocalDate.parse(date2, formatter);

        return  parsedDate1.isAfter(parsedDate2);
    }

    public static void showProgressDialog(Context context, String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String getRandomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String GetFileExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getFutureDate(int month){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(cal.getTime());

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void setSpinnerValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public static void setSpinnerAdapter(Context context, List<String> itemslist, Spinner spinner){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, itemslist);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public static void sendMsg(String phoneno, Context context){
        Uri sms_uri = Uri.parse("smsto:"+phoneno);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("sms_body", "SOLAR CEM");
        context.startActivity(sms_intent);
    }

    public static void startCall(String phoneno, Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneno));
        context.startActivity(intent);
    }

    public static void sendEmail(String email, Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacting from SOLAR CEM");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your message here");
        context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static void setPref(Context context, String key, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void clearPref(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getPref(Context context, String key, String defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);

    }

    public static String getCityName(Context context, double lat, double lng){
        if(lat==0 && lng==0)
            return "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            return "";
        }
    }


}
