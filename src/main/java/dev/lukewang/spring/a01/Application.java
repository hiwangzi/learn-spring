package dev.lukewang.spring.a01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
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
        // 3. ApplicationContext能多做什么（ApplicationContext组合并扩展了BeanFactory的功能）
        // 3.1 国际化 message
        String greeting = context.getMessage("greeting", new Object[]{LocalDateTime.now()}, Locale.JAPANESE);
        logger.info(greeting);
        greeting = context.getMessage("greeting", new Object[]{LocalDateTime.now()}, Locale.CHINA);
        logger.info(greeting);
        // 3.2 资源管理 resources
        Resource resource = context.getResource("classpath:application.properties");
        logger.info("[context.getResource(\"classpath:application.properties\")] {}", resource.getURI());
        Resource[] resources = context.getResources("classpath:application.properties");
        for (Resource r : resources) {
            logger.info("[context.getResources(\"classpath:application.properties\"] {}", r.getURI());
        }
        resources = context.getResources("classpath*:application.properties");
        for (Resource r : resources) {
            logger.info("[context.getResources(\"classpath*:application.properties\")] {}", r.getURI());
        }
        logger.info("-------");
        resource = context.getResource("classpath:META-INF/spring.factories");
        logger.info("[context.getResource(\"classpath:META-INF/spring.factories\")] {}", resource.getURI());
        resources = context.getResources("classpath:META-INF/spring.factories");
        for (Resource r : resources) {
            logger.info("[context.getResources(\"classpath:META-INF/spring.factories\")] {}", r.getURI());
        }
        resources = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource r : resources) {
            logger.info("[context.getResources(\"classpath*:META-INF/spring.factories\")] {}", r.getURI());
        }
        // 3.3 环境变量/属性 environment
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.getPropertySources();
        logger.info(environment.getProperty("java_Home"));
        logger.info(environment.getProperty("spring.application.name"));
        // 3.4 事件 event
        context.publishEvent(new GreetingEvent(context));
    }
}
