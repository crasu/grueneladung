package de.crasu.grueneladung.status;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;
import com.google.inject.Inject;
import de.crasu.grueneladung.PowerGridInformationRetriever;
import de.crasu.grueneladung.R;
import roboguice.receiver.RoboBroadcastReceiver;
import roboguice.util.RoboAsyncTask;


public class ChargeReceiver extends RoboBroadcastReceiver {
    @Inject
    PowerGridInformationRetriever pgir;

    static ChargeReceiver chargeReceiver;
    boolean counted = false;
    ChargeCounter chargeCounter;
    boolean chargeIsGreen = false;

    static public void registerChargeReceiver(Context context, ChargeReceiver chargeReceiver) {
        Log.d("power", "registering charge chargeReceiver");
        ChargeReceiver.chargeReceiver = chargeReceiver;

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.getApplicationContext().registerReceiver(chargeReceiver, filter);

        chargeReceiver.retrieveGridStatus(context);
    }

    static public void unregisterChargeReceiver(Context context) {
        Log.d("power", "trying to unregister charge chargeReceiver");

        if (chargeReceiver != null)
            context.getApplicationContext().unregisterReceiver(chargeReceiver);
    }

    protected void retrieveGridStatus(Context context) {
        (new GridStatusTask(context)).execute();
    }

    @Override
    public void handleReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        Log.d("power", "got intend: " + intent.toString() + " with status: " + status);

        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            Log.i("power", "battery is fully charged");
            chargeCounter = new ChargeCounter(context);

            if (!counted && chargeIsGreen) {
                chargeCounter.count();
                counted = true;

                Log.i("power", "counted charge");
            }
        }

    }

    private class GridStatusTask extends RoboAsyncTask<Boolean> {

        protected GridStatusTask(Context context) {
            super(context);
            this.context = context;
        }

        public Boolean call() {
            return pgir.isEnergyGreen();
        }

        @Override
         protected void onSuccess(Boolean isGreen) {
            chargeIsGreen = isGreen;
        }

        @Override
        protected void onException(Exception e) {
            Toast.makeText(context, R.string.power_state_error, 4).show();
        }
    }
}
