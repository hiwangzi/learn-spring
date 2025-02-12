package dev.lukewang.spring.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean3 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bean3.class);

    public Bean3() {
        LOGGER.info("Bean3 constructor called");
    }
}
