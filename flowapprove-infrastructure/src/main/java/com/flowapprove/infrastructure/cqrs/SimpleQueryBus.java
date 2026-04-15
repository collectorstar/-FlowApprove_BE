package com.flowapprove.infrastructure.cqrs;

import com.flowapprove.application.cqrs.Query;
import com.flowapprove.application.cqrs.QueryBus;
import com.flowapprove.application.cqrs.QueryHandler;
import java.util.List;

public class SimpleQueryBus implements QueryBus {
    private final List<QueryHandler<?, ?>> handlers;

    public SimpleQueryBus(List<QueryHandler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResult> TResult dispatch(Query<TResult> query) {
        return (TResult) handlers.stream()
                .filter(handler -> handler.queryType().equals(query.getClass()))
                .findFirst()
                .map(handler -> ((QueryHandler<Query<TResult>, TResult>) handler).handle(query))
                .orElseThrow(() -> new IllegalArgumentException("No query handler registered for " + query.getClass().getName()));
    }
}
