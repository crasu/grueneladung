package de.crasu.grueneladung;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.*;
import android.util.Log;
import twitter4j.auth.AccessToken;

public class TwitterInformationHelper implements InformationHelper {
    List<Status> retrieveTweets() {
        Twitter twitter = getTwitterInstance();

        Query query = new Query("from:rwetransparent");

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }

        return result.getTweets();
    }

    private Twitter getTwitterInstance() {
        Twitter twitter = (new TwitterFactory()).getInstance();

        twitter.setOAuthConsumer("sc7ykQE8pt7EPnP9yl46Q", "1UnDAOIMKJuwWmebWlzaU5KruXuwTVK05W3yNpGdhDM");
        AccessToken accessToken = new AccessToken("135220604-24qXQKuCEbO8blgKCsLzkn8vblkq79iUPRADjNRw", "nx4klUE04O4TDXmsT27LUeNhz0Py9ODy2ufPgFr8");

        twitter.setOAuthAccessToken(accessToken);

        return twitter;
    }

    @Override
    public List<PowerGridValues> retrievePowerInformation() {
        List<Status> tweets = retrieveTweets();

        Log.i("power", "retrieved tweets");

        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();

        for (Status tweet : tweets) {
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
            pgv = new PowerGridValuesBuilder().
                    withCoal(Integer.valueOf(m.group(1)) + Integer.valueOf(m.group(2))).
                    withGas(Integer.valueOf(m.group(3))).
                    withNuclear(Integer.valueOf(m.group(4))).build();
        }

        return pgv;
    }
}
