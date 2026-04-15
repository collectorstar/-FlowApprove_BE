package com.flowapprove.infrastructure.cqrs;

import com.flowapprove.application.cqrs.Command;
import com.flowapprove.application.cqrs.CommandBus;
import com.flowapprove.application.cqrs.CommandHandler;
import java.util.List;

public class SimpleCommandBus implements CommandBus {
    private final List<CommandHandler<?, ?>> handlers;

    public SimpleCommandBus(List<CommandHandler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResult> TResult dispatch(Command<TResult> command) {
        return (TResult) handlers.stream()
                .filter(handler -> handler.commandType().equals(command.getClass()))
                .findFirst()
                .map(handler -> ((CommandHandler<Command<TResult>, TResult>) handler).handle(command))
                .orElseThrow(() -> new IllegalArgumentException("No command handler registered for " + command.getClass().getName()));
    }
}
