package dev.lukewang.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;

@SpringBootApplication
public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ConfigurableBeanFactory beanFactory = context.getBeanFactory();
        // 1. 什么是BeanFactory
        //      - 是接口ApplicationContext的parent接口
        //      - 是Spring的核心容器，主要的ApplicationContext实现都组合了它的功能
        boolean bool = context.getBean(Application.class) == beanFactory.getBean(Application.class);
        logger.info("It should be true: {}", bool);
        // 2. BeanFactory能做什么
        //      - 表面上只有 getBean
        //      - 实际上控制反转，基本的依赖注入，Bean的生命周期的各种功能，都由它的实现类提供
        Field singletonObjectsField = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjectsField.setAccessible(true);
        Map<String, Object> singletonObjects = (Map<String, Object>) singletonObjectsField.get(beanFactory);
        singletonObjects.forEach((key, value) -> {
            if (key.equals("application")) {
                logger.info("[Bean] {}: {}", key, value);
            }
        });
    }
}
