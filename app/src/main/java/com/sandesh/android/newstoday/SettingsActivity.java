package com.sandesh.android.newstoday;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.settings);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class NewsPreferenceFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {

        private static final int MIN_ARTICLES = 1;
        private static final int MAX_ARTICLES = 200;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_main, rootKey);

            Preference selectedArticleType =
                    findPreference(getString(R.string.settings_article_type_key));
            if (selectedArticleType != null) {
                selectedArticleType.setOnPreferenceChangeListener(this);
            }

            Preference pageSize =
                    findPreference(getString(R.string.settings_number_of_articles_key));
            if (pageSize != null) {
                pageSize.setOnPreferenceChangeListener(this);
            }

            // Set initial summaries
            if (selectedArticleType instanceof ListPreference) {
                ListPreference lp = (ListPreference) selectedArticleType;
                lp.setSummary(lp.getEntry());
            }
            if (pageSize instanceof EditTextPreference) {
                EditTextPreference et = (EditTextPreference) pageSize;
                et.setSummary(et.getText());
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            String stringValue = String.valueOf(newValue);

            if (preference instanceof ListPreference) {
                ListPreference lp = (ListPreference) preference;
                int index = lp.findIndexOfValue(stringValue);
                if (index >= 0) {
                    preference.setSummary(lp.getEntries()[index]);
                }
                return true;
            }

            // page size validation
            String pageSizeKey = getString(R.string.settings_number_of_articles_key);
            if (key.equals(pageSizeKey)) {
                try {
                    int n = Integer.parseInt(stringValue);
                    if (n < MIN_ARTICLES || n > MAX_ARTICLES) {
                        Utils.showToast(getActivity(), getString(R.string.settings_number_of_articles_warning));
                        return false;
                    }
                } catch (NumberFormatException e) {
                    Utils.showToast(getActivity(), getString(R.string.settings_number_of_articles_warning));
                    return false;
                }
            }

            preference.setSummary(stringValue);
            return true;
        }
    }
}
