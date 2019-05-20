package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.adapter.AdvocateAdapter;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.Advocates;
import com.example.user.advocate.models.GetAdvocatesNetWorkResult;
import com.example.user.advocate.models.GetAdvocatesResult;
import com.example.user.advocate.models.NetworkAdvocates;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.GPSTracker;


public class Usr_Log extends AppCompatActivity {
    private Context context;
    @BindView(R.id.textView)
    TextView textView;
    private RecyclerView recyclerView;
    private AdvocateAdapter mDeptAdapter;
    public List<NetworkAdvocates> advocates = new ArrayList<>();
    public List<String> adv = new ArrayList<>();
    public Double lat, lng, type, latitude, longitude;
    public String name, email, mobile, exp, cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_log);
        ButterKnife.bind(this);
        context = Usr_Log.this;
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("USER");
        setSupportActionBar(t);
        textView.setText("Advocate Near To Your Location");

        getAdvocates();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stocks);
        mDeptAdapter = new AdvocateAdapter(context, advocates);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mDeptAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);//Menu Resource, Menu
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                Intent intent = new Intent(context, Advocate_network.class);
                startActivity(intent);
                return true;
            case R.id.menu_item2:
                onLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getAdvocates() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        S.hideSoftKeyboard(Usr_Log.this);
        //CALL NOW
        webServices.getAdvocatesInNetwork()
                .enqueue(new Callback<GetAdvocatesNetWorkResult>() {
                    @Override
                    public void onResponse(Call<GetAdvocatesNetWorkResult> call, Response<GetAdvocatesNetWorkResult> response) {
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        GetAdvocatesNetWorkResult result = response.body();
                        if (result.success) {
                            advocates = result.advocates;
//                            for (int i = 0; i < advocates.size(); i++) {
//                                lat = Double.parseDouble(advocates.get(i).getLattitude());
//                                lng = Double.parseDouble(advocates.get(i).getLongitude());
//                                name = advocates.get(i).getName();
//                                exp = advocates.get(i).getExperience();
//                                cases = advocates.get(i).getCases_handled();
//                                mobile = advocates.get(i).getMobile();
//                                email = advocates.get(i).getEmail();
//                            }

                            Log.d("LHS", String.valueOf(advocates));
                            mDeptAdapter = new AdvocateAdapter(context, advocates);
                            recyclerView.setAdapter(mDeptAdapter);
                        } else {
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetAdvocatesNetWorkResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                    }
                });
    }


    public void onLogout() {
        S.userLogout(context);
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

