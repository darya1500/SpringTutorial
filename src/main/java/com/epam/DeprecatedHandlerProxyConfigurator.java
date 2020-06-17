package com.epam;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DeprecatedHandlerProxyConfigurator implements ProxyConfigurator {

    @Override
    public Object replaceWithProxyIfNeeded(Object t, Class implClass) {
        if (implClass.isAnnotationPresent(Deprecated.class)) {
            if (implClass.getInterfaces().length == 0) {
                //from cglib
                return Enhancer.create(implClass, new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return getInvocationHandlerLogic(t, method);
                    }


                });
            }
            return Proxy.newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(), (o, method, objects) -> getInvocationHandlerLogic(t, method));
        } else {
            return t;
        }
    }

    private Object getInvocationHandlerLogic(Object t, Method method) throws IllegalAccessException, InvocationTargetException {
        System.out.println("что ж ты делаешь урод");
        return method.invoke(t);
    }
}
