package com.sandesh.android.newstoday;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    private static final int LOADER_ID = 1;

    private static final String API_URL = "https://content.guardianapis.com/search";

    private ArticleAdapter mAdapter;

    private View mEmptyStateView;
    private View mLoadingIndicator;
    private TextView mEmptyStateTextView;

    private String mNoInternetMsg;
    private String mNoArticlesMsg;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Strings
        mNoInternetMsg = getString(R.string.no_internet_connection);
        mNoArticlesMsg = getString(R.string.no_articles);

        // Views
        ListView newsList = findViewById(R.id.news_items_view);
        mEmptyStateView = findViewById(R.id.empty_state_view);
        mEmptyStateTextView = findViewById(R.id.empty_text_view);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        // Adapter
        mAdapter = new ArticleAdapter(this, new ArrayList<>(), Bookmarks.getInstance());
        newsList.setAdapter(mAdapter);

        // Empty view
        newsList.setEmptyView(mEmptyStateView);

        // Click -> open article
        newsList.setOnItemClickListener((parent, view, position, id) -> {
            Article currentArticle = mAdapter.getItem(position);
            if (currentArticle != null) {
                Utils.openWebsite(NewsActivity.this, currentArticle.getArticleUrl());
            }
        });

        // Pull to refresh
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.clear();
            loadArticles(true); // refresh
        });

        // Initial load
        loadArticles(false);
    }

    private void loadArticles(boolean isRefresh) {
        boolean isConnected = isNetworkConnected();

        if (isConnected) {
            // Hide empty state (if any)
            mEmptyStateView.setVisibility(View.GONE);

            // Show loading indicator only for first load (optional)
            if (!isRefresh) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            if (!isRefresh) {
                LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
            } else {
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this);
            }
        } else {
            // No internet
            mLoadingIndicator.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);

            mEmptyStateTextView.setText(mNoInternetMsg);
            mEmptyStateView.setVisibility(View.VISIBLE);

            Log.e(LOG_TAG, "No internet connection.");
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities caps = cm.getNetworkCapabilities(network);
            return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            @SuppressWarnings("deprecation")
            boolean connected = info != null && info.isConnected();
            return connected;
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        // Preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String selectedArticleType = sharedPrefs.getString(
                getString(R.string.settings_article_type_key),
                getString(R.string.settings_article_type_default)
        );

        String numberOfArticles = sharedPrefs.getString(
                getString(R.string.settings_number_of_articles_key),
                getString(R.string.settings_number_of_articles_default)
        );

        // Build URL
        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("section", selectedArticleType);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numberOfArticles);
        uriBuilder.appendQueryParameter("api-key", Utils.getApiKey());

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Stop spinners
        mLoadingIndicator.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        // Clear old data
        mAdapter.clear();

        // Show results or empty state
        if (articles != null && !articles.isEmpty()) {
            mEmptyStateView.setVisibility(View.GONE);
            mAdapter.addAll(articles);
        } else {
            mEmptyStateTextView.setText(mNoArticlesMsg);
            mEmptyStateView.setVisibility(View.VISIBLE);
            Log.e(LOG_TAG, "No articles found.");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bookmarks) {
            Utils.startActivity(this, BookmarksActivity.class);
            return true;
        }

        if (id == R.id.action_settings) {
            Utils.startActivity(this, SettingsActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
