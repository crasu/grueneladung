package de.crasu.grueneladung;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

public class TwitterHelperImpl implements TwitterHelper {
    List<Tweet> retrieveTweets() {
        Twitter twitter = (new TwitterFactory()).getInstance();

        Query query = new Query("from:rwetransparent");

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
        return result.getTweets();
    }

    @Override
    public List<PowerGridValues> retrievePowerInformation() {
        List<Tweet> tweets = retrieveTweets();

        Log.i("power", "retrieved tweets");

        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();

        for (Tweet tweet : tweets) {
            PowerGridValues pgv = parseTweet(tweet.getText());
            pgvs.add(pgv);
        }
        return pgvs;
    }

    public PowerGridValues parseTweet(String tweetText) {
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
}
