package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.adapter.AdvocateAdapter;
import com.example.user.advocate.adapter.AdvocateNetworkAdapter;
import com.example.user.advocate.api.Api;
import com.example.user.advocate.api.WebServices;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.Advocates;
import com.example.user.advocate.models.GetAdvocatesNetWorkResult;
import com.example.user.advocate.models.GetAdvocatesResult;
import com.example.user.advocate.models.NetworkAdvocates;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Advocate_network extends AppCompatActivity {
    private Context context;
    @BindView(R.id.textView)
    TextView textView;
    private RecyclerView recyclerView;
    private AdvocateNetworkAdapter mDeptAdapter;
    public List<NetworkAdvocates> advocates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_log);
        ButterKnife.bind(this);
        context = Advocate_network.this;
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        //tool_bar_title.setText("DASHBOARD");
        //setSupportActionBar(t);
        t.setTitle("USER");
        setSupportActionBar(t);

        textView.setText("Advocate In Network");

        getAdvocates();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stocks);
        mDeptAdapter = new AdvocateNetworkAdapter(context, advocates);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mDeptAdapter);
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

        S.hideSoftKeyboard(Advocate_network.this);
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
                            Log.d("LHS", String.valueOf(advocates));
                            mDeptAdapter = new AdvocateNetworkAdapter(context, advocates);
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

    /*@Override
    protected void onRestart() {
        super.onRestart();
    }*/

    public void onLogout() {
        S.userLogout(context);
        Intent intent = new Intent(context, User.class);
        startActivity(intent);
        finish();
    }
    /*public void next(View v) {
        Intent k = new Intent(Usr_Log.this, Rating_Bar.class);
        startActivity(k);
    }*/
}

