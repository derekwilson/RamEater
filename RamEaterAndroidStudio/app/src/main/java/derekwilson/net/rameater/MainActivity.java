package derekwilson.net.rameater;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public class ServiceConfig {
        public Intent StartIntent;
        public Class<?> ServiceClass;
    }

    private ServiceConfig[] services;

    private void initServices() {
        services = new ServiceConfig[5];
        for (int index=0; index<5; index++){
            services[index] = new ServiceConfig();
        }

        services[0].ServiceClass = Service1.class;
        services[1].ServiceClass = Service2.class;
        services[2].ServiceClass = Service3.class;
        services[3].ServiceClass = Service4.class;
        services[4].ServiceClass = Service5.class;

        for (ServiceConfig config : services){
            config.StartIntent = new Intent(this, config.ServiceClass);
        }
    }

    private void startOneService(int index) {
        startService(services[index].StartIntent);
    }

    private void stopOneService(int index) {
        stopService(services[index].StartIntent);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        initServices();
	}

	@Override
	public void onClick(View view) {
		logMessage("OnClick");
		switch (view.getId())
		{
			case R.id.btnStart1:
                startOneService(0);
				break;
			case R.id.btnStop1:
				stopOneService(0);
				break;
            case R.id.btnStart2:
                startOneService(1);
                break;
            case R.id.btnStop2:
                stopOneService(1);
                break;
            case R.id.btnStart3:
                startOneService(2);
                break;
            case R.id.btnStop3:
                stopOneService(2);
                break;
            case R.id.btnStart4:
                startOneService(3);
                break;
            case R.id.btnStop4:
                stopOneService(3);
                break;
            case R.id.btnStart5:
                startOneService(4);
                break;
            case R.id.btnStop5:
                stopOneService(4);
                break;
		}
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void logMessage(String message) {
		Log.i("RamEater", message);
	}
}
