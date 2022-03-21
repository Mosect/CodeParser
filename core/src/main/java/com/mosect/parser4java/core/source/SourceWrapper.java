package com.mosect.parser4java.core.source;

import com.mosect.parser4java.core.TextSource;

public class SourceWrapper extends TextSource {

    private final TextSource target;
    private int offset = 0;
    private int length;
    private final CharSequence text = new CharSequence() {
        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int i) {
            return target.getText().charAt(offset + i);
        }

        @Override
        public CharSequence subSequence(int s, int e) {
            return target.getText().subSequence(s + offset, e + offset);
        }

        @Override
        public String toString() {
            return target.getText().subSequence(offset, offset + length).toString();
        }
    };

    public SourceWrapper(TextSource target) {
        this.target = target;
        this.length = target.length();
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public CharSequence getText() {
        return text;
    }

    public void clip(int start, int end) {
        if (start < 0 || start >= target.length()) {
            throw new IllegalArgumentException("Invalid start: " + start);
        }
        if (end < start || end > target.length()) {
            throw new IllegalArgumentException("Invalid end: " + end);
        }
        offset = start;
        length = end - start;
    }

    public void clip(int end) {
        clip(0, end);
    }

    public void clearClip() {
        offset = 0;
        length = target.length();
    }
}
