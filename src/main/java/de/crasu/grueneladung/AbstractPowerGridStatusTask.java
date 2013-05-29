package de.crasu.grueneladung;

import android.content.Context;
import android.widget.ImageView;
import com.google.inject.Inject;
import roboguice.util.RoboAsyncTask;

public abstract class AbstractPowerGridStatusTask extends RoboAsyncTask<Boolean> {
    @Inject
    PowerGridInformationRetriever pgir;

    protected AbstractPowerGridStatusTask(Context context) {
        super(context);
    }

    abstract protected ImageView getImageView();

    public Boolean call() throws Exception {
        return pgir.isEnergyGreen();
    }

    @Override
    protected void onSuccess(Boolean powerState) {
        ImageView image = getImageView();

        if (powerState) {
            image.setImageResource(R.drawable.flower);
        } else {
            image.setImageResource(R.drawable.nuclear_power_plant);
        }
    }
}
