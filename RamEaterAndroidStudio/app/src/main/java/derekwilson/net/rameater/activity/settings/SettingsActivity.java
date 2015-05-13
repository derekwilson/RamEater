package derekwilson.net.rameater.activity.settings;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;
import derekwilson.net.rameater.RamEaterBuildConfig;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    private IPreferencesHelper preferences;

    private Toolbar toolbar;

    private void setupToolbar() {
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
            root.addView(bar, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);


            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }else{
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();

        addPreferencesFromResource(R.xml.activty_settings);

        // inject
        preferences = new PreferencesHelper(getBaseContext());

        findPreference(getResources().getString(R.string.pref_key_version)).setSummary(
                "Version " + RamEater.getVersionName(getApplicationContext()) +
                        (RamEaterBuildConfig.PRODUCTION ? " (prod)" : " (dev)")
        );

        setMemorySummary();
    }

    private void setMemorySummary() {
        int max = preferences.getMaxMemoryMb();
        String memorySummary = "Each service will allocate " + max + "MB";
        if (max == 0) {
            memorySummary = "Each service will automatically allocate the maximum amount of memory allowed";
        }

        findPreference(getResources().getString(R.string.pref_key_max_memory_mb)).setSummary(memorySummary);
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

