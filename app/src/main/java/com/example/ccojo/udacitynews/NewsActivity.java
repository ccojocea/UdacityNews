package com.example.ccojo.udacitynews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccojo on 5/23/2018.
 */

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {
    /**
     * Tag for log messages
     */
    private static final String TAG = NewsActivity.class.getName() + "DEBUG";

    /**
     * Static variables/constants related to the query
     */
    private static final String GUARDIAN_API_URL = "http://content.guardianapis.com/search"; //NON-NLS
    private static final String FROM_DATE = "from-date"; //NON-NLS
    private static final String FROM_DATE_VALUE = "2018-01-01";
    private static final String SHOW_FIELDS = "show-fields"; //NON-NLS
    private static final String SHOW_THUMB_FIELDS_VALUE = "bodyText,thumbnail,byline"; //NON-NLS
    private static final String SHOW_NO_THUMB_FIELDS_VALUE = "bodyText,byline"; //NON-NLS
    private static final String SHOW_TAGS = "show-tags"; //NON-NLS
    private static final String SHOW_TAGS_VALUE = "contributor"; //NON-NLS
    private static final String PRE_API_KEY = "api-key"; //NON-NLS
    private static final String ORDER_BY = "order-by"; //NON-NLS
    private static final String PAGE_SIZE = "page-size"; //NON-NLS
    private static final String SECTION = "section"; //NON-NLS
    private static final String DEFAULT_SECTION_VALUE = "news"; //NON-NLS
    private static final String LANG = "lang"; //NON-NLS
    private static String sectionValue = DEFAULT_SECTION_VALUE;

    private ConnectivityManager cm;

    /**
     * Constant value for the news loader ID.
     */
    private static final int LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * Layout views
     */
    private TextView emptyView;
    private ProgressBar loadingIndicator;
    private SwipeRefreshLayout swipeRefreshEmpty;
    private SwipeRefreshLayout swipeRefreshList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Check extras bundle to get the section to be displayed
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sectionValue = extras.getString(MainActivity.SECTION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        // Set the empty state TextView onto the ListView
        emptyView = findViewById(R.id.empty_list_view);
        swipeRefreshEmpty = findViewById(R.id.swiperefresh_empty);
        swipeRefreshList = findViewById(R.id.swiperefresh_listview);
        newsListView.setEmptyView(swipeRefreshEmpty);

        // Create a new {@link ArrayAdapter} of news
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(intent);
            }
        });

        //call the swipe refresh methods
        onCreateSwipeToRefresh(swipeRefreshList);
        onCreateSwipeToRefresh(swipeRefreshEmpty);

        //Connection stuff following
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

        if (isConnected) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            Log.d(TAG, "onCreate: initloader");

            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {

            Log.d(TAG, "onCreate: no internet view");

            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }
    }

    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(this);

        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light
        );
    }

    @Override
    public void onRefresh() {

        Log.d(TAG, "onRefresh: ");

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            if(mAdapter.isEmpty()) {
                Log.d(TAG, "onRefresh: restartLoader");
                getLoaderManager().restartLoader(LOADER_ID, null, this);    
            } else {
                Log.d(TAG, "onRefresh: initLoader");
                getLoaderManager().initLoader(LOADER_ID, null, this);    
            }
        } else {
            Toast.makeText(this, R.string.no_internet_refresh, Toast.LENGTH_SHORT).show();
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);

            Log.d(TAG, "onRefresh: no internet view");

            if (swipeRefreshList.isRefreshing()) {
                swipeRefreshList.setRefreshing(false);
            }

            if (swipeRefreshEmpty.isRefreshing()) {
                swipeRefreshEmpty.setRefreshing(false);
            }
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        // Get preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String itemsPerPage = sharedPrefs.getString(getString(R.string.settings_items_per_page_key), getString(R.string.settings_items_per_page_default));
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String language = sharedPrefs.getString(getString(R.string.settings_language_key), getString(R.string.settings_language_default));
        boolean showImages = sharedPrefs.getBoolean(getString(R.string.settings_thumbnails_key), true);

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter(SECTION, sectionValue);
        uriBuilder.appendQueryParameter(SHOW_TAGS, SHOW_TAGS_VALUE);
        uriBuilder.appendQueryParameter(LANG, language);
        uriBuilder.appendQueryParameter(PAGE_SIZE, itemsPerPage);
        if(showImages) {
            uriBuilder.appendQueryParameter(SHOW_FIELDS, SHOW_THUMB_FIELDS_VALUE);
        } else {
            uriBuilder.appendQueryParameter(SHOW_FIELDS, SHOW_NO_THUMB_FIELDS_VALUE);
        }
        uriBuilder.appendQueryParameter(ORDER_BY, orderBy);
        if(orderBy.equals(getString(R.string.settings_order_by_relevance_value))) {
            uriBuilder.appendQueryParameter(FROM_DATE, FROM_DATE_VALUE);
        }
        uriBuilder.appendQueryParameter(PRE_API_KEY, BuildConfig.GUARDIAN_NEWS_API_KEY);

        Log.d(TAG, "onCreateLoader uriBuild: " + uriBuilder.toString()); //NON-NLS

        // Return the completed uri
        return new NewsLoader(NewsActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        Log.d(TAG, "onLoadFinished: ");

        // Clear the adapter of previous news data
        mAdapter.clear();

        loadingIndicator.setVisibility(View.GONE);

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            Log.d(TAG, "onLoadFinished: isConnected");

            // If there is a valid list of {@link News}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            } else {
                emptyView.setText(R.string.no_news);

                Log.d(TAG, "onLoadFinished: no news view");
            }
        } else {
            Toast.makeText(this, R.string.no_internet_refresh, Toast.LENGTH_SHORT).show();
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);

            Log.d(TAG, "onLoadFinished: no internet view");
        }

        if (swipeRefreshList.isRefreshing()) {
            swipeRefreshList.setRefreshing(false);
        }

        if (swipeRefreshEmpty.isRefreshing()) {
            swipeRefreshEmpty.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        Log.d(TAG, "onLoaderReset: ");

        // Loader reset, clear out our existing data.
        mAdapter.clear();
    }

    @Override
    // Initialises the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options menu specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent (this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

