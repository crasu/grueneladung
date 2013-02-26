package de.crasu.grueneladung.status;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import de.crasu.grueneladung.AbstractPowerGridStatusTask;
import de.crasu.grueneladung.R;

import static java.lang.Math.min;

public class ChargeStatusActivity extends Activity {
    private ChargeCounter chargeCounter;
    protected ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ChargeReceiver receiver = new ChargeReceiver();
        registerReceiver(receiver, filter);

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        updateView();
    }

    private void updateView() {
        (new PowerGridStatusTask()).execute();
        progressBar.setProgress((int) min(chargeCounter.getCount(), progressBar.getMax()));
    }

    private class PowerGridStatusTask extends AbstractPowerGridStatusTask {
        @Override
        protected ImageView getImageView() {
            return (ImageView) findViewById(R.id.chargeStatusImageView);
        }

        @Override
        protected void onPostExecute(Boolean powerState) {
            super.onPostExecute(powerState);
        }
    }
}