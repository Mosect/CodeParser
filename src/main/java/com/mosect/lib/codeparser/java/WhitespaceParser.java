package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;
import com.mosect.lib.codeparser.TextMatcher;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.util.TextUtils;

public class WhitespaceParser implements NodeParser {

    protected TextMatcher whitespaceMatcher = new WhitespaceMatcher();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        String str = whitespaceMatcher.match(text, offset, end);
        if (TextUtils.notEmpty(str)) {
            out.node = new Token("whitespace", str);
            out.offset = offset + str.length();
            return true;
        }
        return false;
    }
}
