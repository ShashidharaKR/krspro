package com.example.user.advocate.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.UserRegisterInput;
import com.example.user.advocate.models.UserRegisterResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
//import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.GPSTracker;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Advocate_register extends AppCompatActivity {
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_pwd)
    EditText edt_pwd;
    @BindView(R.id.edt_cfm_pwd)
    EditText edt_cfm_pwd;
    @BindView(R.id.edt_addr)
    EditText edt_addr;
    @BindView(R.id.edt_lic)
    EditText edt_lic;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    private Context context;
   // private SweetAlertDialog pDialog;
    public String lat,lng,type,latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocate_register);
        ButterKnife.bind(this);
        context= Advocate_register.this;
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading...");

        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());

            Toast.makeText(context, "Lat==>" + latitude + " Lng==>" + longitude + "", Toast.LENGTH_LONG).show();
            // \n is for new line

        } else {

            gps.showSettingsAlert();
        }

        Intent i=getIntent();
        type=i.getStringExtra("type");
        edt_mobile.setText(i.getStringExtra("mob"));
        edt_mobile.setEnabled(false);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_pwd.getText().toString().trim().equals(edt_cfm_pwd.getText().toString().trim())) {
                    advocateRegister();
                }else{
                    if(S.isValidEditText(edt_pwd,"Re Enter Password")) return;
                    if(S.isValidEditText(edt_cfm_pwd,"Re Enter Confirm Password")) return;
                }
            }
        });
    }

    private void advocateRegister() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);
      //  pDialog.show();
        //PREPARE INPUT/REQUEST PARAMETERS
        UserRegisterInput input = new UserRegisterInput(
                edt_name.getText().toString().trim(),
                "",
                edt_pwd.getText().toString().trim(),
                edt_email.getText().toString().trim(),
                edt_mobile.getText().toString().trim(),
                edt_addr.getText().toString().trim(),
                edt_lic.getText().toString().trim(),
                latitude,
                longitude,
                "advocate"
        );
        S.hideSoftKeyboard(Advocate_register.this);
        //CALL NOW
        webServices.userRegister(input)
                .enqueue(new Callback<UserRegisterResult>() {
                    @Override
                    public void onResponse(Call<UserRegisterResult> call, Response<UserRegisterResult> response) {
                      //  if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UserRegisterResult result = response.body();
                        if (result.is_success) {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, Advocate.class);
                           /* intent.putExtra("fn", "KRS");
                            setResult(Activity.RESULT_OK, intent);*/
                           startActivity(intent);
                           // finish();
                        } else {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<UserRegisterResult> call, Throwable t) {
                  //      if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();

                    }
                });
    }
    }

   /* public void submit(View v) {
        Intent k = new Intent(Advocate_register.this, Registration.class);
        startActivity(k);
    }*/
