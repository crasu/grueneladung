package de.crasu.grueneladung;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.crasu.grueneladung.status.ChargeReceiver;
import de.crasu.grueneladung.status.ChargeStatusActivity;

public class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("power", "got intend: " + intent.toString());

        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Log.i("power", "connected");
            ChargeReceiver.registerChargeReceiver(context);

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