package com.sandesh.android.newstoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * A custom ArrayAdapter for displaying list of news articles.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private Bookmarks mBookmarks;

    /**
     * ArticleAdapter constructor. Accepts 3 parameters.
     * @param context The activity from which this adapter is called.
     * @param articles The ArrayList with all the articles type.
     * @param bookmarks The reference Bookmarks for tracking bookmarks.
     */
    public ArticleAdapter(@NonNull Context context,
                          @NonNull ArrayList<Article> articles,
                          @NonNull Bookmarks bookmarks) {
        super(context, 0, articles);
        mBookmarks = bookmarks;
    }


    /**
     * Overrides the getView() method for displaying a custom layout for each article in the ListView.
     * @param position The article position in ListView.
     * @param convertView To view an article in ListView.
     * @param parent The parent view.
     * @return Returns the assembled article to display in the ListView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_view, parent, false);
            // Initializing the ViewHolder.
            viewHolder = new ViewHolder();
            // Look for all necessary Views in News article layout.
            viewHolder.articleTypeTextView = convertView.findViewById(R.id.article_type_name_tv);
            viewHolder.newsHeadlineTextView = convertView.findViewById(R.id.news_headline_tv);
            viewHolder.bookmarkButton = convertView.findViewById(R.id.bookmark_img_btn);
            viewHolder.shareButton = convertView.findViewById(R.id.share_img_btn);
            viewHolder.newsThumbnailImageView = convertView.findViewById(R.id.news_thumbnail_iv);
            viewHolder.authorAndDateTextView = convertView.findViewById(R.id.author_and_date_tv);
            // Add ViewHolder Tag on the News article layout.
            convertView.setTag(viewHolder);
        } else {
            // Restoring ViewHolder from the Tag.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fetching current News object from the ArrayList.
        Article currentArticle = getItem(position);

        // Set the article type and the title
        viewHolder.articleTypeTextView.setText(currentArticle.getArticleType());
        viewHolder.newsHeadlineTextView.setText(Utils.capitalize(currentArticle.getArticleTitle()));

        // Set the thumbnail.
        if (currentArticle.hasThumbnail()) {
            viewHolder.newsThumbnailImageView.setImageBitmap(currentArticle.getArticleThumbnail());
        }

        // OnClickListener for the Bookmark button.
        mBookmarks.toggleBookmarkButton(viewHolder.bookmarkButton, currentArticle);

        // Share Intent to the Share button.
        Utils.attachShareIntent(convertView.getContext(), viewHolder.shareButton, currentArticle.getArticleUrl());

        // Author name and article publish date
        Utils.composeAuthorDate(convertView.getContext(), viewHolder.authorAndDateTextView, currentArticle);

        // Return the complete news article.
        return convertView;
    }

    /** Private inner class to stores all the necessary views for the news article. */
    private static class ViewHolder {
        private TextView articleTypeTextView, newsHeadlineTextView, authorAndDateTextView;
        private ImageButton bookmarkButton, shareButton;
        private ImageView newsThumbnailImageView;
    }

}