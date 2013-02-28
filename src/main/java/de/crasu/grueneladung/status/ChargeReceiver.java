package de.crasu.grueneladung.status;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.util.Log;
import de.crasu.grueneladung.PowerGridInformationRetriever;
import de.crasu.grueneladung.R;

public class ChargeReceiver extends BroadcastReceiver {
    static ChargeReceiver receiver;
    boolean counted = false;

    static public void registerChargeReceiver(Context context) {
        Log.d("power", "registering charge receiver");

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new ChargeReceiver();
        context.getApplicationContext().registerReceiver(receiver, filter);
    }

    static public void unregisterChargeReceiver(Context context) {
        Log.d("power", "trying to unregister charge receiver");

        if(receiver != null)
            context.getApplicationContext().unregisterReceiver(receiver);
    }

    private ChargeCounter chargeCounter;

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        Log.d("power", "got intend: " + intent.toString() + " with status: " + status);

        if(status == BatteryManager.BATTERY_STATUS_FULL) {
            Log.i("power", "battery is fully charged");

            if(!counted) {
                chargeCounter = new ChargeCounter(context);
                (new CounterTask()).execute();
                counted = true;

                Log.i("power", "counted charge");
            }
        }

    }

    private class CounterTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... unused) {
            return (new PowerGridInformationRetriever()).isEnergyGreen();
        }

        @Override
        protected void onPostExecute(Boolean isGreen) {
            if(isGreen) {
                chargeCounter.count();
            }
        }
    }
}
