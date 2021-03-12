package net.ali.eventbus.api;

/**
 * A enum that represents different event priorities.
 * The higher the priority the earlier it will be invoked with respect to other listener handlers.
 */
public enum Priority {
    VERY_LOW(0), LOW(1), MEDIUM(2), HIGH(3), VERY_HIGH(4);

    public int val;

    Priority(int val) {
        this.val = val;
    }

}
