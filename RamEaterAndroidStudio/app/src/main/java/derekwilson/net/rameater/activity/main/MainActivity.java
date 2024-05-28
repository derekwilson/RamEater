package derekwilson.net.rameater.activity.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import java.util.List;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;
import derekwilson.net.rameater.activity.BaseActivity;
import derekwilson.net.rameater.activity.help.HelpActivity;
import derekwilson.net.rameater.activity.settings.SettingsActivity;
import derekwilson.net.rameater.services.ServiceConfig;


public class MainActivity extends BaseActivity
        implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener
{
	private DrawerLayout drawer;
	private NavigationView navigationView;

    private ServiceArrayAdapter adapter;
    private ListView lvServices;
    private RamEater application;

	private IPermissionsHelper permissionsHelper;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
	    toolbarMenuButtonNeeded = true;
	    super.onCreate(savedInstanceState);

	    drawer = (DrawerLayout) findViewById(R.id.drawer);
	    navigationView = (NavigationView) findViewById(R.id.nav_view);
	    if (navigationView != null) {
		    navigationView.setNavigationItemSelectedListener(this);
	    }

        lvServices = (ListView) findViewById(R.id.lvServices);
        application = (RamEater)getApplication();
        adapter = new ServiceArrayAdapter(this,application.getAllServiceConfigs());
        lvServices.setAdapter(adapter);

		// inject
		permissionsHelper = new PermissionsHelper();
		if (!permissionsHelper.hasPushNotificationPermission(this)) {
			permissionsHelper.requestPushNotificationPermission(this);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case PermissionsHelper.MY_PERMISSIONS_REQUEST_NOTIFICATION:
			{
				// If request is cancelled, the result arrays are empty.
				if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission granted
					if (permissionsHelper.hasPushNotificationPermission(this)) {
						RamEater.logMessage("MainActivity notification permission granted");
					}
				} else {
					// permission denied
					RamEater.logMessage("MainActivity notification permission denied");
					Toast.makeText(this, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}

	@Override
	public void onClick(View view) {
		RamEater.logMessage("OnClick");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		// we want a separator on the menu
		MenuCompat.setGroupDividerEnabled(menu,true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

        switch (id) {
	        case android.R.id.home:
		        drawer.openDrawer(GravityCompat.START);
		        return true;
			case R.id.action_start_all:
				application.startAllServices();
				if (permissionsHelper.hasPushNotificationPermission(this)) {
					Toast.makeText(this, R.string.all_started, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show();
				}
				return true;
            case R.id.action_stop_all:
                application.stopAllServices();
	            Toast.makeText(this, R.string.all_stopped, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_app_settings:
	            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		            // goto settings -> developer options
		            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
	            }
	            else {
		            // goto settings -> Apps
		            startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
	            }
                break;
        }

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		RamEater.logMessage("Navigation item selected: checked " + menuItem.getTitle());
		switch (menuItem.getItemId()) {
			case R.id.navigation_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				break;
			case R.id.navigation_help:
				Intent helpIntent = new Intent(this, HelpActivity.class);
				startActivity(helpIntent);
				break;
		}
		drawer.closeDrawers();
		return false;
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(navigationView)) {
			drawer.closeDrawers();
			return;
		}
		super.onBackPressed();
	}

	public class ServiceArrayAdapter extends ArrayAdapter<ServiceConfig> {
        private final Context context;
        private final List<ServiceConfig> values;

        public ServiceArrayAdapter(Context context, List<ServiceConfig> serviceConfigs) {
            super(context, R.layout.list_item_service, serviceConfigs);
            this.context = context;
            this.values = serviceConfigs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_service, parent, false);

            final ServiceConfig thisService = values.get(position);

            TextView label = (TextView) rowView.findViewById(R.id.service_row_label);
            label.setText(thisService.DisplayName);

            Button startButton = (Button) rowView.findViewById(R.id.btnStart);
            startButton.setText("Start " + (position + 1));
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	try {
						ComponentName name = startService(thisService.StartIntent);
						if (name == null) {
							RamEater.logMessage("Cannot start service");
							Toast.makeText(getContext(), R.string.service_not_started, Toast.LENGTH_SHORT).show();
						} else {
							RamEater.logMessage("Started: " + name.flattenToString());
							if (!permissionsHelper.hasPushNotificationPermission(getContext())) {
								Toast.makeText(getContext(), R.string.notification_permission_denied, Toast.LENGTH_SHORT).show();
							}
						}
					} catch (SecurityException ex) {
                		RamEater.logError("permission denied", ex);
						Toast.makeText(getContext(), R.string.service_not_started_permission, Toast.LENGTH_SHORT).show();
					}
                }
            });

            Button stopButton = (Button) rowView.findViewById(R.id.btnStop);
            stopButton.setText("Stop " + (position + 1));
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopService(thisService.StartIntent);
                }
            });

            return rowView;
        }
    }
}


