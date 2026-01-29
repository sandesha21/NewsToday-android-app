package com.sandesh.android.newstoday;


import android.graphics.Bitmap;

/**
 * Class for creating objects for displaying the news article.
 */
public class Article {

    private String mArticleTitle;
    private String mArticleUrl;
    private Bitmap mArticleThumbnail;
    private String mArticlePublishedDate;
    private String mArticleAuthor;
    private String mArticleType;

    /**
     * The Article object constructor.
     * @param title The article title.
     * @param url The article URL.
     * @param thumbnail The article (bitmap) thumbnail.
     * @param published The article publish date.
     * @param author The article's author name.
     * @param type The article type.
     */
    public Article(String title, String url, Bitmap thumbnail, String published, String author, String type) {
        mArticleTitle = title;
        mArticleUrl = url;
        mArticleThumbnail = thumbnail;
        mArticlePublishedDate = published;
        mArticleAuthor = author;
        mArticleType = type;
    }

    /** @return Returns the article title, URL, thumbnail, publish date, author and type respectively. */
    public String getArticleTitle() {
        return mArticleTitle;
    }
    public String getArticleUrl() {
        return mArticleUrl;
    }
    public Bitmap getArticleThumbnail() {
        return mArticleThumbnail;
    }
    public String getArticlePublishedDate() {
        return mArticlePublishedDate;
    }
    public String getArticleAuthor() {
        return mArticleAuthor;
    }
    public String getArticleType() {
        return mArticleType;
    }

    /** @return Checks for thumbnail and returns a boolean. */
    public boolean hasThumbnail() {
        return mArticleThumbnail != null;
    }

    /** @return Checks for article author. */
    public boolean hasAuthor() {
        return (mArticleAuthor != null) && !mArticleAuthor.isEmpty();
    }

    /**
     * Overrides toString() method for concatenating purposes.
     * @return Returns a concatenated string with all required fields.
     */
    @Override
    public String toString() {
        return "Article {" +
                "mArticleTitle='" + mArticleTitle + "', " +
                "mArticleUrl='" + mArticleUrl + "', " +
                "mArticleThumbnail='" + mArticleThumbnail.toString() + "', " +
                "mArticlePublishedDate='" + mArticlePublishedDate + "' " +
                "mArticleAuthor='" + mArticleAuthor + "' " +
                "mArticleType='" + mArticleType + "' " +
                "}";
    }
}