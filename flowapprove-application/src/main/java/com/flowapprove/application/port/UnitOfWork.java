package com.flowapprove.application.port;

public interface UnitOfWork {
    <T> T execute(TransactionCallback<T> callback);

    interface TransactionCallback<T> {
        T doInTransaction();
    }
}
