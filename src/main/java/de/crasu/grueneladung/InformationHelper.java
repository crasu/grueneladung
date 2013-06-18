package de.crasu.grueneladung;

import twitter4j.TwitterException;
import java.util.List;

public interface InformationHelper {
    List<PowerGridValues> retrievePowerInformation();
}
