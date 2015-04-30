package derekwilson.net.rameater.activity.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import derekwilson.net.rameater.R;

public class PreferencesHelper implements IPreferencesHelper {

    private final String NAME = "defaultAppPrefs";
    private String maxMemoryKey;

    private SharedPreferences preferences;

    public PreferencesHelper(Context context)
    {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        maxMemoryKey = context.getResources().getString(R.string.pref_key_max_memory_mb);
    }

    @Override
    public int getMaxMemoryMb() {
        return preferences.getInt(maxMemoryKey,getDefaultMaxMemoryMb());
    }

    @Override
    public void setMaxMemoryMb(int max) {
        SharedPreferences.Editor editor = preferences.edit();
        if (Build.VERSION.SDK_INT < 11 && max ==0) {
            editor.putInt(maxMemoryKey, 500);
        }
        else {
            editor.putInt(maxMemoryKey, max);
        }
        editor.commit();
    }

    @Override
    public void setMaxMemoryMb(String max) {
        try {
            setMaxMemoryMb(Integer.parseInt(max));
        }
        catch (Exception ex) {
            setMaxMemoryMb(0);
        }
    }

    @Override
    public int getDefaultMaxMemoryMb() {
        int defValue = 0;
        if (Build.VERSION.SDK_INT < 11) {
            defValue = 500;
        }
        return defValue;
    }
}
