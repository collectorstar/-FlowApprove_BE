package com.flowapprove.shared.error;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
