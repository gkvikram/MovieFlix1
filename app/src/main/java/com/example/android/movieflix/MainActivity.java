package com.example.android.movieflix;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.GridItemClickListener {
    private GridView mgridView;
    private ProgressBar loadingIndicator;
    private TextView merrorDisplay;
    private String jsonResponse;
    private List<MoviePoster> moviePosters;
    MovieGridAdapter mgridAdapter;
    JSONArray results= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgridView=(GridView)findViewById(R.id.movies_gridview);
        loadingIndicator=(ProgressBar)findViewById(R.id.loading_indicator);
        merrorDisplay=(TextView)findViewById(R.id.error_message_display);
        makeMovieDBQuery("popular");


    }


    private void showLoadingIndicator(){
        mgridView.setVisibility(View.INVISIBLE);
        merrorDisplay.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }
    private void showGridView(){
        loadingIndicator.setVisibility(View.INVISIBLE);
        merrorDisplay.setVisibility(View.INVISIBLE);
        mgridView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        loadingIndicator.setVisibility(View.INVISIBLE);
        mgridView.setVisibility(View.INVISIBLE);
        merrorDisplay.setVisibility(View.VISIBLE);

    }

    public void makeMovieDBQuery(String sortType){
        URL msearchUrl=NetworkUtils.buildMovieApiURL(sortType);
        MoviedbQueryTask mqtask=new MoviedbQueryTask(this);
        mqtask.execute(msearchUrl);
    }

    public class MoviedbQueryTask extends AsyncTask<URL,Void,String>{
        Context mcontext;

        public MoviedbQueryTask(Context context){
            super();
            mcontext=context;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            moviePosters=new ArrayList<MoviePoster>();
            showLoadingIndicator();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl=params[0];
            String searchResults=null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if(searchResults!=null && !searchResults.equals("")){
                jsonResponse=searchResults;
                JSONObject searchResponse= null;
                try {
                    searchResponse = new JSONObject(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {

                    results = searchResponse.getJSONArray("results");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject js;
                String posterpath;
                //String backdroppath;
                MoviePoster mp;
                URL posterUrl;
                for(int i=0;i<results.length();i++){

                    try {
                        js=results.getJSONObject(i);
                        posterpath=js.getString("poster_path");

                        if(posterpath!=null&&!posterpath.equals("")) {
                            posterUrl = NetworkUtils.buildImageUrl(posterpath.substring(1));
                            mp = new MoviePoster(posterUrl.toString());
                            moviePosters.add(mp);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                }
                mgridAdapter=new MovieGridAdapter(mcontext,moviePosters,moviePosters.size());
                showGridView();
                mgridView.setAdapter(mgridAdapter);

            }else{
                showErrorMessage();
            }
        }
    }
    @Override
    public void onGridItemClick(int clickedItemIndex) {
        JSONObject itemDetails=null;
        try {
            itemDetails=results.getJSONObject(clickedItemIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

            String details = itemDetails.toString();

        Context context=MainActivity.this;
        Class detailViewActivity= DetailViewActivity.class;
        Intent startDetailViewIntent=new Intent(context,detailViewActivity);
        startDetailViewIntent.putExtra(Intent.EXTRA_TEXT,details);
        startActivity(startDetailViewIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.popular_search) {
            makeMovieDBQuery("popular");
            return true;
        }else if(itemThatWasClickedId==R.id.toprated_search){
             makeMovieDBQuery("top_rated");
            return true;
        }else {

            return super.onOptionsItemSelected(item);
        }
    }



}
