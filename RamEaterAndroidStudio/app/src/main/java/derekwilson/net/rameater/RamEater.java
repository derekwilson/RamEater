package derekwilson.net.rameater;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import derekwilson.net.rameater.services.Service1;
import derekwilson.net.rameater.services.Service10;
import derekwilson.net.rameater.services.Service11;
import derekwilson.net.rameater.services.Service12;
import derekwilson.net.rameater.services.Service13;
import derekwilson.net.rameater.services.Service14;
import derekwilson.net.rameater.services.Service15;
import derekwilson.net.rameater.services.Service16;
import derekwilson.net.rameater.services.Service17;
import derekwilson.net.rameater.services.Service18;
import derekwilson.net.rameater.services.Service19;
import derekwilson.net.rameater.services.Service2;
import derekwilson.net.rameater.services.Service20;
import derekwilson.net.rameater.services.Service21;
import derekwilson.net.rameater.services.Service22;
import derekwilson.net.rameater.services.Service23;
import derekwilson.net.rameater.services.Service24;
import derekwilson.net.rameater.services.Service25;
import derekwilson.net.rameater.services.Service26;
import derekwilson.net.rameater.services.Service27;
import derekwilson.net.rameater.services.Service28;
import derekwilson.net.rameater.services.Service29;
import derekwilson.net.rameater.services.Service3;
import derekwilson.net.rameater.services.Service30;
import derekwilson.net.rameater.services.Service4;
import derekwilson.net.rameater.services.Service5;
import derekwilson.net.rameater.services.Service6;
import derekwilson.net.rameater.services.Service7;
import derekwilson.net.rameater.services.Service8;
import derekwilson.net.rameater.services.Service9;
import derekwilson.net.rameater.services.ServiceConfig;

public class RamEater extends Application {
    private final int numberOfServices = 30;
    private List<ServiceConfig> services;

    @Override
    public void onCreate() {
        super.onCreate();
        logMessage("RamEater: v" + getVersionName(getBaseContext()));
        logMessage("RamEater: SDK " + Build.VERSION.SDK_INT);
        PreferenceManager.setDefaultValues(this,R.xml.activty_settings,false);
    }

    private void initServices() {
        services = new ArrayList<ServiceConfig>(numberOfServices);
        for (int index=0; index<numberOfServices; index++){
            ServiceConfig thisConfig = new ServiceConfig();
            thisConfig.DisplayName = "Service " + (index + 1);
            services.add(thisConfig);
        }

        services.get(0).ServiceClass = Service1.class;
        services.get(1).ServiceClass = Service2.class;
        services.get(2).ServiceClass = Service3.class;
        services.get(3).ServiceClass = Service4.class;
        services.get(4).ServiceClass = Service5.class;
        services.get(5).ServiceClass = Service6.class;
        services.get(6).ServiceClass = Service7.class;
        services.get(7).ServiceClass = Service8.class;
        services.get(8).ServiceClass = Service9.class;
        services.get(9).ServiceClass = Service10.class;
        services.get(10).ServiceClass = Service11.class;
        services.get(11).ServiceClass = Service12.class;
        services.get(12).ServiceClass = Service13.class;
        services.get(13).ServiceClass = Service14.class;
        services.get(14).ServiceClass = Service15.class;
        services.get(15).ServiceClass = Service16.class;
        services.get(16).ServiceClass = Service17.class;
        services.get(17).ServiceClass = Service18.class;
        services.get(18).ServiceClass = Service19.class;
        services.get(19).ServiceClass = Service20.class;
        services.get(20).ServiceClass = Service21.class;
        services.get(21).ServiceClass = Service22.class;
        services.get(22).ServiceClass = Service23.class;
        services.get(23).ServiceClass = Service24.class;
        services.get(24).ServiceClass = Service25.class;
        services.get(25).ServiceClass = Service26.class;
        services.get(26).ServiceClass = Service27.class;
        services.get(27).ServiceClass = Service28.class;
        services.get(28).ServiceClass = Service29.class;
        services.get(29).ServiceClass = Service30.class;

        for (ServiceConfig config : services){
            config.StartIntent = new Intent(this, config.ServiceClass);
        }

        logMessage("Services initialised: " + services.size());
        for (ServiceConfig config : services){
            logMessage("Services " + config.DisplayName + " Class: " + config.ServiceClass);
        }
    }

    public List<ServiceConfig> getAllServiceConfigs() {
        if (services == null) {
            initServices();
        }
        return services;
    }

    public void startAllServices() {
        getAllServiceConfigs();
        for (ServiceConfig config : services) {
            startService(config.StartIntent);
        }
    }

    public void stopAllServices() {
        getAllServiceConfigs();
        for (ServiceConfig config : services) {
            stopService(config.StartIntent);
        }
    }

    public static void logMessage(String message) {
        Log.i("RamEater", message);
    }
    public static void logError(String message, Exception ex) {
        Log.e("RamEater", message, ex);
    }

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
