package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonToken;

public class NumberToken extends CommonToken {

    private final NumberType numberType;
    private final Number value;

    protected NumberToken(String name, String text, NumberType numberType, Number value) {
        super("", name, "number", text);
        this.numberType = numberType;
        this.value = value;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public Number getValue() {
        return value;
    }
}
