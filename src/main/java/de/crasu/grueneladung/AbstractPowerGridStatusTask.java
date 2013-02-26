package de.crasu.grueneladung;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

abstract public class AbstractPowerGridStatusTask extends AsyncTask<Void, Void, Boolean> {
    abstract protected ImageView getImageView();

    protected Boolean doInBackground(Void... unused) {
        return (new PowerGridInformationRetriever()).isEnergyGreen();
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
