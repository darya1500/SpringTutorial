package com.epam;

import org.reflections.Reflections;

public interface Config {
    abstract <T> Class<? extends T> getImplClass(Class<T> ifc);

    Reflections getScanner();
}
