# EventBus
Java EventBus Implementation

### Example usage:
1. Create an instance of the EventBus (or optionally create your own implementation)

```java
private IEventBus eventBus = new EventBus();
```

2. Create an event(s) to handle 
```java
public class ExampleEvent implements IEvent {
    public boolean foo;
    public int bar;
}
```

3. Implement IEventListener and add an event handler
```java
IEventListener listener = new IEventListener() {

    @EventHandler(events = ExampleEvent.class, priority = Priority.LOW)
    private final EventTask<ExampleEvent> onTestEvent = exampleEvent -> {
        exampleEvent.foo = true;
        exampleEvent.bar = 3;
    };
}
```

4. Register listener(s)
```java
eventBus.registerListener(listener);
```

5. Fire event(s)
```java
eventBus.fireEventSync(new ExampleEvent());
```