package de.crasu.grueneladung;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

abstract public class AbstractPowerGridStatusTask extends AsyncTask<Void, Void, Boolean> {
    abstract protected ImageView getImageView();

    protected Boolean doInBackground(Void... unused) {
        List<PowerGridValues> pgvs = (new TwitterHelper()).retrievePowerInformation();

        Log.i("power", "Green energy is: " + (new PowerGridInformationRetriever()).isEnergyGreen(pgvs));
        return (new PowerGridInformationRetriever()).isEnergyGreen(pgvs);
    }

    @Override
    protected void onPostExecute(Boolean powerState) {
        ImageView image = getImageView();

        if (powerState) {
            image.setImageResource(R.drawable.flower);
        } else {
            image.setImageResource(R.drawable.nuclear_power_plant);
        }
    }
}
