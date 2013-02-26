package de.crasu.grueneladung;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.util.Log;

public class TwitterHelper {
    List<Tweet> retrieveTweets() throws TwitterException {
        Twitter twitter = (new TwitterFactory()).getInstance();

        Query query = new Query("from:rwetransparent");

        QueryResult result = twitter.search(query);
        return result.getTweets();
    }

    public List<PowerGridValues> retrievePowerInformation() {
        List<Tweet> tweets = null;
        try {
            tweets = retrieveTweets();
        } catch (TwitterException e) {
            // TODO error msg
            e.printStackTrace();
        }

        Log.i("power", "retrieved tweets");

        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();

        for (Tweet tweet : tweets) {
            PowerGridValues pgv = (new PowerGridInformationRetriever()).parseTweet(tweet.getText());
            pgvs.add(pgv);
        }
        return pgvs;
    }
}
