package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.ListTextMatcher;

public class WhitespaceMatcher extends ListTextMatcher {

    public WhitespaceMatcher() {
        register("\r").register("\n").register("\t").register("\f").register("\b").register(" ").register("\r\n");
        sort();
    }
}
