package com.sandesh.android.newstoday;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sandesh.android.newstoday.BuildConfig;
import com.sandesh.android.newstoday.R;


/**
 * Class containing general utility methods.
 */
public final class Utils {

    private Utils() {}

    /**
     * Method for capitalizing given news article titles.
     * @param string The new article title to capitalize.
     * @return Returns the new capitalized string.
     */
    public static String capitalize(String string) {
        String[] strArray = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap;
            if (s.length() > 1) cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            else cap = s.toUpperCase();
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }

    /**
     * Method for sharing a news article with onClick listener for share button.
     * @param context The context to call method.
     * @param button Share button.
     * @param url The new article URL intended to be shared.
     */
    public static void attachShareIntent(Context context, ImageButton button, String url) {
        String subject = context.getResources().getString(R.string.news_subject);
        String title = context.getResources().getString(R.string.news_title);
        button.setOnClickListener(view -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(sharingIntent, title));
        });
    }

    /**
     * Method for creating TextView string for 'Author - Date'.
     * @param context The Context to call method.
     * @param view The intended TextView.
     * @param article The Article whose author and date need to be extracted..
     */
    public static void composeAuthorDate(Context context, TextView view, Article article) {
        String emDash = context.getResources().getString(R.string.em_dash);
        String composedString;
        if (article.hasAuthor()) {
            composedString = capitalize(article.getArticleAuthor()) + "  " + emDash + "  " + article.getArticlePublishedDate();
        } else {
            composedString = article.getArticlePublishedDate();
        }
        view.setText(composedString);
    }

    /**
     * Method for creating API KEY string.
     * @return Returns API KEY.
     */
    public static String getApiKey() {
        String dash = "-";
        String a = BuildConfig.GUARD_A;
        String b = BuildConfig.GUARD_B;
        String c = BuildConfig.GUARD_C;
        String d = BuildConfig.GUARD_D;
        String e = BuildConfig.GUARD_E;
        return b+dash+a+dash+d+dash+c+dash+e;
    }

    /**
     * Toast message method.
     * @param context The Context to call method.
     * @param message The message to show in the Toast.
     */
    public static void showToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Start selected Activity.
     * @param context The Context to call method.
     * @param activity An activity to start.
     */
    public static void startActivity(Context context, Class activity) {
        Intent thisIntent = new Intent(context, activity);
        context.startActivity(thisIntent);
    }

    /**
     * Method for opening the selected News article.
     * @param context The Context to call method.
     * @param url The news article URL to open.
     */
    public static void openWebsite(Context context, String url) {
        Uri articleUri = Uri.parse(url);
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
        context.startActivity(websiteIntent);
    }

}