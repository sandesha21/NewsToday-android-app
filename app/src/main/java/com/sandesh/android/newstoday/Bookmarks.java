package com.sandesh.android.newstoday;


import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Class to stores selected Bookmarks list.
 */
public class Bookmarks {

    private static final String LOG_TAG = Bookmarks.class.getSimpleName();

    // Initializing the class instance.
    private static Bookmarks INSTANCE = new Bookmarks();

    // The global Bookmarks list tot stores bookmarked articles.
    private ArrayList<Article> mBookmarks = new ArrayList<>();

    // The stored Adapter
    private ArticleAdapter mAdapter;

    private Bookmarks() {}

    /** @return Returns the existing instance of the class. */
    public static Bookmarks getInstance() {
        return(INSTANCE);
    }

    /** @return Returns the bookmarked news articles list. */
    public ArrayList<Article> getBookmarks() {
        return mBookmarks;
    }


    public void setAdapter(ArticleAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Method for adding a news article to selected bookmarks list.
     * @param article The news article to add to selected bookmarks list.
     */
    private void addArticleToBookmarks(Article article) {
        if ((article != null) && !isBookmark(article)) {
            mBookmarks.add(article);
        }
    }

    /**
     * Method for removing the news article from selected bookmarks list.
     * @param article The news article to remove from selected bookmarks list.
     */
    private void removeArticleFromBookmarks(Article article) {
        for (int i = 0; i < mBookmarks.size(); i++) {
            Article currentArticle = mBookmarks.get(i);
            if (currentArticle.getArticleTitle().equals(article.getArticleTitle())) {
                mBookmarks.remove(i);
            }
        }
    }

    /**
     * Method to check if news article is already present in selected bookmarks list or not.
     * @param article The news article which needs to be searched in selected bookmarks list.
     * @return Returns logic, checking presence of the new article.
     */
    private boolean isBookmark(Article article) {
        boolean bookmarkState = false;
        for (int i = 0; i < mBookmarks.size(); i++) {
            Article currentArticle = mBookmarks.get(i);
            if (currentArticle.getArticleTitle().equals(article.getArticleTitle())) {
                bookmarkState = true;
            }
        }
        return bookmarkState;
    }

    /**
     * Method for adding or removing news article to/from the selected bookmarks list.
     * @param article The news article to add or to remove from the selected bookmarks list.
     */
    public void toggleBookmarkButton(ImageButton button, Article article) {

        // Set the button.
        if (isBookmark(article)) {
            button.setActivated(true);
        } else {
            button.setActivated(false);
        }

        // Attach the OnClickListener to the bookmark button of a news article.
        button.setOnClickListener(view -> {
            if (!isBookmark(article) && !button.isActivated()) {
                addArticleToBookmarks(article);
                button.setActivated(true);
            } else {
                removeArticleFromBookmarks(article);
                button.setActivated(false);
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @return Returns the list of Bookmarks contained.
     */
    @Override
    public String toString() {
        return LOG_TAG + " { mBookmarks=" + mBookmarks + " }";
    }
}