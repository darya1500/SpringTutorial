package com.epam;


//у класса 7+ responsibilities

public class CoronaDesinfector {
    //это не инверсия контроля, это lookup
    //myObjectFactory.createObject(Announcer.class)
    //context.getObject(Announcer.class)
    //утечка инфраструктуры в бизнес логику
    //private Announcer announcer= MyObjectFactory.getInstance().createObject(Announcer.class);
    //private Policeman policeman= MyObjectFactory.getInstance().createObject(Policeman.class);
    //нужна инверсия контроля- don't call us, we call you
    @InjectByType
    private Announcer announcer;
    @InjectByType
    private Policeman policeman;

    public void start(Room room) {
        // сообщить всем присутствующим в комнате о начале дезинфекции
        announcer.announce("начинаем дезинфекцию, всем вон");
        // разогнать всех кто не вышел после объявления
        policeman.makePeopleLeaveRoom();
        desinfect(room);
        // сообщить всем присутствующим в комнате что они могут вернуться обратно
        announcer.announce("можете рискнуть и зайти внутрь");
    }

    //single responsibility этого класса-desinfect
    private void desinfect(Room room) {
        System.out.println("зачитывается молитва: корона изыйди-молитва прочитана, вирус побежден");
    }
}
