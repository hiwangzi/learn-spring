package dev.lukewang.spring.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bean1.class);

    public Bean1() {
        LOGGER.info("Bean1 constructor called");
    }
}
