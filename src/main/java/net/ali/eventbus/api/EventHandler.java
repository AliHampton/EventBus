package net.ali.eventbus.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that specifies properties of a event handler.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * Defines the {@code IEvent}s a handler will be invoked on.
     *
     * @return an array of {@code IEvent}s
     */
    Class<? extends IEvent>[] events();

    /**
     * Defines the priority of a handler.
     * Events will be invoked in order from highest priority to lowest.
     *
     * @return a {@code Priority} enum
     */
    Priority priority() default Priority.MEDIUM;
}

