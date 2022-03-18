package com.mosect.parser4java.core.source;

import com.mosect.parser4java.core.TextSource;

public class StringSource extends TextSource {

    private final String string;

    @Override
    public String getName() {
        return "String";
    }

    public StringSource(String string) {
        this.string = string;
    }

    @Override
    public CharSequence getText() {
        return string;
    }
}
