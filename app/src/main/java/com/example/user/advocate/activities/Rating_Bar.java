package com.example.user.advocate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.advocate.R;


public class Rating_Bar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_bar);
        final RatingBar simpleRatingBar=(RatingBar)findViewById(R.id.ratingBar);
        Button submitButton=(Button)findViewById(R.id.button9);

        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String totalStars= "Total Stars::"  + simpleRatingBar.getNumStars();
                String rating= "Rating::" + simpleRatingBar.getRating();
                Toast.makeText(getApplicationContext(),totalStars +"\n" + rating,Toast.LENGTH_LONG).show();
            }
        });
    }

}
