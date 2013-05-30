package de.crasu.grueneladung;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import twitter4j.Tweet;
import twitter4j.TwitterException;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TwitterHelperTest {
    @Test
    public void rweTweetsAreValid() throws TwitterException {
        //TODO use injection here
        List<Tweet> result = (new TwitterHelperImpl()).retrieveTweets();
        for (Tweet tweet : result) {
            String text = tweet.getText();
            assertTrue(text.matches("Braunkohle ges.: (\\d+)MW\\/ Steinkohle ges.: (\\d+)MW\\/ Gas ges.: (\\d+)MW\\/ Kernenergie ges.: (\\d+)MW/.*?"));
        }        
    }

    @Test
    public void parseTweetToPowerGridValues() {
        String tweetText = "Braunkohle ges.: 8613MW/ " +
                "Steinkohle ges.: 0MW/ Gas ges.: 603MW/ " +
                "Kernenergie ges.: 3960MW/ Gesamt-Summe: 13176MW " +
                "03.02.13/13:29";

        PowerGridValues pgv = (new TwitterHelperImpl()).parseTweet(tweetText);

        assertThat(pgv.getCoal(), is(8613));
        assertThat(pgv.getGas(), is(603));
        assertThat(pgv.getNuclear(), is(3960));

        assertThat(pgv.getOverallPower(), is(13176));
    }

}
