package net.ali.eventbus.api;

import java.util.function.Consumer;

/**
 * An interface representing a eventbus that can register listeners and invoke events
 */
public interface IEventBus {

    /**
     * Registers a given listener that listen for given events
     *
     * @param listener a listener
     */
    void registerListener(IEventListener listener);

    /**
     * Unregisters a given listener
     *
     * @param listener a listener already registered
     */
    void unregisterListener(IEventListener listener);

    /**
     * Clears all listeners
     */
    void clearListeners();

    /**
     * Fires a event asynchronously by invoking all listeners listening to the given event
     *
     * @param event a event
     */
    void fireEventASync(IEvent event);

    /**
     * Fires a event synchronously by invoking all listeners listening to the given event
     *
     * @param event a event
     * @return event the event fired
     */
    <T extends IEvent> T fireEventSync(T event);

    /**
     * Fires a event asynchronously by invoking all listeners listening to the given event.
     * Allows callbacks to be specified that will be invoked after each listener is invoked
     *
     * @param event a event
     * @param callbacks a callback
     */
    <T extends IEvent> void fireEventASync(T event, Consumer<T> callbacks);
}
