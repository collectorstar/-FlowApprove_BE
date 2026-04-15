package com.flowapprove.application.cqrs;

public interface QueryBus {
    <TResult> TResult dispatch(Query<TResult> query);
}
