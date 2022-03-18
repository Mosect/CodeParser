package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonToken;

public class NumberToken extends CommonToken {

    protected NumberToken(String name, String text) {
        super("", name, "number", text);
    }
}
