package com.epam;

import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

//централизованное место для создания всех объектов
//-гибкость- если надо менять имплементацию не надо лезть в код
//-перед тем как фабрика отдаст объект, она его может настроить согласно нашим конвенциям, которые мы придумаем
//- в будущем можно будет кэшировать синглетоны
//(stateless-уже в конструкторе добавлен state и не будет меняться), клон нарушает бизнес логикубвсе сервисы д б синглетоны
public class MyObjectFactory {
    private static MyObjectFactory objectFactory;
    private ApplicationContext context;
    private Config config;
    private List<ObjectConfigurator> configurators = new ArrayList<>();
    private List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @SneakyThrows
    public MyObjectFactory(ApplicationContext context) {
        // config = new JavaConfig("com.epam", new HashMap<Class, Class>(Map.of(Policeman.class, PolicemanImpl.class)));
        this.context = context;
        for (Iterator<Class<? extends ObjectConfigurator>> it = context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class).iterator(); it.hasNext(); ) {
            Class<? extends ObjectConfigurator> aClass = it.next();
            configurators.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Iterator<Class<? extends ProxyConfigurator>> it = context.getConfig().getScanner().getSubTypesOf(ProxyConfigurator.class).iterator(); it.hasNext(); ) {
            Class<? extends ProxyConfigurator> aClass = it.next();
            proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> implClass) {
        T t = create(implClass);
        configure(t);
        invokeInit(implClass, t);
        t = wrapWithProxyIfNeeded(implClass, t);
        return t;
    }

    private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T t) {
        for (ProxyConfigurator proxyConfigurator:proxyConfigurators){
            t=(T)proxyConfigurator.replaceWithProxyIfNeeded(t,implClass);
        }
        return t;
    }

    @SneakyThrows
    private <T> void invokeInit(Class<T> implClass, T t) {
        for (Method methd : implClass.getMethods()) {
            if (methd.isAnnotationPresent(PostConstruct.class)) {
                methd.invoke(t);
            }
        }
    }

    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }
}
