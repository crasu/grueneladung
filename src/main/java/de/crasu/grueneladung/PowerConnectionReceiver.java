package de.crasu.grueneladung;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("power", "got intend: " + intent.toString());

        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Log.i("power", "charging");

            Intent in = new Intent(context, GridStateActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }

}