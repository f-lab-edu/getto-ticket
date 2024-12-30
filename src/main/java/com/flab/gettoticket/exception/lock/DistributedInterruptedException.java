package com.flab.gettoticket.exception.lock;

public class DistributedInterruptedException extends DistributedLockException{
    public DistributedInterruptedException(String message) {
        super(message);
    }

    public DistributedInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
