package com.example.ccojo.udacitynews;

/*
  Created by ccojo on 5/23/2018.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news by using an AsyncTask to perform the
 * network request to the given URL.
 */
class NewsLoader extends AsyncTaskLoader<List<News>> {

    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.requestNewsData(mUrl);
    }
}
