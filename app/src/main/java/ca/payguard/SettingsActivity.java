package ca.payguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void goBack() {
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if(preference.getKey().equals("edit_mode")) {
                /* TODO find out how to receive intent in MainActivity
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivityForResult(intent, 0); */

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("edit_mode", "e");
                startActivity(intent);
            }
            return super.onPreferenceTreeClick(preference);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            goBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}