package de.crasu.grueneladung.status;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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

    ChargeCounter chargeCounter;

    @Override
    public void handleReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        Log.d("power", "got intend: " + intent.toString() + " with status: " + status);

        if(status == BatteryManager.BATTERY_STATUS_FULL) {
            Log.i("power", "battery is fully charged");

            if(!counted) {
                chargeCounter = new ChargeCounter(context);
                (new CounterTask(context)).execute();
                counted = true;

                Log.i("power", "counted charge");
            }
        }

    }

    private class CounterTask extends RoboAsyncTask<Boolean> { //TODO use abstract task

        protected CounterTask(Context context) {
            super(context);
            this.context = context;
        }
        public Boolean call() {
            try {
                System.out.print("call called\n");//TODO test code
                return pgir.isEnergyGreen();
            } catch (Throwable e) {
                System.out.print("exception in call "+e.toString() +"\n");//TODO test code
                return null; //TODO test code
            }
        }

        @Override
        protected void onSuccess(Boolean isGreen) {
            System.out.print("success called   " + isGreen.toString() + "\n");//TODO test code
            if(isGreen) {
                chargeCounter.count();
            }
        }

        @Override
        protected void onException(Exception e) {
            Toast.makeText(context, R.string.power_state_error, 4).show();
        }
    }
}
