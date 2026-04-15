package com.flowapprove.application.cqrs;

public interface CommandBus {
    <TResult> TResult dispatch(Command<TResult> command);
}
