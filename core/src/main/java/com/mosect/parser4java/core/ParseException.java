package com.mosect.parser4java.core;

/**
 * 解析异常
 */
public class ParseException extends Exception {

    private final String errorId;
    private final int position;

    public ParseException(String errorId, int position) {
        this.errorId = errorId;
        this.position = position;
    }

    public ParseException(String message, String errorId, int position) {
        super(message);
        this.errorId = errorId;
        this.position = position;
    }

    public ParseException(String message, Throwable cause, String errorId, int position) {
        super(message, cause);
        this.errorId = errorId;
        this.position = position;
    }

    public ParseException(Throwable cause, String errorId, int position) {
        super(cause);
        this.errorId = errorId;
        this.position = position;
    }

    public String getErrorId() {
        return errorId;
    }

    public int getPosition() {
        return position;
    }
}
