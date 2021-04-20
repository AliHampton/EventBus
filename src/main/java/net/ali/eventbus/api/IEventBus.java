package net.ali.eventbus.api;

import java.util.function.Consumer;

/**
 * An interface representing a eventbus that can register listeners and invoke events.
 */
public interface IEventBus {

    /**
     * Registers a given listener that listen for given events.
     *
     * @param listener a listener
     */
    void registerListener(IEventListener listener);

    /**
     * Unregisters a given listener.
     *
     * @param listener a listener already registered
     */
    void unregisterListener(IEventListener listener);

    /**
     * Clears all listeners.
     */
    void clearListeners();

    /**
     * Fires an event asynchronously by invoking all listeners listening to the given event.
     *
     * @param event an event
     */
    void fireEventASync(IEvent event);

    /**
     * Fires an event synchronously by invoking all listeners listening to the given event.
     *
     * @param event an event
     * @return event the event fired
     */
    <T extends IEvent> T fireEventSync(T event);

    /**
     * Fires an event asynchronously by invoking all listeners listening to the given event.
     * Once all listeners have been invoked the callback will be invoked.
     *
     * @param event an event
     * @param callback a callback
     */
    <T extends IEvent> void fireEventASync(T event, Consumer<T> callback);
}
