package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 数字token
 */
public class NumberToken extends CommonToken {

    private final Number value;
    private final int radix;
    private final boolean integer;

    public NumberToken(String type, String text, Number value, int radix, boolean integer) {
        super(type, text);
        this.value = value;
        this.radix = radix;
        this.integer = integer;
    }

    public Number getValue() {
        return value;
    }

    public int getRadix() {
        return radix;
    }

    public boolean isInteger() {
        return integer;
    }
}
