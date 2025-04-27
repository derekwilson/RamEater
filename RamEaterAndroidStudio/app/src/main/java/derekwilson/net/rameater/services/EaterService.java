package derekwilson.net.rameater.services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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
		logMemoryUsage("onStartCommand - start");

		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
				startForeground(getServiceId(), notificationBuilder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING);
			} else {
				startForeground(getServiceId(), notificationBuilder.build());
			}
		} catch (SecurityException ex) {
			logError("Permission denied", ex);
			Toast.makeText(this, R.string.service_not_started_permission, Toast.LENGTH_SHORT).show();
			return START_NOT_STICKY;
		}
		synchronized(this) {
			if (memoryBlackHole != null) {
				// we already had memory allocated
				logMessage("memory already allocated - ignoring restart");
				updateNotificationToCurrentMemoryUsage();
			} else {
				eatAllMemory();
			}
		}
		logMemoryUsage("onStartCommand - end");
		logMessage("onStartCommand done");
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
		logMessage("onCreate Method done");
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

	@Override
	public void onTimeout(int startId, int fgsType) {
		logMessage("onTimeout " + startId + ": " + fgsType);
		stopSelf();
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
				logMessage("success eating memory bytes = "  + numberOfBytes);
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
		//updateNotification(message);
		updateNotificationToCurrentMemoryUsage();
	}

    private void fillBlackHole(int numberOfChars) {
	    logMessage("filling memory bytes = "  + numberOfChars);
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
				new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createChannel();
		}

		notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
				.setContentTitle(getString(R.string.app_name) + " " + getServiceName())
				.setContentText(message)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setOngoing(true)
				.setContentIntent(contentIntent);


		// Send the notification.
		notificationManager.notify(getServiceId(), notificationBuilder.build());
	}

	private void updateNotification(String message) {
		logMessage("updateNotification = "  + message);
		notificationBuilder.setContentText(message);
		notificationManager.notify(getServiceId(), notificationBuilder.build());
		logMessage("updateNotification complete");
	}

	private void updateNotificationToCurrentMemoryUsage() {
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);
		String memMessage = String.format(
				"%.2f MB allocated",
				memoryInfo.getTotalPss() / 1024.0
		);
		updateNotification(memMessage);
	}

	protected void logMessage(String message) {
		RamEater.logMessage("Service: " + getServiceName() + " " + message);
	}

	protected void logError(String message, Exception ex) {
		RamEater.logError("Service: " + getServiceName() + " " + message, ex);
	}

	protected void logMemoryUsage(String message) {
		try {
			double max = Runtime.getRuntime().maxMemory(); //the maximum memory the app can use
			double heapSize = Runtime.getRuntime().totalMemory(); //current heap size
			double heapRemaining = Runtime.getRuntime().freeMemory(); //amount available in heap
			double nativeUsage = Debug.getNativeHeapAllocatedSize(); //is this right? I only want to account for native memory that my app is being "charged" for.  Is this the proper way to account for that?
			//heapSize - heapRemaining = heapUsed + nativeUsage = totalUsage
			double remaining = max - (heapSize - heapRemaining + nativeUsage);
			String runtimeMemMessage = String.format(
					"Memory: max=%.2f MB, heap=%.2f MB, remain=%.2f MB, native=%.2f MB",
					max / 1024.0 / 1024.0,
					heapSize / 1024.0 / 1024.0,
					heapRemaining / 1024.0 / 1024.0,
					nativeUsage / 1024.0 / 1024.0
			);
			logMessage(message + runtimeMemMessage);

			Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
			Debug.getMemoryInfo(memoryInfo);
			String memMessage = String.format(
					"MemoryInfo: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB",
					memoryInfo.getTotalPss() / 1024.0,
					memoryInfo.getTotalPrivateDirty() / 1024.0,
					memoryInfo.getTotalSharedDirty() / 1024.0);
			logMessage(memMessage);
		} catch (Exception e) {
			logError("cannot display memory stats " + message, e);
		}
	}
}
