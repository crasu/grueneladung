package de.crasu.grueneladung;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.util.RoboAsyncTask;

public abstract class AbstractPowerGridStatusTask extends RoboAsyncTask<Boolean> {
    @Inject
    PowerGridInformationRetriever pgir;
    Context context;
    private final ProgressDialog progressDialog;

    protected AbstractPowerGridStatusTask(Context context, RoboActivity activity) {
        super(context);
        progressDialog = ProgressDialog.show(activity, "", context.getString(R.string.waiting_message), true);
        this.context = context;
    }

    abstract protected ImageView getImageView();

    public Boolean call() throws Exception {
        return pgir.isEnergyGreen();
    }

    @Override
    protected void onSuccess(Boolean powerState) {
        progressDialog.dismiss();

        ImageView image = getImageView();

        if (powerState) {
            image.setImageResource(R.drawable.flower);
        } else {
            image.setImageResource(R.drawable.nuclear_power_plant);
        }
    }

    @Override
    protected void onException(Exception e) {
        Toast.makeText(context, R.string.power_state_error, 4).show();
        Log.d("grueneladung", "", e);
    }
}
