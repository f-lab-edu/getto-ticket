package com.flab.gettoticket.exception.lock;

public abstract class DistributedLockException extends RuntimeException {
    public DistributedLockException(String message) {
        super(message);
    }

    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
