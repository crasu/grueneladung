package de.crasu.grueneladung;

public interface PowerGridInformationRetriever {
    Boolean isEnergyGreen();
    PowerGridValues parseTweet(String tweetText);
}
