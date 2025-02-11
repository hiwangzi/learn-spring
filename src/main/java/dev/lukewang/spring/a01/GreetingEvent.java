package dev.lukewang.spring.a01;

import org.springframework.context.ApplicationEvent;

public class GreetingEvent extends ApplicationEvent {

    public GreetingEvent(Object source) {
        super(source);
    }

}
