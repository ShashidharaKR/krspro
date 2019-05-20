package com.example.user.advocate.logics;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.advocate.R;
import com.example.user.advocate.activities.User;
import com.example.user.advocate.interfaces.OnDateSetInterface;
import com.example.user.advocate.models.Users;
import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class S {

    public static String URL = "http://sskrs04shashi.000webhostapp.com/e_alert/index.php/";
    public static String IMAGE_URL = "http://sskrs04shashi.000webhostapp.com/e_alert/index.php/";

    public static void LogD(String msg) {
        Log.d("Blank", "-----------------------------------------------------------------");
        Log.d("Blank", msg);
        Log.d("Blank", "-----------------------------------------------------------------");
    }

    public static void LogE(String msg) {
        Log.d("Blank", "-----------------------------------------------------------------");
        Log.d("Blank", msg);
    }

    public static boolean analyseResponse(Response<?> response) {
        if (response == null) {
            LogE(" <<<<<< Response: NULL >>>>>>");
            return false;
        }

        if (response.body() == null) {
            LogE(" <<<<<< Response: BODY NULL >>>>>>");
            return false;
        }

        return true;
    }

    public static void displayNetworkErrorMessage(Context context, View view, Throwable t) {

        if (view == null) {


            if (t instanceof ConnectException)
                Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_SHORT).show();
            else if (t instanceof SocketTimeoutException)
                Toast.makeText(context, "Server Timed Out", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        } else {

            if (t instanceof ConnectException)
                Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_LONG).show();

            else if (t instanceof SocketTimeoutException)
                Toast.makeText(context, "Server Timed Out", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

        }


    }

    public static void sendSMS(String number, String msg) {
        LogD("number : " + number + " msg : " + msg);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }


    public static String getDate() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.DATE)) + "/" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "/" + c.get(Calendar.YEAR);
        return str;
    }

    public static String getTime() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + prefixZeroIfSingleDigit((c.get(Calendar.MINUTE)));
        return str;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        String strDate =  mdformat.format(calendar.getTime());
        return strDate;
    }

    public static String convertToCustomDateFormat(int y, int monthMinusOneValue, int d) {
        return prefixZeroIfSingleDigit(y) + "-" + prefixZeroIfSingleDigit(monthMinusOneValue + 1) + "-" + prefixZeroIfSingleDigit(d);
    }

    public static String prefixZeroIfSingleDigit(int intVal) {
        Integer i = new Integer(intVal);
        String val = i.toString();
        return val.length() == 1 ? "0" + val : val;
    }

    //SET SPINNER ADAPTER
    public static void setSpinnerAdapter(Context context, Spinner sp, List<String> list) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.ao_simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
    }

    //SET SPINNER ADAPTER
    public static void setSpinnerAdapter(Context context, Spinner sp, String[] list) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.ao_simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
    }

//
    public static boolean isValidEditText(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please Enter " + errorMessage);
            return false;
        }
        return true;
    }

    public static String generateOTP() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }




    public static String getDateYYMMDD() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.YEAR)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.DATE)));
        return str;
    }


    public static void setDateDefault(Context context, final String tittle, final TextView txt_date1, final TextView txt_date2, int day, boolean canSetMax, boolean canSetMin, final OnDateSetInterface onDateSetInterface) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (txt_date1.getText().toString().trim().length() > 0) {
            String a[] = txt_date1.getText().toString().trim().split("-");
            mYear = Integer.parseInt(a[0]);
            mMonth = Integer.parseInt(a[1]) - 1;
            mDay = Integer.parseInt(a[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //c.set(year, month, day);
                        S.LogD(year + " " + month + " " + day);
                        String date = S.convertToCustomDateFormat(year, month, day);
                        txt_date1.setText(date);
                        txt_date1.setError(null);
                        onDateSetInterface.OnDateSet(date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.updateDate(mYear, mMonth, mDay);
        try {
            if (canSetMax && (!canSetMin)) {
                if (txt_date2.getText().toString().trim().length() > 0) {
                    datePickerDialog.getDatePicker().setMaxDate(getInstance(txt_date2).getTimeInMillis());
                }

            }
            if (canSetMin) {
                if (txt_date2.getText().toString().trim().length() > 0) {
                    datePickerDialog.getDatePicker().setMinDate(getInstance(txt_date2).getTimeInMillis() - 2000);
                }
            }
            if (canSetMax && canSetMin) {
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        S.LogD("DATE FOR UPDATE " + mYear + "-" + mMonth + "-" + mDay);

        datePickerDialog.setTitle(tittle);
        datePickerDialog.show();

    }

    public static Calendar getInstance(TextView txt_date1) {
        final Calendar c = Calendar.getInstance();
        if (txt_date1.getText().toString().trim().length() > 0) {
            String a[] = txt_date1.getText().toString().trim().split("-");
            c.set(Calendar.YEAR, Integer.parseInt(a[0]));
            c.set(Calendar.MONTH, Integer.parseInt(a[1]) - 1);
            c.set(Calendar.DATE, Integer.parseInt(a[2]));
        }
        return c;
    }









    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//SHARED PREFERENCES TO STORE LOGIN CREDETIONALS
    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE);

        if (prefs.getString("id", null) == null) return false;
        else
            return true;
    }

    public static final String PREFS_LOGIN = "PREFS_LOGIN";

    public static void saveUserLoginData(Context context, Users u) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString("id", u.id);
        editor.putString("name", u.name);
        editor.putString("password", u.password);
        editor.putString("mobile", u.mobile);
        editor.putString("lat", u.lat);
        editor.putString("lng", u.lng);
        editor.putString("type", u.type);
        editor.apply();
    }

    public static Users getUserDetails(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE);
        Users u = new Users();
        u.id = prefs.getString("id", null);
        u.name = prefs.getString("name", null);
        u.password = prefs.getString("password", null);
        u.mobile = prefs.getString("mobile", null);
        u.lat = prefs.getString("lat", null);
        u.lng = prefs.getString("lng", null);
        u.type = prefs.getString("type", null);
        return u;
    }


    public static void userLogout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString("id", null);
        editor.putString("name", null);
        editor.putString("password", null);
        editor.putString("mobile", null);
        editor.putString("lat", null);
        editor.putString("lng", null);
        editor.putString("type", null);
        editor.clear();
        editor.commit();
    }
}