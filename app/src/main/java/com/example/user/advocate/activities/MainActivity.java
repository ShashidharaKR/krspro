package com.example.user.advocate.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.advocate.R;
import com.example.user.advocate.logics.S;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private static final int STORAGE_PERMISSION_CODE = 1;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3 * 1000);
                    if (S.isUserLoggedIn(context)) {
                        moveNext();
                    } else {
                        Intent i = new Intent(MainActivity.this, InitialScrn.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        };
        background.start();


    }

    private void moveNext() {
        type = S.getUserDetails(context).type;

        if (type.equals("user")) {
            Intent intent = new Intent(context, Usr_Log.class);
            intent.putExtra("type", "user");
            startActivity(intent);
            finish();
        }
        if (type.equals("advocate")) {
            Intent intent = new Intent(context, Advocate_Profile.class);
            intent.putExtra("type", "advocate");
            startActivity(intent);
            finish();
        }
        if (type.equals("admin")) {
            Intent intent = new Intent(context, AdminHome.class);
            intent.putExtra("type", "admin");
            startActivity(intent);
            finish();

        }
    }
}
