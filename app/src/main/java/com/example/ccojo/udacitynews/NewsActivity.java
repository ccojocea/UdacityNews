package com.example.ccojo.udacitynews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener{
    /** Tag for log messages */
    private static final String TAG = NewsActivity.class.getName();

    /**
     * Static variables/constants related to the query
     * The ones commented out are to be used in the following version of the app
     */
    //private static final String SHOW_FIELDS = "&show-fields=all";
    //private static final String FROM_DATE = "&from-date="; //NON-NLS
    //private static final String TO_DATE = "&to-date="; //NON-NLS
    private static final String SHOW_FIELDS = "&show-fields=bodyText%2Cthumbnail%2Cbyline"; //NON-NLS
    private static final String SHOW_TAGS = "&show-tags=contributor"; //NON-NLS
    private static final String API_URL = "http://content.guardianapis.com/search?"; //NON-NLS
    private static final String API_KEY = "ca842212-fe7c-4a30-b602-ce111cb86204"; //NON-NLS
    private static final String PRE_API_KEY = "&api-key="; //NON-NLS
    private static final String ORDER_BY = "&order-by=newest"; //NON-NLS
    private static final String PAGE_SIZE = "&page-size="; //NON-NLS
    //default page size
    private static final int PAGESIZE = 25;
    private static String queryUrl;

    private ConnectivityManager cm;

    /** Constant value for the news loader ID. */
    private static final int LOADER_ID = 1;

    /** Adapter for the list of news */
    private NewsAdapter mAdapter;

    /** Layout views */
    private TextView emptyView;
    private ProgressBar loadingIndicator;
    private SwipeRefreshLayout swipeRefreshEmpty;
    private SwipeRefreshLayout swipeRefreshList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**build current date as String to pass in the url
         Calendar calendar = Calendar.getInstance();
         String cDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
         String cMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
         String cYear = String.valueOf(calendar.get(Calendar.YEAR));
         String currentDay = cYear + "-" + cMonth + "-" + cDay;
         */

        //Check extras bundle to get the section to be displayed
        String sectionURL = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            sectionURL = extras.getString(MainActivity.SECTION);
        }
        queryUrl = API_URL + sectionURL + SHOW_TAGS + PAGE_SIZE + PAGESIZE + SHOW_FIELDS + ORDER_BY + PRE_API_KEY + API_KEY;

        Log.d(TAG, "onCreate: " + queryUrl); //NON-NLS

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

        if(isConnected){
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
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
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            Toast.makeText(this, R.string.no_internet_refresh, Toast.LENGTH_SHORT).show();
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);

            if (swipeRefreshList.isRefreshing()){
                swipeRefreshList.setRefreshing(false);
            }

            if(swipeRefreshEmpty.isRefreshing()) {
                swipeRefreshEmpty.setRefreshing(false);
            }
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(NewsActivity.this, queryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Clear the adapter of previous news data
        mAdapter.clear();

        loadingIndicator.setVisibility(View.GONE);

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        } else {
            emptyView.setText(R.string.no_news);
        }


        if (swipeRefreshList.isRefreshing()){
            swipeRefreshList.setRefreshing(false);
        }

        if(swipeRefreshEmpty.isRefreshing()) {
            swipeRefreshEmpty.setRefreshing(false);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, clear out our existing data.
        mAdapter.clear();
    }
}

