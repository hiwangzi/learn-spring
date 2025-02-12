package dev.lukewang.spring.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Bean2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bean2.class);

    // 构造方法注入
    private final Bean3 bean3;
    // setter注入
    private Bean4 bean4;

    public Bean2(Bean3 bean3) {
        LOGGER.info("Bean2 constructor called");
        this.bean3 = bean3;
    }

    @Autowired
    public void setBean4(Bean4 bean4) {
        this.bean4 = bean4;
    }

    public Bean3 getBean3() {
        return bean3;
    }

    public Bean4 getBean4() {
        return bean4;
    }
}
