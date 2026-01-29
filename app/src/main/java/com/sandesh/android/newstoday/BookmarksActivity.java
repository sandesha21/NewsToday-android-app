package com.sandesh.android.newstoday;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sandesh.android.newstoday.R;

import java.util.ArrayList;

/**
 * Class to initialize Bookmarks Activity.
 */
public class BookmarksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Bookmarks bookmarks = Bookmarks.getInstance();
        ArrayList<Article> articles = bookmarks.getBookmarks();

        // Setting empty state views.
        View emptyStateView = findViewById(R.id.empty_state_view);
        ImageView emptyStateImageView = findViewById(R.id.empty_image_view);
        TextView emptyStateTextView = findViewById(R.id.empty_text_view);

        // Setting error message for empty bookmarks screen.
        String noBookmarksMsg = getString(R.string.no_bookmarks);

        // Setting empty state text and image icon.
        emptyStateImageView.setImageResource(R.drawable.ic_bookmark_not_selected);
        emptyStateTextView.setText(noBookmarksMsg);

        // Initializing the Adapter with the selected bookmarked articles.
        ListView bookmarksList = findViewById(R.id.bookmarks_list_view);
        ArticleAdapter adapter = new ArticleAdapter(BookmarksActivity.this, articles, bookmarks);
        bookmarksList.setAdapter(adapter);
        bookmarksList.setEmptyView(emptyStateView);
        bookmarks.setAdapter(adapter);

        // onClick intent to open news articles.
        bookmarksList.setOnItemClickListener((adapterView, view, i, l) -> {
            Article currentArticle = adapter.getItem(i);
            Utils.openWebsite(BookmarksActivity.this, currentArticle.getArticleUrl());
        });

    }
}