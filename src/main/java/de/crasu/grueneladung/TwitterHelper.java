package de.crasu.grueneladung;

import twitter4j.Tweet;
import twitter4j.TwitterException;
import java.util.List;

public interface TwitterHelper {
    List<Tweet> retrieveTweets() throws TwitterException;
    List<PowerGridValues> retrievePowerInformation();
}
