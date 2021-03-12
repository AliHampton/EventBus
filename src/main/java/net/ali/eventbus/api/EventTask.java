package net.ali.eventbus.api;

import java.util.function.Consumer;

/**
 * A functional interface that acts as a event handler that will be invoked when a event is fired.
 *
 * @param <T> an implementation of {@code IEvent}
 */
@FunctionalInterface
public interface EventTask<T extends IEvent> extends Consumer<T> {

}
