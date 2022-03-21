package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 名称token
 */
public class NameToken extends CommonToken {

    protected NameToken(String text) {
        super(text, text, "name", text);
    }
}
