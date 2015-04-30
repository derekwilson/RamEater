package derekwilson.net.rameater;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class RamEater extends Application {
    public static String getVersionName(Context applicationContext) {
        String versionName;
        final PackageManager packageManager = applicationContext.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(applicationContext.getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = "UNKNOWN";
            }
            return versionName;
        }
        return "UNKNOWN";
    }
}
