package com.example.android.movieflix;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by shwetagk on 25/07/2017.
 */

public class NetworkUtils {
    final static String MovieDB_BASE_URL =
            "https://api.themoviedb.org/3/discover/movie";
    final static String API_KEY="api_key";
    private static String api_key="88caf39faa54b20e48c409c6072f2523";
    final static String PAGE_NO="page";
    private final static String pageNo="1";
    final static String LANG="language";
    private static String language="en-US";
    final static String PARAM_SORT = "sort_by";
    final static String INCLUDE_ADULT="include_adult";
    private final static String include_adult="false";
    final static String INCLUDE_VIDEO="include_video";
    private final static String include_video="false";

    final static String Image_MovieDB_BASE_URL=" http://image.tmdb.org/t/p/w342";


    static String popularity="popularity.desc";
    static String toprated="vote_count.desc";


    //final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */

    //final static String sortBy = "stars";

    /**
     * Builds the URL used to query MovieDB.
     *
     * @param imageAddress The keyword that will be queried for.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildImageUrl(String imageAddress) {
        Uri builtUri = Uri.parse(Image_MovieDB_BASE_URL).buildUpon()
                .appendPath(imageAddress)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildDiscoverApiURL(String sortType){

        Uri builtUri=Uri.parse(MovieDB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY,api_key)
                        .appendQueryParameter(LANG,language)
                        .appendQueryParameter(PARAM_SORT,sortType)
                        .appendQueryParameter(INCLUDE_ADULT,include_adult)
                        .appendQueryParameter(INCLUDE_VIDEO,include_video)
                        .appendQueryParameter(PAGE_NO,pageNo).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
