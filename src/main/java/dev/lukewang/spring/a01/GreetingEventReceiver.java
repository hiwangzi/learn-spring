package dev.lukewang.spring.a01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GreetingEventReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingEventReceiver.class);

    @EventListener
    public void receive(GreetingEvent event) {
        LOGGER.info("Received greeting event: {}", event);
    }
}
