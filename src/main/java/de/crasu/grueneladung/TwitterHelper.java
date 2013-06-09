package de.crasu.grueneladung;

import twitter4j.Tweet;
import twitter4j.TwitterException;
import java.util.List;

public interface TwitterHelper {
    List<PowerGridValues> retrievePowerInformation();
}
