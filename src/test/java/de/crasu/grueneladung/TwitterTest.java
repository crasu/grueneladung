package de.crasu.grueneladung;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import twitter4j.Tweet;
import twitter4j.TwitterException;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TwitterTest { 
    @Test
    public void rweTweetsAreValid() throws TwitterException {
        List<Tweet> result = (new TwitterHelper()).retrieveTweets();
        for (Tweet tweet : result) {
            String text = tweet.getText();
            assertTrue(text.matches("Braunkohle ges.: (\\d+)MW\\/ Steinkohle ges.: (\\d+)MW\\/ Gas ges.: (\\d+)MW\\/ Kernenergie ges.: (\\d+)MW/.*?"));
        }        
    }

}
