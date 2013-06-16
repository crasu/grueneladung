package de.crasu.grueneladung;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.crasu.grueneladung.status.ChargeReceiver;
import roboguice.receiver.RoboBroadcastReceiver;

public class PowerStatusReceiver extends RoboBroadcastReceiver {
    @Override
    public void handleReceive(Context context, Intent intent) {
        Log.i("power", "got intend: " + intent.toString());

        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Log.i("power", "connected");
            ChargeReceiver.registerChargeReceiver(context, new ChargeReceiver());

            Intent in = new Intent(context, GridStatusActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }

        if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Log.i("power", "disconnected");
            ChargeReceiver.unregisterChargeReceiver(context);
        }

    }

}