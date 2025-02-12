package dev.lukewang.spring.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;

public class BeanFactoryDemo {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryDemo.class);

    public static void main(String[] args) {
        demo();

        在关联AutowiredAnnotationBeanPostProcessor后会构造bean4并关联到成员变量();

        设置预实例化后会全部构造但不会自动设置bean2中的bean4成员变量();
    }

    private static void demo() {
        logger.info("[开始] demo");
        // BeanFactory 可以根据“定义”创建bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(Bean1.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("bean1", beanDefinition);
        for (String name : beanFactory.getBeanDefinitionNames()) {
            logger.debug(name);
        }
        beanFactory.getBeansOfType(Object.class).values()
                .forEach(v -> logger.debug(v.toString()));

        // 再增加一个bean `config`的定义
        beanFactory.registerBeanDefinition(
                "config",
                BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition()
        );
        // 为beanFactory实例添加一些常用的“处理器”，但是不会调用或者说关联
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        for (String name : beanFactory.getBeanDefinitionNames()) {
            logger.debug(name);
        }
        // 调用“BeanFactory后处理器”
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class)
                .values()
                .forEach(processor -> processor.postProcessBeanFactory(beanFactory));
        for (String name : beanFactory.getBeanDefinitionNames()) {
            logger.debug(name); // 此处可以发现自动创建了 bean2, bean3, bean4 对象定义，实际上还没有构造生成对象
        }
        beanFactory.getBeansOfType(Bean2.class).values() // 此时才可以看到先构造了bean3（构造方法注入），然后再构造bean2，但是bean4并没有被构造
                .forEach(v -> {
                    logger.debug(v.toString());
                    logger.debug("{}", v.getBean3());
                    logger.debug("{}", v.getBean4());
                });
        logger.info("[结束] demo\n\n\n");
    }

    private static void 在关联AutowiredAnnotationBeanPostProcessor后会构造bean4并关联到成员变量(){
        logger.info("[开始] 在关联AutowiredAnnotationBeanPostProcessor后会构造bean4并关联到成员变量");
        // BeanFactory 可以根据“定义”创建bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 增加一个bean `config`的定义
        beanFactory.registerBeanDefinition(
                "config",
                BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition()
        );
        // 为beanFactory实例添加一些常用的“处理器”
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        // 调用“BeanFactory后处理器”
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class)
                .values()
                .forEach(processor -> processor.postProcessBeanFactory(beanFactory));
        // 调用“Bean后处理器” AutowiredAnnotationBeanPostProcessor
        beanFactory.getBeansOfType(AutowiredAnnotationBeanPostProcessor.class)
                .values()
                .forEach(beanFactory::addBeanPostProcessor);
        // 可以看到先构造了bean3（构造方法注入），然后再构造bean2，然后再构造bean4
        logger.info("getBeansOfType 调用之前");
        beanFactory.getBeansOfType(Bean2.class).values()
                .forEach(v -> {
                    logger.debug(v.toString());
                    logger.debug("{}", v.getBean3());
                    logger.debug("{}", v.getBean4());
                });
        logger.info("[结束] 在关联AutowiredAnnotationBeanPostProcessor后会构造bean4并关联到成员变量\n\n\n");
    }

    private static void 设置预实例化后会全部构造但不会自动设置bean2中的bean4成员变量(){
        logger.info("[开始] 设置预实例化后会全部构造但不会自动设置bean2中的bean4成员变量");
        // BeanFactory 可以根据“定义”创建bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 增加一个bean `config`的定义
        beanFactory.registerBeanDefinition(
                "config",
                BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition()
        );
        // 为beanFactory实例添加一些常用的“处理器”
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        // 调用“BeanFactory后处理器”
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class)
                .values()
                .forEach(processor -> processor.postProcessBeanFactory(beanFactory));
        // ⚠️ 调用“Bean后处理器” AutowiredAnnotationBeanPostProcessor
        //beanFactory.getBeansOfType(AutowiredAnnotationBeanPostProcessor.class)
        //        .values()
        //        .forEach(beanFactory::addBeanPostProcessor);

        beanFactory.preInstantiateSingletons();
        // 可以看到在这里就已经构造了bean1, bean3（构造方法注入），然后再构造bean2，然后再构造bean4
        logger.info("getBeansOfType 调用之前");
        beanFactory.getBeansOfType(Bean2.class).values()
                .forEach(v -> {
                    logger.debug(v.toString());
                    logger.debug("{}", v.getBean3());
                    logger.debug("{}", v.getBean4()); // 此处仍然为 null，因为未调用 AutowiredAnnotationBeanPostProcessor 处理器
                });
        logger.info("[结束] 设置预实例化后会全部构造但不会自动设置bean2中的bean4成员变量\n\n\n");
    }
}


