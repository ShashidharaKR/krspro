package com.example.user.advocate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.user.advocate.models.UserLoginInput;
import com.example.user.advocate.models.UserLoginResult;
import com.example.user.advocate.models.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Advocate extends AppCompatActivity {
    @BindView(R.id.edt_uname)
    EditText edt_uname;
    @BindView(R.id.edt_pwd)
    EditText edt_pwd;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_reg)
    Button btn_reg;
    private Context context;
    // private SweetAlertDialog pDialog;
    public String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocate);
        ButterKnife.bind(this);
        context = Advocate.this;
        //

        Intent i = getIntent();
        type = i.getStringExtra("type");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!S.isValidEditText(edt_uname, "UserName")) return;
                if (!S.isValidEditText(edt_pwd, "Password")) return;
                userLogin();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(Advocate.this, Otp_Verify.class);
                k.putExtra("type", "advocate");
                k.putExtra("type1", "reg");
                startActivity(k);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(Advocate.this, Otp_Verify.class);
                k.putExtra("type", "advocate");
                k.putExtra("type1", "pwd");
                startActivity(k);
            }
        });

    }

    private void userLogin() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);
        // pDialog.show();
        //PREPARE INPUT/REQUEST PARAMETERS
        UserLoginInput Input = new UserLoginInput(
                edt_uname.getText().toString().trim(),
                edt_pwd.getText().toString().trim()
        );
        S.hideSoftKeyboard(Advocate.this);
        //CALL NOW
        webServices.userLogin(Input)
                .enqueue(new Callback<UserLoginResult>() {
                    @Override
                    public void onResponse(Call<UserLoginResult> call, Response<UserLoginResult> response) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UserLoginResult result = response.body();
                        if (result.is_success) {
                            //      user=result.user;
                            S.saveUserLoginData(context, result.user);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, Advocate_Profile.class);
                            intent.putExtra("id", result.user.id);
                            intent.putExtra("type", "advocate");
                            startActivity(intent);
                            finish();
                           /* intent.putExtra("fn", "KRS");
                            setResult(Activity.RESULT_OK, intent);
                            finish();*/
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserLoginResult> call, Throwable t) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();

                    }
                });
    }
}