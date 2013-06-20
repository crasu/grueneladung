package de.crasu.grueneladung;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class GridStatusActivity extends RoboActivity {
    @InjectView(R.id.gridStatusImageView)
    ImageView imageView;

    private static final int WINDOW_CLOSING_DELAY = 4000;
    private Timer windowClosingTimer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        (new PowerGridStatusTask(getApplicationContext(), this)).execute();
    }
    
    private class PowerGridStatusTask extends AbstractPowerGridStatusTask {
        protected PowerGridStatusTask(Context context, RoboActivity activity) {
            super(context, activity);
        }

        @Override
        protected ImageView getImageView() {
            return imageView;
        }

        @Override
        protected void onSuccess(Boolean powerState) {
            super.onSuccess(powerState);
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
