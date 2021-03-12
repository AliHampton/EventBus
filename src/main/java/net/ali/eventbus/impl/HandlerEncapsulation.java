package net.ali.eventbus.impl;

import net.ali.eventbus.api.EventTask;
import net.ali.eventbus.api.IEvent;
import net.ali.eventbus.api.IEventListener;
import net.ali.eventbus.api.Priority;

/**
 * A class that encapsulates data involved with a {@code IListener}'s {@code EventHandler}.
 */
public class HandlerEncapsulation {

    private final IEventListener listener;
    private final EventTask<IEvent> task;
    private final Priority priority;

    public HandlerEncapsulation(IEventListener listener, EventTask<IEvent> task, Priority priority) {
        this.listener = listener;
        this.task = task;
        this.priority = priority;
    }

    public IEventListener getListener() {
        return listener;
    }

    public EventTask<IEvent> getTask() {
        return task;
    }

    public Priority getPriority() {
        return priority;
    }
}
