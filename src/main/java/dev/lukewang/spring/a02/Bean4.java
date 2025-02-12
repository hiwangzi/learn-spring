package dev.lukewang.spring.a02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean4 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bean4.class);

    public Bean4() {
        LOGGER.info("Bean4 constructor called");
    }
}
