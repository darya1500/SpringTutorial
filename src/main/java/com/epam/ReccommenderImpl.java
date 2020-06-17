package com.epam;

import javax.annotation.PostConstruct;

@Singleton
@Deprecated
public class ReccommenderImpl implements Reccommender {
    @InjectProperty("whiskey")
    private String alcohol;

    public ReccommenderImpl() {
        System.out.println("recommendator was created");
    }

    @Override
    public void reccommend() {
        System.out.println("to protect from covid-19 drink " + alcohol);
    }
}
