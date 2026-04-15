package com.flowapprove.domain.common;

public abstract class AggregateRoot<T> {
    private final T id;

    protected AggregateRoot(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
