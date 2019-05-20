package com.example.user.advocate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.Advocates;
import com.example.user.advocate.models.NetworkAdvocates;

import java.util.ArrayList;
import java.util.List;

import services.GPSTracker;


public class AdvocateAdapter extends RecyclerView.Adapter<AdvocateAdapter.MyViewHolder> {
    //private SweetAlertDialog pDialog;
    public List<NetworkAdvocates> advocates = new ArrayList<>();
    private Context context;
    String pid, mob;
    String check_repeat = "Shash";
    public Double lat, lng, type, latitude, longitude;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_exp, txt_email, txt_case, txt_mobile, txt_book;
        public LinearLayout row_details;

        public MyViewHolder(View view) {
            super(view);

            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_exp = (TextView) view.findViewById(R.id.txt_exp);
            txt_email = (TextView) view.findViewById(R.id.txt_email);
            txt_case = (TextView) view.findViewById(R.id.txt_case);
            txt_mobile = (TextView) view.findViewById(R.id.txt_mobile);
            txt_book = (TextView) view.findViewById(R.id.txt_book);
            row_details = (LinearLayout) view.findViewById(R.id.row_details);
        }
    }

    public AdvocateAdapter(Context context, List<NetworkAdvocates> advocates) {
        this.advocates = advocates;
        this.context = context;
    }

    @Override
    public AdvocateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list, parent, false);
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading...");
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            //  Toast.makeText(context, "Lat==>" + latitude + " Lng==>" + longitude + "", Toast.LENGTH_LONG).show();
            // \n is for new line

        } else {

            gps.showSettingsAlert();
        }

        return new AdvocateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdvocateAdapter.MyViewHolder holder, int position) {
        final NetworkAdvocates advocate = advocates.get(position);

        lat = Double.parseDouble(advocate.getLattitude());
        lng = Double.parseDouble(advocate.getLongitude());


        float[] dist = new float[1];

        Location.distanceBetween(latitude, longitude, lat, lng, dist);

        if (dist[0] / 2000 < 1) { //Distance in meter 1000 ie 1Kms

            holder.txt_name.setText("Name :  " + advocate.getName());
            holder.txt_exp.setText("Exp : " + advocate.getExperience() + "yrs");
            holder.txt_email.setText("Email :    " + advocate.getEmail());
            holder.txt_case.setText("Case :  " + advocate.getCases_handled());
            holder.txt_mobile.setText("Call - " + advocate.getMobile());

            Log.d("NAME", advocate.getName() + "");

            Log.d("C_LAT", latitude + "");
            Log.d("C_LNG", longitude + "");
            Log.d("D_LAT", lat + "");
            Log.d("D_LNG", lng + "");

        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.row_details.getLayoutParams();
            params.height = 0;
            params.width = 0;
            holder.row_details.setLayoutParams(params);
            holder.row_details.setVisibility(View.GONE);
        }


        mob = advocate.getMobile();

        holder.txt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSend = "Hi " + advocate.getName() + ", \n" + S.getUserDetails(context).name + " is requesting service from you";
                String number = mob;
                S.sendSMS(advocate.getMobile(), messageToSend);
                SmsManager.getDefault().sendTextMessage(mob, null, messageToSend, null,null);
               // S.ShowSuccessDialog(context, "Success", "Requested successfully");
                 Toast.makeText(context,"Request sent to the Advocate",Toast.LENGTH_LONG).show();
            }
        });
//        holder.txt_mobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse(mob));
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return advocates.size();
    }


}
