package de.crasu.grueneladung.status;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import de.crasu.grueneladung.AbstractPowerGridStatusTask;
import de.crasu.grueneladung.R;

public class ChargeStatusActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_status);
    }

    @Override
    public void onStart() {
        super.onStart();
        (new PowerGridStatusTask()).execute();
    }

    private class PowerGridStatusTask extends AbstractPowerGridStatusTask {
        @Override
        protected ImageView getImageView() {
            return(ImageView) findViewById(R.id.chargeStatusImageView);
        }

        @Override
        protected void onPostExecute(Boolean powerState) {
            super.onPostExecute(powerState);
        }
    }
}