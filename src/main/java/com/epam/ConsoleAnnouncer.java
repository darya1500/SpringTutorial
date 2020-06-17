package com.epam;


public class ConsoleAnnouncer implements Announcer{
    @InjectByType
    private Reccommender reccommender;

    public void announce(String message) {
        System.out.println(message);
        reccommender.reccommend();
    }
}
