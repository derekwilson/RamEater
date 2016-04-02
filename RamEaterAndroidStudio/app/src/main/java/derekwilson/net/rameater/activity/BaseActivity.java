package derekwilson.net.rameater.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import derekwilson.net.rameater.R;

public abstract class BaseActivity extends AppCompatActivity {

	protected abstract int getLayoutResource();

	protected boolean toolbarBackButtonNeeded = false;
	protected boolean toolbarMenuButtonNeeded = false;

	protected Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int resourceId = getLayoutResource();
		if (resourceId != 0) {
			setContentView(getLayoutResource());
		}
		toolbar = (Toolbar) findViewById(R.id.toolbar_main);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			final ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(toolbarBackButtonNeeded || toolbarMenuButtonNeeded);
			if (toolbarBackButtonNeeded) {
				toolbar.setNavigationOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
			}
			if (toolbarMenuButtonNeeded) {
				actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
			}
		}
	}

	protected void setActionBarIcon(int iconRes) {
		toolbar.setNavigationIcon(iconRes);
	}
}