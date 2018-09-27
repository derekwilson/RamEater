package derekwilson.net.rameater.services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;
import derekwilson.net.rameater.activity.main.MainActivity;
import derekwilson.net.rameater.activity.settings.IPreferencesHelper;
import derekwilson.net.rameater.activity.settings.PreferencesHelper;

public abstract class EaterService extends Service {
	// we cannot modify a channel once its created - so we need to increment this ID or reinstall
	private static final String NOTIFICATION_CHANNEL_ID = "rameater_channel_id_01";

	protected abstract int getServiceId();

	private ActivityManager activityManager;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;

	private char[] memoryBlackHole = null;

    private IPreferencesHelper preferences;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logMessage("Received start id " + startId + ": " + intent);

        startForeground(getServiceId(), notificationBuilder.build());
		eatAllMemory();

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		logMessage("onCreate Method is called");
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		showNotification();

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		logMessage("OnDestroy Method is called");
		freeAllMemory();
		notificationManager.cancel(getServiceId());
		super.onDestroy();
	}

	@Override
	public boolean stopService(Intent name) {
		logMessage("stopService Method is called");
		return super.stopService(name);
	}

	private void eatAllMemory() {
        preferences = new PreferencesHelper(getBaseContext());
        int max = preferences.getMaxMemoryMb();
        boolean retry = false;
        if (max == 0) {
            // Get amount of memory this app is allowed to use (in MBs)
            int availMem = activityManager.getLargeMemoryClass();
            logMessage("available memory MB = " + availMem);
            max = availMem;
            // we need to retry as getLargeMemoryClass does not really understand MB
            retry = true;
        }
        else {
            logMessage("preferences max MB = " + max);
        }
        eatMemory(max,retry);
	}

	private void eatMemory(int numberOfMb, boolean retry) {
		logMessage("eating memory MB = " + numberOfMb);
		// convert MB -> KB -> Bytes
		int numberOfBytes = numberOfMb * 1024 * 1024;
		logMessage("eating memory bytes = " + numberOfBytes);

		boolean memoryAllocated = false;
		String message = (numberOfBytes / 1024 / 1024) + " MB allocated";
		while (!memoryAllocated)
		{
			try {
				logMessage("trying to eat memory bytes = " + numberOfBytes);
				// we need to do this before we allocate all the memory :-)
				message = (numberOfBytes / 1024 / 1024) + " MB allocated";
				// char is 2 bytes in java
				memoryBlackHole = new char[numberOfBytes / 2];
				memoryAllocated = true;
				logMessage("success eating memory bytes ="  + numberOfBytes);
			}
			catch (OutOfMemoryError e) {
                if (retry) {
                    // we cannot have that much, lets try 5MB less
                    numberOfBytes = numberOfBytes - (1024 * 1024 * 5);
                }
                else {
                    message = "Cannot allocate " + (numberOfBytes / 1024 / 1024) + " MB, try less memory";
                    updateNotification(message);
                    return;
                }
			}
		}

		logMessage("after memory allocation");
        fillBlackHole(numberOfBytes / 2);
		updateNotification(message);
	}

    private void fillBlackHole(int numberOfChars) {
	    logMessage("filling memory bytes ="  + numberOfChars);
        // dalvik allocate the memory when requested
        // art is clever and waits until you use the memory
        for (int index=0; index<numberOfChars; index++) {
            memoryBlackHole[index] = 'a';
        }
    }

	private void freeAllMemory() {
		logMessage("freeing memory");
		memoryBlackHole = null;
	}

	private void showNotification() {
		CharSequence text = getText(R.string.service_started);
		showNotification(text);

	}

	private String getServiceName() {
		return getString(getServiceId());
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private void createChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			logMessage("creating channel ="  + NOTIFICATION_CHANNEL_ID);

			NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "RamEater Notifications", NotificationManager.IMPORTANCE_LOW);

			// Configure the notification channel.
			notificationChannel.setDescription("Memory Eater Services");
			notificationChannel.enableLights(false);
			notificationChannel.enableVibration(false);
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}

	private void showNotification(CharSequence message) {
		// The PendingIntent to launch our activity if the user selects this  notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createChannel();
		}

		notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
				.setContentTitle(getString(R.string.app_name) + " " + getServiceName())
				.setContentText(message)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentIntent(contentIntent);


		// Send the notification.
		notificationManager.notify(getServiceId(), notificationBuilder.build());
	}

	private void updateNotification(String message) {
		logMessage("updateNotification ="  + message);
		notificationBuilder.setContentText(message);
		notificationManager.notify(getServiceId(), notificationBuilder.build());
		logMessage("updateNotification complete");
	}

	protected void logMessage(String message) {
        RamEater.logMessage("Service: " + getServiceName() + " " + message);
	}
}
