package com.example.android.movieflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailViewActivity extends AppCompatActivity {
private TextView movieTitle;
private ImageView moviePoster;
private TextView releaseDate;
private TextView userRating;
private TextView movieOverview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        movieTitle=(TextView)findViewById(R.id.movie_title);
        moviePoster=(ImageView)findViewById(R.id.movie_poster);
        releaseDate=(TextView)findViewById(R.id.release_date);
        userRating=(TextView)findViewById(R.id.user_rating);
        movieOverview=(TextView)findViewById(R.id.movie_overview);
        movieOverview.setMovementMethod(new ScrollingMovementMethod());//incorporating suggestion on making movie overview scrollable

        Intent intentThatStartedThisActivity = getIntent();
        String resultSent=null;
        String posterpath;
        URL posterUrl;
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            resultSent = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

        }

        try {
            JSONObject details=new JSONObject(resultSent);
            posterpath=details.getString("poster_path");
            posterUrl=NetworkUtils.buildImageUrl(posterpath.substring(1));
            Picasso.with(this).load(posterUrl.toString()).into(moviePoster);
            movieTitle.setText(details.getString("title"));
            releaseDate.setText(details.getString("release_date"));
            String srating=details.getString("vote_average")+"/10";
            userRating.setText(srating);
            movieOverview.setText(details.getString("overview"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
