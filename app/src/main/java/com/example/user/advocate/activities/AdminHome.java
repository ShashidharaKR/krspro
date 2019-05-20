package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.AdvocateNewtorkInput;
import com.example.user.advocate.models.Advocates;
import com.example.user.advocate.models.GetAdvocatesResult;
import com.example.user.advocate.models.UpdateAdvocateResult;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminHome extends AppCompatActivity {
    private Context context;
    public List<Advocates> advocates=new ArrayList<>();

    private List<String> advIdList=new ArrayList<>();
    private List<String> advNameList=new ArrayList<String>();

    @BindView(R.id.sp_advocate)
    SearchableSpinner sp_advocate;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_cases)
    TextView txt_case;
    @BindView(R.id.txt_exp)
    TextView txt_exp;
    @BindView(R.id.btn_logout)
    Button btn_logout;
    public String add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        ButterKnife.bind(this);
        context = AdminHome.this;
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("ADMIN");
        setSupportActionBar(t);
        add="YES";
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToNetwork();
            }
        });
        getAdvocates();

        sp_advocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                if(position!=0){
                    Advocates a=advocates.get(position-1);
                    txt_name.setVisibility(View.VISIBLE);
                    txt_email.setVisibility(View.VISIBLE);
                    txt_mobile.setVisibility(View.VISIBLE);
                    txt_exp.setVisibility(View.VISIBLE);
                    txt_case.setVisibility(View.VISIBLE);
                    txt_name.setText("Name:          "+a.getName());
                    txt_email.setText("Email:           "+a.getEmail());
                    txt_mobile.setText("Mobile:         "+a.getMobile());
                    txt_case.setText("Cases:          "+a.getCases_handled());
                    txt_exp.setText("Experience:  "+a.getExperience());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);//Menu Resource, Menu
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                onLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void getAdvocates() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //CALL NOW
        webServices.getAdvocates()
                .enqueue(new Callback<GetAdvocatesResult>() {
                    @Override
                    public void onResponse(Call<GetAdvocatesResult> call, Response<GetAdvocatesResult> response) {
                       // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        GetAdvocatesResult result = response.body();
                        if (result.success) {
                            advocates=result.advocates;
                          //  Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();

                            advNameList.add("Select Advocates");
                            advIdList.add("0");

                            for (int i = 0; i < result.advocates.size(); i++) {
                                advIdList.add(result.advocates.get(i).getId());
                                advNameList.add(result.advocates.get(i).getName());
                            }
                            S.setSpinnerAdapter(context, sp_advocate, advNameList);

                            sp_advocate.setSelection(0);

                        } else {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<GetAdvocatesResult> call, Throwable t) {
                      //  if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();

                    }
                });
    }


    private void addToNetwork() {
        if (advIdList.isEmpty()){
            Toast.makeText(context, "At least select one advocate", Toast.LENGTH_LONG).show();
            return;
        }
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        AdvocateNewtorkInput input= new AdvocateNewtorkInput(
          advIdList.get(sp_advocate.getSelectedItemPosition()),
          add,
          "advocate"
        );
        //CALL NOW
        webServices.addToNetwork(input)
                .enqueue(new Callback<UpdateAdvocateResult>() {
                    @Override
                    public void onResponse(Call<UpdateAdvocateResult> call, Response<UpdateAdvocateResult> response) {
                        // if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UpdateAdvocateResult result = response.body();
                        if (result.is_success) {
                          // S.ShowSuccessDialogAndBeHere(context,"Success",result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(context,AdminHome.class);
                            startActivity(intent);
                            finish();

                        } else {
                          //  S.ShowErrorDialogAndBeHere(context, "Error",result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<UpdateAdvocateResult> call, Throwable t) {
                        //  if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();

                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(context,AdminHome.class);
        startActivity(intent);
        finish();
    }

    public void onLogout() {
        S.userLogout(context);
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
