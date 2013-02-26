package de.crasu.grueneladung;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class GridStatusActivity extends Activity {
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
    
    private class PowerGridStatusTask extends AbstractPowerGridStatusTask {
        @Override
        protected ImageView getImageView() {
            return(ImageView) findViewById(R.id.gridStatusImageView);
        }

        @Override
        protected void onPostExecute(Boolean powerState) {
            super.onPostExecute(powerState);
            
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
