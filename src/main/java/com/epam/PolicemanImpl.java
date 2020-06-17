package com.epam;

import javax.annotation.PostConstruct;

public class PolicemanImpl implements Policeman {

    @InjectByType
    private Reccommender reccommender;

    //где запускать init методы
    //-оставим в фабрике
    //-перенесем еще в 1 конфигуратор
    //-создадим новый тип конфигураторов
    @PostConstruct
    public void init() {
        System.out.println(reccommender.getClass());
    }

    @Override
    public void makePeopleLeaveRoom() {
        System.out.println(reccommender.getClass());
        //System.out.println("кыш кыш бах бах");
    }
}
