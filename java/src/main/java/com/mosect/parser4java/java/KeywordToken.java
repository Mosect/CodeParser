package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonToken;

public class KeywordToken extends CommonToken {

    protected KeywordToken(String text) {
        super(text, text, "keyword", text);
    }
}
