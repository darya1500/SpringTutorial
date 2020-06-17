package com.epam;

import java.util.Map;

public class Application {
    public static ApplicationContext run(String packageToScan, Map<Class, Class> ifc2ImplClass) {
        JavaConfig config = new JavaConfig(packageToScan, ifc2ImplClass);
        ApplicationContext context = new ApplicationContext(config);
        MyObjectFactory objectFactory = new MyObjectFactory(context);
        //todo init all singletons are not lazy
        context.setObjectFactory(objectFactory);
        return context;
    }
}
