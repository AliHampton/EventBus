package net.ali.eventbus.impl;

import net.ali.eventbus.api.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * An implementation of {@code IEventBus} that can store {@code IEventListener} to listener for given {@code IEvent}s.
 */
public class EventBus implements IEventBus {

    private final Map<Class<? extends IEvent>, List<HandlerEncapsulation>> EVENT_REGISTRY;
    private final ExecutorService EXECUTOR_SERVICE;

    public EventBus() {
        EVENT_REGISTRY = new HashMap<>();
        EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    }   

    @Override
    public void registerListener(IEventListener listener) {
        registerHandlers(listener);
    }

    @Override
    public void unregisterListener(IEventListener listener) {
        EVENT_REGISTRY.values().forEach(list -> list.removeIf(handler -> handler.getListener().equals(listener)));
    }

    @Override
    public void clearListeners() {
        EVENT_REGISTRY.clear();
    }

    @Override
    public void fireEventASync(IEvent event) {
        getHandlers(event.getClass()).forEach(handler -> EXECUTOR_SERVICE.submit(() -> handler.getTask().accept(event)));
    }

    @Override
    public <T extends IEvent> T fireEventSync(T event) {
        getHandlers(event.getClass()).forEach(handler -> handler.getTask().accept(event));
        return event;
    }

    @Override
    public <T extends IEvent> void fireEventASync(T event, Consumer<T> callback) {
        EXECUTOR_SERVICE.submit(() -> {
            getHandlers(event.getClass()).forEach(handler -> handler.getTask().accept(event));
            callback.accept(event);
        });
    }

    private void registerHandlers(IEventListener listener) {
        for (Field field : listener.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EventHandler.class) && field.getType().equals(EventTask.class)) {
                try {
                    field.trySetAccessible();
                    registerHandler(listener, field.getAnnotation(EventHandler.class), (EventTask<IEvent>) field.get(listener));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void registerHandler(IEventListener listener, EventHandler handler, EventTask<IEvent> task) {
        HandlerEncapsulation handlerEncapsulation = new HandlerEncapsulation(listener, task, handler.priority());
        for (Class<? extends IEvent> clazz : handler.events()) {
            if (EVENT_REGISTRY.containsKey(clazz)) {
                EVENT_REGISTRY.get(clazz).add(handlerEncapsulation);
                EVENT_REGISTRY.get(clazz).sort(Comparator.comparingInt(src -> ((HandlerEncapsulation) src).getPriority().val).reversed());
            } else {
                EVENT_REGISTRY.put(clazz, new CopyOnWriteArrayList<>(List.of(handlerEncapsulation)));
            }
        }
    }

    private List<HandlerEncapsulation> getHandlers(Class<? extends IEvent> clazz) {
        return EVENT_REGISTRY.getOrDefault(clazz, Collections.emptyList());
    }
}
