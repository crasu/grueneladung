package de.crasu.grueneladung;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class GridStateActivity extends Activity {
    private static final int WINDOW_CLOSING_DELAY = 4000;
    private Timer windowClosingTimer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        progressDialog = ProgressDialog.show(this, "", getString(R.string.waiting_message), true);

        (new PowerGridStatusTask()).execute();
    }
    
    private class PowerGridStatusTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... unused) {
            List<PowerGridValues> pgvs = (new TwitterHelper()).retrievePowerInformation();   
            
            Log.i("power", "Green energy is: " + (new PowerGridInformationRetriever()).isEnergyGreen(pgvs));            
            return (new PowerGridInformationRetriever()).isEnergyGreen(pgvs);
        }

        @Override
        protected void onPostExecute(Boolean powerState) {          
            ImageView image = (ImageView) findViewById(R.id.imageView);
            
            if (powerState) {
                image.setImageResource(R.drawable.flower);
            } else {
                image.setImageResource(R.drawable.nuclear_power_plant);
            }
            
            progressDialog.dismiss();

            startWindowClosingTimer();
        }
    }    

    private void startWindowClosingTimer() {
        windowClosingTimer = new Timer();
        windowClosingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, WINDOW_CLOSING_DELAY);
    }

    @Override
    protected void onDestroy() {
        if (windowClosingTimer != null) {
            windowClosingTimer.cancel();
        }
        super.onDestroy();
    }

}
