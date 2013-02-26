package de.crasu.grueneladung;

import android.util.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerGridInformationRetriever {
    TwitterHelper twitterHelper = new TwitterHelper();

    public void setTwitterHelper(TwitterHelper twitterHelper) {
        this.twitterHelper = twitterHelper;
    }

    PowerGridValues parseTweet(String tweetText) {
        PowerGridValues pgv = null;
        
        Pattern p = Pattern.compile("Braunkohle ges.: (\\d+)MW\\/ Steinkohle ges.: (\\d+)MW\\/ Gas ges.: (\\d+)MW\\/ Kernenergie ges.: (\\d+)MW/");
        Matcher m = p.matcher(tweetText);
        
        if(m.find()) {
            pgv = new PowerGridValues();
            
            pgv.setCoal(Integer.valueOf(m.group(1)) + Integer.valueOf(m.group(2)));
            pgv.setGas(Integer.valueOf(m.group(3)));
            pgv.setNuclear(Integer.valueOf(m.group(4)));
        }            
        
        return pgv;        
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
