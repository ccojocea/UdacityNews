package com.example.ccojo.udacitynews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccojo on 5/23/2018.
 */

public final class QueryUtils {

    /** Tag for the log messages */
    private static final String TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    static List<News> requestNewsData(String requestUrl){
        //TODO Disable this (only here for testing purposes)
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if(requestUrl == null || requestUrl.isEmpty()){
            return null;
        }

        URL url = createUrl(requestUrl);
        String jsonString = null;

        try {
            jsonString = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "requestNewsData: ", e);
        }

        return extractNews(jsonString);
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Url Connection Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder sb = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                sb.append(line);
                line = bufferedReader.readLine();
            }
        }
        return sb.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<News> extractNews(String json) {
        if(TextUtils.isEmpty(json)){
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Try to parse the json response.
        try {
            //Convert json response String into a JSONObject
            JSONObject rootJsonObject = new JSONObject(json);

            //Extract “response” JSONObject
            JSONObject responseJsonObject = rootJsonObject.getJSONObject("response");

            //Check Guardian API JSON status
            String jsonStatus = responseJsonObject.getString("status");
            if(!jsonStatus.equals("ok")){
                Log.d(TAG, "extractNews status: " + jsonStatus);
                return null;
            }

            JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

            //Loop through each result in the array
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                //Get news JSONObject at position i
                JSONObject newsJsonObject = resultsJsonArray.getJSONObject(i);

                //Get webTitle
                String webTitle = newsJsonObject.getString("webTitle");

                //Get sectionName
                String sectionName = newsJsonObject.getString("sectionName");

                //Get webUrl
                String webUrl = newsJsonObject.getString("webUrl");

                //Get webPublicationDate
                String webPublicationDate = newsJsonObject.getString("webPublicationDate");

                //Get “fields” JSONObject
                JSONObject fieldsJsonObject = newsJsonObject.getJSONObject("fields");

                //Get the optional thumbnail
                String thumbnailUrl = null;
                try {
                    thumbnailUrl = fieldsJsonObject.getString("thumbnail");
                } catch (JSONException e) {
                    Log.d(TAG, "Get thumbnail: " + e);
                }

                //Get body text
                String bodyText = fieldsJsonObject.getString("bodyText");

                //Get byline
                String byline = fieldsJsonObject.getString("byline");

                //Create News java object
                //Add news to list of news
                news.add(new News(webTitle, sectionName, webPublicationDate, webUrl, thumbnailUrl, byline, bodyText));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, "Problem parsing the news JSON results: ", e);
        }

        // Return the list of news
        return news;
    }

    //return URL object from String
    private static URL createUrl(String inputUrl){
        URL url = null;

        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error with creating URL ", e);
        }

        return url;
    }

}
