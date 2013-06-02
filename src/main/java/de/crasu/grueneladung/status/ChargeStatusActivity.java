package de.crasu.grueneladung.status;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import de.crasu.grueneladung.AbstractPowerGridStatusTask;
import de.crasu.grueneladung.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static java.lang.Math.min;

public class ChargeStatusActivity extends RoboActivity {
    @InjectView(R.id.progressBar)
    protected ProgressBar progressBar;
    @InjectView(R.id.chargeStatusImageView)
    protected ImageView imageView;
    private ChargeCounter chargeCounter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_status);
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        chargeCounter = new ChargeCounter(this);

        updateView();
    }

    private void updateView() {
        (new PowerGridStatusTask(getApplicationContext())).execute();
        progressBar.setProgress((int) min(chargeCounter.getCount(), progressBar.getMax()));
    }

    private class PowerGridStatusTask extends AbstractPowerGridStatusTask {
        protected PowerGridStatusTask(Context context) {
            super(context);
        }

        @Override
        protected ImageView getImageView() {
            return imageView;
        }

        @Override
        protected void onSuccess(Boolean powerState) {
            super.onSuccess(powerState);
        }
    }
}