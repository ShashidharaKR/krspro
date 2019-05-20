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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.activities.Login;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.interfaces.OnDateSetInterface;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.UpdateAdvocateInput;
import com.example.user.advocate.models.UpdateAdvocateResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.GPSTracker;



public class Advocate_Profile extends AppCompatActivity {
    @BindView(R.id.sp_cases)
    Spinner sp_cases;
    @BindView(R.id.edt_exp)
    EditText edt_exp;

    @BindView(R.id.txt_dob)
    TextView txt_dob;
    @BindView(R.id.edt_lat)
    EditText edt_lat;
    @BindView(R.id.edt_lng)
    EditText edt_lng;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_logout)
    Button btn_logout;

    private List<String> caseIdList=new ArrayList<>();
    private List<String> caseNameList=new ArrayList<>();
    //private Spinner spinner;
    //private String[] cases;
    private String type,id;
    private Context context;
    String latitude;
    String longitude;
    int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocate_profile);
        ButterKnife.bind(this);
        context=Advocate_Profile.this;
//        edt_exp.setText(S.getUserDetails(context).exp);
        caseIdList.add("0");
        caseIdList.add("1");
        caseIdList.add("2");
        caseIdList.add("3");

        caseNameList.add("Select type");
        caseNameList.add("Civil");
        caseNameList.add("Criminal");
        caseNameList.add("Both");
        sp_cases.setSelection(0);

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("ADVOCATE");
        setSupportActionBar(t);
       // sp_cases.setBackgroundColor(Integer.parseInt("#FFF2C6C6"));
        //cases = getResources().getStringArray(R.array.cases);
      //  spinner = (Spinner)findViewById(R.id.sp_cases);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,caseNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cases.setAdapter(dataAdapter);

        sp_cases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               /* if(i!=selectedItem){
                    sp_cases.get(i);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());

            Toast.makeText(context, "Lat==>" + latitude + " Lng==>" + longitude + "", Toast.LENGTH_LONG).show();
            // \n is for new line

        } else {

            gps.showSettingsAlert();
        }

        edt_lat.setEnabled(false);
        edt_lat.setText(latitude);
        edt_lng.setEnabled(false);
        edt_lng.setText(longitude);

        Intent i=getIntent();
        id=i.getStringExtra("id");
        type=i.getStringExtra("type");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!S.isValidEditText(edt_exp,"Experience"))return;
                updateAdvocate();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });

        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_dob.setText(S.getDateYYMMDD());
                S.setDateDefault(context,"",txt_dob,txt_dob,0,true,false, new  OnDateSetInterface(){

                    @Override
                    public void OnDateSet(String date) {
                        txt_dob.setText(date);
                    }
                });
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

    private void updateAdvocate() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        UpdateAdvocateInput input = new UpdateAdvocateInput(
                id,
                caseNameList.get(sp_cases.getSelectedItemPosition()),
                edt_exp.getText().toString().trim(),
                txt_dob.getText().toString().trim(),
                edt_lat.getText().toString().trim(),
                edt_lng.getText().toString().trim(),
                type
        );
        //CALL NOW
        webServices.updateUser(input)
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
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                          //  S.ShowSuccessDialogAndBeHere(context,"Success",result.msg);
                            Intent intent = new Intent(context, Advocate_Profile.class);
                            intent.putExtra("id", S.getUserDetails(context).id);
                            intent.putExtra("type", "advocate");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                           // S.ShowErrorDialogAndBeHere(context,"Error",result.msg);
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
        Intent intent=new Intent(context,Advocate_Profile.class);
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
