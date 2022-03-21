package com.mosect.parser4java.core;

/**
 * 解析错误
 */
public class ParseError {

    private final String id;
    private final String text;
    private final int position;
    private int lineNumber;
    private int lineOffset;
    private int linePosition;

    public ParseError(String id, String text, int position) {
        this.id = id;
        this.text = text;
        this.position = position;
    }

    public ParseError(ParseError other) {
        this(other.getId(), other.getText(), other.getPosition());
        setLineNumber(other.getLineNumber());
        setLineOffset(other.getLineOffset());
        setLinePosition(other.getLinePosition());
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public void setLinePosition(int linePosition) {
        this.linePosition = linePosition;
    }

    @Override
    public String toString() {
        return "ParseError{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", position=" + position +
                ", lineNumber=" + lineNumber +
                ", lineOffset=" + lineOffset +
                ", linePosition=" + linePosition +
                '}';
    }
}
