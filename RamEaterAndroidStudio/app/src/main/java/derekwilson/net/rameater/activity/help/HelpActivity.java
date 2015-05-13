package derekwilson.net.rameater.activity.help;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import derekwilson.net.rameater.R;
import derekwilson.net.rameater.RamEater;

public class HelpActivity extends ActionBarActivity {
    private TextView helpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helpTextView = (TextView)findViewById(R.id.txtHelp);

        // make links clickable
        helpTextView.setMovementMethod(LinkMovementMethod.getInstance());
        getHtmlFromFile(this.getApplicationContext(),"help/help.html",helpTextView);
    }

    private void getHtmlFromFile(Context context, String filename, TextView view) {

        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer buf = new StringBuffer();
        try {
            inputStream = context.getAssets().open(filename);
            RamEater.logMessage("Opened " + filename + " available " + inputStream.available());
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String str;
            if (inputStream != null) {
                while ((str = reader.readLine()) != null) {
                    buf.append(str);
                }
            }
        } catch (IOException e) {
            RamEater.logMessage("Error reading " + filename);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                RamEater.logMessage("Error closing " + filename);
            }
        }

        view.setText(Html.fromHtml(buf.toString()));
    }
}
