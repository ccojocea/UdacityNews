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

final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String TAG = QueryUtils.class.getSimpleName();
    private static final String BODY_TEXT = "bodyText"; //NON-NLS
    private static final String BYLINE = "byline"; //NON-NLS
    private static final String THUMBNAIL = "thumbnail"; //NON-NLS
    private static final String FIELDS = "fields"; //NON-NLS
    private static final String WEB_URL = "webUrl"; //NON-NLS
    private static final String SECTION_NAME = "sectionName"; //NON-NLS
    private static final String WEB_TITLE = "webTitle"; //NON-NLS
    private static final String TYPE = "type"; //NON-NLS
    private static final String ARTICLE = "article"; //NON-NLS
    private static final String RESULTS = "results"; //NON-NLS
    private static final String STATUS = "status"; //NON-NLS
    private static final String RESPONSE = "response"; //NON-NLS
    private static final String OK = "ok"; //NON-NLS
    private static final String WEB_PUBLICATION_DATE = "webPublicationDate"; //NON-NLS
    private static final String GET_REQUEST_METHOD = "GET"; //NON-NLS
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int OK_RESPONSE_CODE = 200;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    static List<News> requestNewsData(String requestUrl) {

        if (requestUrl == null || requestUrl.isEmpty()) {
            return null;
        }

        URL url = createUrl(requestUrl);
        String jsonString = null;

        try {
            jsonString = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "requestNewsData: ", e); //NON-NLS
        }

        return extractNews(jsonString);
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(GET_REQUEST_METHOD); //NON-NLS
            urlConnection.connect();

            if (urlConnection.getResponseCode() == OK_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Url Connection Response Code: " + urlConnection.getResponseCode()); //NON-NLS
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the news JSON results.", e); //NON-NLS
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8")); //NON-NLS
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
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
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Try to parse the json response.
        try {
            //Convert json response String into a JSONObject
            JSONObject rootJsonObject = new JSONObject(json);

            //Extract “response” JSONObject
            JSONObject responseJsonObject = rootJsonObject.getJSONObject(RESPONSE);

            //Check Guardian API JSON status
            String jsonStatus = responseJsonObject.getString(STATUS);
            if (!jsonStatus.equals(OK)) {
                Log.d(TAG, "extractNews status: " + jsonStatus); //NON-NLS
                return null;
            }

            JSONArray resultsJsonArray = responseJsonObject.getJSONArray(RESULTS);

            //Loop through each result in the array
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                //Get news JSONObject at position i
                JSONObject newsJsonObject = resultsJsonArray.getJSONObject(i);

                //Get type
                String type = newsJsonObject.getString(TYPE);
                if (!type.equals(ARTICLE)) {
                    continue;
                }

                //Get webTitle
                String webTitle = newsJsonObject.optString(WEB_TITLE);

                //Get sectionName
                String sectionName = newsJsonObject.optString(SECTION_NAME);

                //Get webUrl
                String webUrl = newsJsonObject.optString(WEB_URL);

                //Get webPublicationDate
                String webPublicationDate = newsJsonObject.optString(WEB_PUBLICATION_DATE);

                //Get fields JSONObject - byline, body and thumbnail
                String thumbnailUrl = null;
                String bodyText = "";
                String byline = "";
                try {
                    //Get “fields” JSONObject
                    JSONObject fieldsJsonObject = newsJsonObject.getJSONObject(FIELDS);

                    //Get the optional thumbnail
                    try {
                        thumbnailUrl = fieldsJsonObject.getString(THUMBNAIL);
                    } catch (JSONException e) {
                        Log.d(TAG, "Get thumbnail: " + e); //NON-NLS
                    }

                    //Get body text
                    bodyText = fieldsJsonObject.optString(BODY_TEXT);

                    //Get byline
                    byline = fieldsJsonObject.optString(BYLINE);
                } catch (JSONException e) {
                    Log.e(TAG, "Problem getting the \"fields\" JSONObject ", e); //NON-NLS
                }

                //Create News java object
                //Add news to list of news
                news.add(new News(webTitle, sectionName, webPublicationDate, webUrl, thumbnailUrl, byline, bodyText));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, "Problem parsing the news JSON results: ", e); //NON-NLS
        }

        // Return the list of news
        return news;
    }

    //return URL object from String
    private static URL createUrl(String inputUrl) {
        URL url = null;

        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error creating URL ", e); //NON-NLS
        }

        return url;
    }
}
