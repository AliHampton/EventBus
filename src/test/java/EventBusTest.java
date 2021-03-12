import net.ali.eventbus.api.*;
import net.ali.eventbus.impl.EventBus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EventBusTest {

    private IEventBus bus;

    @BeforeEach
    public void createEventBusInstance() {
        bus = new EventBus();
    }

    @Test
    public void testFireEventSync() {
        createAndRegisterSuccessListener();

        TestEvent event = bus.fireEventSync(new TestEvent()); //Fire a new TestEvent

        // Check if the listener EventTask was invoked
        Assertions.assertTrue(event.success, "The listener wasn't invoked or didn't update the success value");

    }

    @Test
    public void testFireEventASync() throws InterruptedException {
        createAndRegisterSuccessListener();

        TestEvent event = new TestEvent();

        bus.fireEventASync(event); //Fire the event

        Thread.sleep(100L); //Sleep to allow the handlers to run

        // Check if the listener EventTask was invoked
        Assertions.assertTrue(event.success, "The listener wasn't invoked or didn't update the success value (in time)");
    }

    @Test
    public void testFireEventASyncCallback() throws InterruptedException{
        //Create listeners that will arbitrarily modify the TestEvent class
        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.codes.add(0);
            };
        });

        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.codes.add(1);
            };
        });

        TestEvent event = new TestEvent();
        AtomicReference<TestEvent> eventCallback = new AtomicReference<>();

        bus.fireEventASync(event, eventCallback::set); //Fire the event

        Thread.sleep(100L); //Sleep to allow the handlers to run

        // Check if the callback is the same event object
        Assertions.assertEquals(event, eventCallback.get(), "The callback didn't return correctly or in time");
    }

    @Test
    public void testUnregisterListener() {
        IEventListener listener = createAndRegisterSuccessListener();

        bus.unregisterListener(listener); //Unregister the listener

        TestEvent event = bus.fireEventSync(new TestEvent());

        // Check if the listener EventTask was invoked
        Assertions.assertFalse(event.success, "The listener was invoked");
    }


    @Test
    public void testPrioritySync() {
        //Create three listeners that update the Event's code to different values
        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class, priority = Priority.LOW)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(1);
            };
        });

        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(2);
            };
        });

        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class, priority = Priority.HIGH)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(3);
            };
        });

        TestEvent event = bus.fireEventSync(new TestEvent()); //Fire the event

        //Check if the listeners were invoked in the correct order
        Assertions.assertEquals(List.of(3, 2, 1), event.codes, "The bus fired the events in the incorrect order");
    }

    @Test
    public void testPriorityASync() throws InterruptedException{
        //Create three listeners that update the Event's code to different values
        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class, priority = Priority.LOW)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(1);
            };
        });

        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(2);
            };
        });

        bus.registerListener(new IEventListener() {
            @EventHandler(events = TestEvent.class, priority = Priority.HIGH)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
                testEvent.codes.add(3);
            };
        });

        TestEvent event = new TestEvent();

        bus.fireEventASync(event); //Fire the event

        Thread.sleep(250L); //Sleep to allow the handlers to run

        //Check if the listeners were invoked in the correct order
        Assertions.assertEquals(List.of(3, 2, 1), event.codes, "The bus fired the events in the incorrect order");
    }

    public IEventListener createAndRegisterSuccessListener() {
        //Create a listener that will update the success value to true within the TestEvent class
        IEventListener listener = new IEventListener() {
            @EventHandler(events = TestEvent.class)
            private final EventTask<TestEvent> onTestEvent = testEvent -> {
                testEvent.success = true;
            };
        };
        bus.registerListener(listener);

        return listener;
    }
}
