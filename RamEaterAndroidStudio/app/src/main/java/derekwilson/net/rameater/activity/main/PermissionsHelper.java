package derekwilson.net.rameater.activity.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import derekwilson.net.rameater.R;

public class PermissionsHelper implements IPermissionsHelper {
    public final static int MY_PERMISSIONS_REQUEST_NOTIFICATION = 123;

    @Override
    public boolean hasPushNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return hasPermissionBeenGranted(context, Manifest.permission.POST_NOTIFICATIONS);
        }
        // earlier versions didnt need to ask
        return true;
    }

    private boolean hasPermissionBeenGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPushNotificationPermission(Activity activity) {
        requestPermission(
                activity,
                android.Manifest.permission.POST_NOTIFICATIONS,
                R.string.notification_permission_request_title,
                R.string.notification_permission_request_body,
                MY_PERMISSIONS_REQUEST_NOTIFICATION
        );
    }

    private void requestPermission(Activity activity, String permission, int messageTitle, int messageBody, int requestCode) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showMessageOKCancel(activity, activity.getString(messageTitle), activity.getString(messageBody),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
                        }
                    });
            return;
        }
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
    }


        private void showMessageOKCancel(Activity activity, String title, String message, DialogInterface.OnClickListener okListener) {
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(activity.getString(R.string.ok_cancel_ok), okListener)
                    .setNegativeButton(activity.getString(R.string.ok_cancel_cancel), null)
                    .create()
                    .show();
        }
}
