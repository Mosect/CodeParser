package com.mosect.lib.codeparser;

public class NodeException extends Exception {
    public NodeException() {
    }

    public NodeException(String message) {
        super(message);
    }

    public NodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeException(Throwable cause) {
        super(cause);
    }
}
