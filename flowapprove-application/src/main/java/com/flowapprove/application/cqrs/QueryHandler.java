package com.flowapprove.application.cqrs;

public interface QueryHandler<TQuery extends Query<TResult>, TResult> {
    Class<TQuery> queryType();

    TResult handle(TQuery query);
}
