package org.apache.nifi.datageneration.validation;

public class ValidationException extends RuntimeException {
    public ValidationException(Throwable t) {
        super(t);
    }

    public ValidationException(String message) {
        super(message);
    }
}
