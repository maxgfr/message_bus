package com.maxgfr.utils;

import java.util.ArrayList;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import io.github.cdimascio.dotenv.Dotenv;

public class TwitterManager {

    private static TwitterManager single_instance = null;
    private ConfigurationBuilder cb = null;

    private TwitterManager() {
        this.cb = new ConfigurationBuilder();
        Dotenv dotenv = Dotenv.load();
        this.cb.setDebugEnabled(true)
          .setOAuthConsumerKey(dotenv.get("CONSUM_API_KEY"))
          .setOAuthConsumerSecret(dotenv.get("CONSUM_PRIV_API_KEY"))
          .setOAuthAccessToken(dotenv.get("ACCESS_TOKEN"))
          .setOAuthAccessTokenSecret(dotenv.get("ACCESS_TOKEN_SECRET"));
    }

    public static TwitterManager getInstance() {
            if (single_instance == null)
                    single_instance = new TwitterManager();
            return single_instance;
    }

    private ArrayList<String> getTrends(int code) {
        ArrayList<String> listTrends = new ArrayList<String>();
        try {
          Twitter twitter = new TwitterFactory(this.cb.build()).getInstance();
          Trends trends = twitter.getPlaceTrends(code);
          for (int i = 0; i < 10; i++) {
                  listTrends.add(trends.getTrends()[i].getName());
          }
        } catch(Exception e) {
          e.printStackTrace();
        }
        return listTrends;
    }

    public void saveTrends(String database_name) {
        DatabaseManager database_manager = DatabaseManager.getInstance();
        ArrayList<String> trends = this.getTrends(1);
        database_manager.createTable(database_name);
        //System.out.println(trends);
        database_manager.insertTable(database_name, trends.toString());
    }
}
