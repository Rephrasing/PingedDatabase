package io.github.rephrasing.pinged.exceptions;

public class NoConnectionFoundException extends RuntimeException {

    public NoConnectionFoundException(String message) {
        super(message);
    }

    public NoConnectionFoundException() {
        super();
    }
}
