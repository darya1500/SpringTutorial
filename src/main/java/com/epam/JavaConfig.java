package com.epam;

import org.reflections.Reflections;
import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config {
    private Reflections scanner;
    private Map<Class, Class> ifcToImplClass;

    public JavaConfig(String packageToScan, Map<Class, Class> ifcToImplClass) {
        this.scanner = new Reflections(packageToScan);
        this.ifcToImplClass = ifcToImplClass;
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        return ifcToImplClass.computeIfAbsent(ifc, Class -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
            if (classes.size() != 1) {
                throw new RuntimeException(ifc + " has 0 or more than 1 impl please update your config");
            }
            return classes.iterator().next();
        });
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }
}
