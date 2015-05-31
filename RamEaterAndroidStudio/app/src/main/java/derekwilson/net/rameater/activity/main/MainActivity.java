package derekwilson.net.rameater.activity.main;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;
import derekwilson.net.rameater.activity.help.HelpActivity;
import derekwilson.net.rameater.activity.settings.SettingsActivity;
import derekwilson.net.rameater.services.Service1;
import derekwilson.net.rameater.services.Service2;
import derekwilson.net.rameater.services.Service3;
import derekwilson.net.rameater.services.Service4;
import derekwilson.net.rameater.services.Service5;
import derekwilson.net.rameater.services.Service6;
import derekwilson.net.rameater.services.ServiceConfig;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private ServiceArrayAdapter adapter;
    private ListView lvServices;
    private RamEater application;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        lvServices = (ListView) findViewById(R.id.lvServices);
        application = (RamEater)getApplication();
        adapter = new ServiceArrayAdapter(this,application.getAllServiceConfigs());
        lvServices.setAdapter(adapter);
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

        switch (id) {
            case R.id.action_stop_all:
                application.stopAllServices();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_help:
                Intent help_intent = new Intent(this, HelpActivity.class);
                startActivity(help_intent);
                break;
            case R.id.action_app_settings:
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                break;
        }

		return super.onOptionsItemSelected(item);
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
                    startService(thisService.StartIntent);
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


