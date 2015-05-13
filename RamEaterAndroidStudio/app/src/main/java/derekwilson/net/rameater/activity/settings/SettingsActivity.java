package derekwilson.net.rameater.activity.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.widget.Toast;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;
import derekwilson.net.rameater.RamEaterBuildConfig;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    private IPreferencesHelper preferences;

    private void setMemorySummary() {
        int max = preferences.getMaxMemoryMb();
        String memorySummary = "Each service will allocate " + max + "MB";
        if (max == 0) {
            memorySummary = "Each service will automatically allocate the maximum amount of memory allowed";
        }

        findPreference(getResources().getString(R.string.pref_key_max_memory_mb)).setSummary(memorySummary);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activty_settings);

        // inject
        preferences = new PreferencesHelper(getBaseContext());

        findPreference(getResources().getString(R.string.pref_key_version)).setSummary(
                "Version " + RamEater.getVersionName(getApplicationContext()) +
                        (RamEaterBuildConfig.PRODUCTION ? " (prod)" : " (dev)")
        );

        setMemorySummary();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == getString(R.string.pref_key_max_memory_mb)) {
            RamEater app = (RamEater) getApplication();
            app.stopAllServices();
            Toast.makeText(this, "All services have been stopped", Toast.LENGTH_SHORT).show();
            preferences.setMaxMemoryMb(sharedPreferences.getString(key,"0"));
        }
        setMemorySummary();
    }
}

