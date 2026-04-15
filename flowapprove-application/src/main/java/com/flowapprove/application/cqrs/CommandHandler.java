package com.flowapprove.application.cqrs;

public interface CommandHandler<TCommand extends Command<TResult>, TResult> {
    Class<TCommand> commandType();

    TResult handle(TCommand command);
}
