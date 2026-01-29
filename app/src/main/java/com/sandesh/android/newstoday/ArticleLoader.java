package com.sandesh.android.newstoday;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 * Class to load articles in the background from the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private final String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) return null;
        return QueryUtils.fetchNews(mUrl);
    }
}
