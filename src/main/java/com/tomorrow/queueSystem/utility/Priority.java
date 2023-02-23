package com.tomorrow.queueSystem.utility;

public enum Priority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    public final int priority;

    Priority(int priority) {
        this.priority = priority;
    }
}
