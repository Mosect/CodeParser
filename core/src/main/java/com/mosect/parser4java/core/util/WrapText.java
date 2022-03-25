package com.mosect.parser4java.core.util;

public class WrapText implements CharSequence {

    private final CharSequence src;
    private int length;

    public WrapText(CharSequence src) {
        this.src = src;
        this.length = src.length();
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int i) {
        return src.charAt(i);
    }

    @Override
    public CharSequence subSequence(int s, int e) {
        return src.subSequence(s, e);
    }

    @Override
    public String toString() {
        return src.subSequence(0, length).toString();
    }
}
