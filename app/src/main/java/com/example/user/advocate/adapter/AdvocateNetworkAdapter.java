package com.example.user.advocate.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.advocate.R;
import com.example.user.advocate.logics.S;
import com.example.user.advocate.models.NetworkAdvocates;

import java.util.ArrayList;
import java.util.List;

import services.GPSTracker;

public class AdvocateNetworkAdapter extends RecyclerView.Adapter<AdvocateNetworkAdapter.MyViewHolder> {
   // private SweetAlertDialog pDialog;
    public List<NetworkAdvocates> advocates = new ArrayList<>();
    private Context context;

    public Double lat, lng, type, latitude, longitude;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_exp, txt_email, txt_case, txt_mobile, txt_book,map;
        public LinearLayout row_details;

        public MyViewHolder(View view) {
            super(view);

            map = (TextView) view.findViewById(R.id.map);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_exp = (TextView) view.findViewById(R.id.txt_exp);
            txt_email = (TextView) view.findViewById(R.id.txt_email);
            txt_case = (TextView) view.findViewById(R.id.txt_case);
            txt_mobile = (TextView) view.findViewById(R.id.txt_mobile);
            txt_book = (TextView) view.findViewById(R.id.txt_book);
            row_details = (LinearLayout) view.findViewById(R.id.row_details);
        }
    }

    public AdvocateNetworkAdapter(Context context, List<NetworkAdvocates> advocates) {
        this.advocates = advocates;
        this.context = context;


    }

    @Override
    public AdvocateNetworkAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list, parent, false);
        /*GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude =gps.getLongitude();

            Toast.makeText(context, "Lat==>" + latitude + " Lng==>" + longitude + "", Toast.LENGTH_LONG).show();
            // \n is for new line

        } else {

            gps.showSettingsAlert();
        }*/


        return new AdvocateNetworkAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NetworkAdvocates advocate = advocates.get(position);
       /* lat=Double.parseDouble(advocate.getLattitude());
        lng=Double.parseDouble(advocate.getLongitude());
        float[] dist = new float[1];

        Location.distanceBetween(lat, lng, latitude, longitude, dist);

        if (dist[0] / 50000 < 1) {*/
        holder.txt_name.setText("Name : " + advocate.getName());
        holder.txt_exp.setText("Exp : " + advocate.getExperience() + "yrs");
        holder.txt_email.setText("E-mail :    " + advocate.getEmail());
        holder.txt_case.setText("Case : " + advocate.getCases_handled());
        holder.txt_mobile.setText("call - " + advocate.getMobile());
            /*return;
        }*/
holder.map.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String url = "https://www.google.de/maps?q="+advocate.getLattitude()+","+advocate.getLongitude();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
});

        holder.txt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSend = "Hi " + advocate.getName() + ", \n" + S.getUserDetails(context).name + " is requesting service from you";
                S.sendSMS(advocate.getMobile(), messageToSend);
                //SmsManager.getDefault().sendTextMessage(mob, null, messageToSend, null,null);.
                Toast.makeText(context,"Request sent to the Advocate",Toast.LENGTH_LONG).show();
               // S.ShowSuccessDialog(context, "Success", "Requested successfully");
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

