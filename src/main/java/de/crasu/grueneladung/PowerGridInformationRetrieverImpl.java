package de.crasu.grueneladung;

import android.util.Log;
import com.google.inject.Inject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerGridInformationRetrieverImpl implements PowerGridInformationRetriever {
    @Inject
    TwitterHelper twitterHelper;

    public void setTwitterHelper(TwitterHelper twitterHelper) {
        this.twitterHelper = twitterHelper;
    }
    
    double calcAverageGasPercentage(List<PowerGridValues> pgvs) {
        if(pgvs == null || pgvs.size() < 1)
            throw new RuntimeException("Reject");
        
        double avg = 0d;
        
        for (PowerGridValues pgv : pgvs) {
            double gasPercentage = (double) (pgv.getGas()) / pgv.getOverallPower();
            avg += gasPercentage/pgvs.size();            
        }  
        
        return avg;
    }
    
    @Override
    public Boolean isEnergyGreen() {
        List<PowerGridValues> pgvs = twitterHelper.retrievePowerInformation();

        if(pgvs == null || pgvs.size() < 1)
            return false;
        
        double avgGasPercantage = calcAverageGasPercentage(pgvs);
        PowerGridValues pgv = pgvs.get(0);
        double currentGasPercentage = (double) (pgv.getGas()) / pgv.getOverallPower();

        boolean isEneryGreen = (currentGasPercentage < avgGasPercantage);
        Log.i("power", "Green energy is: " + isEneryGreen);

        return isEneryGreen;
    }
}
