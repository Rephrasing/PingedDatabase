package io.github.rephrasing.sparkbase.exceptions;

public class NoConnectionFoundException extends RuntimeException {

    public NoConnectionFoundException(String message) {
        super(message);
    }

    public NoConnectionFoundException() {
        super();
    }
}
