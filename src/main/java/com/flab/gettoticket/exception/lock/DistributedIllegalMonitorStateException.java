package com.flab.gettoticket.exception.lock;

public class DistributedIllegalMonitorStateException extends DistributedLockException{
    public DistributedIllegalMonitorStateException(String message) {
        super(message);
    }

    public DistributedIllegalMonitorStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
