package derekwilson.net.rameater.activity.settings;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachToolbar();
        fixToolbarTextAndIconColor(0xFFFFFFFF);

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
            Toast.makeText(this, R.string.all_stopped, Toast.LENGTH_SHORT).show();
            preferences.setMaxMemoryMb(sharedPreferences.getString(key,"0"));
        }
        setMemorySummary();
    }

    private void attachToolbar() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_settings, null);

        root.removeAllViews();
        toolbarContainer.addView(content);      // this will preserve whatever was created for this class
        root.addView(toolbarContainer);

        toolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar_main);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fixToolbarTextAndIconColor(int iconColor) {
        // there is a problem with the text / icon colour when we put the toolbar on a preferences page like this
        // so we do it ourselves
        toolbar.setTitleTextColor(iconColor);
        toolbar.setSubtitleTextColor(iconColor);
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
        toolbar.getNavigationIcon().setColorFilter(colorFilter);
    }
}

