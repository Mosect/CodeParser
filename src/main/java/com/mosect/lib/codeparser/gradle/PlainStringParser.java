package com.mosect.lib.codeparser.gradle;

import com.mosect.lib.codeparser.java.StringParser;
import com.mosect.lib.codeparser.java.StringToken;

public class PlainStringParser extends StringParser {

    @Override
    protected char getStartChar() {
        return '\'';
    }

    @Override
    protected char getEndChar() {
        return '\'';
    }

    @Override
    protected StringToken createStringToken(String text, String value) {
        return new StringToken("string.plain", text, value);
    }
}
