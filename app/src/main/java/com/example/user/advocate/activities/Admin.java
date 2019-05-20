package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.advocate.R;
import com.example.user.advocate.logics.S;

import butterknife.BindView;
import butterknife.ButterKnife;



public class Admin extends AppCompatActivity {
    @BindView(R.id.edt_uname)
    EditText edt_uname;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.edt_pwd)
    EditText edt_pwd;
    @BindView(R.id.btn_login)
    Button btn_login;
    private Context context;
 //   private SweetAlertDialog pDialog;
    public String type,uname,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        ButterKnife.bind(this);
        context=Admin.this;
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading...");

        uname="admin";
        pwd="admin";
        Intent i=getIntent();
        type= i.getStringExtra("type");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(edt_uname.getText().toString().trim().equals("admin")&&edt_pwd.getText().toString().trim().equals("admin")){
                 Intent intent=new Intent(context,AdminHome.class);
                 startActivity(intent);
                 finish();

            }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k=new Intent(Admin.this,Advocate_register.class);
                k.putExtra("type","advocate");
                startActivity(k);
            }
        });

    }

}
