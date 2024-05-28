package derekwilson.net.rameater.activity.main;

import android.app.Activity;
import android.content.Context;

public interface IPermissionsHelper {
    boolean hasPushNotificationPermission(Context context);
    void requestPushNotificationPermission(Activity activity);
}
