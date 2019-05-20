package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.CheckMobInput;
import com.example.user.advocate.models.CheckMobResult;
import com.example.user.advocate.models.ResetPasswordInput;
import com.example.user.advocate.models.ResetPasswordResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Otp_Verify extends AppCompatActivity {
    @BindView(R.id.edt_mob)
    EditText edt_mob;
    @BindView(R.id.edt_otp)
    EditText edt_otp;
    @BindView(R.id.edt_new_pwd)
    EditText edt_new_pwd;
    @BindView(R.id.edt_new_cpwd)
    EditText edt_new_cpwd;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_otp_req)
    Button btn_otp_req;
    @BindView(R.id.btn_verify)
    Button btn_verify;
    private Context context;
    public String otp_compare, uuid, type, type1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verify);
        ButterKnife.bind(this);
        context = Otp_Verify.this;

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        type1 = intent.getStringExtra("type1");

        Log.d("TYPE===>>", type);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

        btn_otp_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_mob.getText().toString().trim().length() < 10) {
                    Toast.makeText(context, "Please enter valed 10 digit mobile number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (type1.equals("pwd")) {
                    verifyMobileNO();
                }

                if (type1.equals("reg")) {
                    checkMobileNO();
                }
                otp_compare = S.generateOTP();

                Log.d("OTP===>>", otp_compare);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!S.isValidEditText(edt_otp, "OTP")) return;
                if (type.equals("user") && type1.equals("reg")) {
                    if (edt_otp.getText().toString().trim().equals(otp_compare)) {
                        Log.d("LHS", otp_compare);
                        Log.d("LHS1", edt_otp.getText().toString().trim());

                        Intent intent = new Intent(context, User_Registration.class);
                        intent.putExtra("mob", edt_mob.getText().toString().trim());
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } else if (type.equals("advocate") && type1.equals("reg")) {
                    if (edt_otp.getText().toString().trim().equals(otp_compare)) {
                        Log.d("LHS", otp_compare);
                        Log.d("LHS1", edt_otp.getText().toString().trim());
                        // if(type1.equals("reg")) {
                        Intent intent = new Intent(context, Advocate_register.class);
                        intent.putExtra("mob", edt_mob.getText().toString().trim());
                        intent.putExtra("type", type);

                        startActivity(intent);
                        finish();
                        return;
                    }
                } else if (type.equals("user") && type1.equals("pwd")) {
                    if (edt_otp.getText().toString().trim().equals(otp_compare)) {
                        Log.d("LHS", otp_compare);
                        Log.d("LHS1", edt_otp.getText().toString().trim());
                        edt_new_pwd.setVisibility(View.VISIBLE);
                        edt_new_cpwd.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.VISIBLE);
                        edt_otp.setVisibility(View.GONE);
                        btn_verify.setVisibility(View.GONE);
                        edt_mob.setVisibility(View.GONE);
                        btn_otp_req.setVisibility(View.GONE);
                    }
                } else if (type.equals("advocate") && type1.equals("pwd")) {
                    if (edt_otp.getText().toString().trim().equals(otp_compare)) {
                        Log.d("LHS", otp_compare);
                        Log.d("LHS1", edt_otp.getText().toString().trim());
                        edt_new_pwd.setVisibility(View.VISIBLE);
                        edt_new_cpwd.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.VISIBLE);
                        edt_otp.setVisibility(View.GONE);
                        btn_verify.setVisibility(View.GONE);
                        edt_mob.setVisibility(View.GONE);
                        btn_otp_req.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, "Invalid OTP", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void updatePassword() {
        // pDialog.show();
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        ResetPasswordInput management = new ResetPasswordInput(
                edt_new_pwd.getText().toString().trim(),
                edt_mob.getText().toString().trim()
        );
        S.hideSoftKeyboard(Otp_Verify.this);
        //CALL NOW
        webServices.resetPassword(management)
                .enqueue(new Callback<ResetPasswordResult>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResult> call, Response<ResetPasswordResult> response) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResetPasswordResult result = response.body();
                        if (result.is_success) {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            if (type.equals("user")) {
                                Intent intent = new Intent(context, User.class);
                                startActivity(intent);
                                finish();
                            }
                            if (type.equals("advocate")) {
                                Intent intent = new Intent(context, Advocate.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPasswordResult> call, Throwable t) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                    }
                });
    }

    private void verifyMobileNO() {
        // pDialog.show();
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        CheckMobInput management = new CheckMobInput(
                edt_mob.getText().toString().trim()
                // S.getUserDetails(context).user_name
        );

        S.hideSoftKeyboard(Otp_Verify.this);
        //CALL NOW
        webServices.checkmob(management)
                .enqueue(new Callback<CheckMobResult>() {
                    @Override
                    public void onResponse(Call<CheckMobResult> call, Response<CheckMobResult> response) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        CheckMobResult result = response.body();
                        if (result.is_success) {
                            uuid = result.user_id;
                            S.sendSMS(edt_mob.getText().toString().trim(), "Hi Law-Friendly user, Your OTP is " + otp_compare);
                            edt_otp.setVisibility(View.VISIBLE);
                            btn_verify.setVisibility(View.VISIBLE);
                            edt_mob.setVisibility(View.GONE);
                            btn_otp_req.setVisibility(View.GONE);
//                            btn_Submit.setProgress(100);
                            // Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
//                            S.ShowSuccessDialog(context, "Success", result.msg);

                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckMobResult> call, Throwable t) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                    }
                });
    }


    private void checkMobileNO() {
        // pDialog.show();
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        CheckMobInput management = new CheckMobInput(
                edt_mob.getText().toString().trim()
                // S.getUserDetails(context).user_name
        );

        S.hideSoftKeyboard(Otp_Verify.this);
        //CALL NOW
        webServices.checkmob(management)
                .enqueue(new Callback<CheckMobResult>() {
                    @Override
                    public void onResponse(Call<CheckMobResult> call, Response<CheckMobResult> response) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        CheckMobResult result = response.body();
                        if (!result.is_success) {
                            uuid = result.user_id;
                            S.sendSMS(edt_mob.getText().toString().trim(), "Hi Law-Friendly user, Your OTP is " + otp_compare);
                            edt_otp.setVisibility(View.VISIBLE);
                            btn_verify.setVisibility(View.VISIBLE);
                            edt_mob.setVisibility(View.GONE);
                            btn_otp_req.setVisibility(View.GONE);
//                            btn_Submit.setProgress(100);
                            // Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
//                            S.ShowSuccessDialog(context, "Success", result.msg);

                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckMobResult> call, Throwable t) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                    }
                });
    }
}


    /*public void verify(View v) {
        Intent k = new Intent(Otp_Verify.this, Registration.class);
        startActivity(k);
    }
}*/