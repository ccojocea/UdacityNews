package com.example.ccojo.udacitynews;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    /**
     * Tag for log messages
     */
    private static final String TAG = SettingsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    // Make sure settings are also applied and refreshed after pressing the Back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_main);

            // show summary under preference label
            Preference itemsPerPage = findPreference(getString(R.string.settings_items_per_page_key));
            bindPreferenceSummaryToValue(itemsPerPage);
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference.getKey().equals(getString(R.string.settings_order_by_key))) {
                if (stringValue.equals(getString(R.string.settings_order_by_oldest_value)) || stringValue.equals(getString(R.string.settings_order_by_relevance_value))){
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toast_order_by) + " " + stringValue + " " + getString(R.string.toast_no_images), Toast.LENGTH_LONG).show();
                }
            } else if (preference.getKey().equals(getString(R.string.settings_items_per_page_key))) {
                if (Integer.valueOf(stringValue) < 5) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_at_least_items_per_page, Toast.LENGTH_LONG).show();
                    return false;
                } else if (Integer.valueOf(stringValue) > 100) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_at_most_items_per_page, Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if(preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if(prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }

            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
