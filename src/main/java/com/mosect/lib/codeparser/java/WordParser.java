package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;
import com.mosect.lib.codeparser.TextMatcher;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.util.TextUtils;

public class WordParser implements NodeParser {

    protected TextMatcher symbolMatcher = new SymbolMatcher();
    protected TextMatcher whitespaceMatcher = new WhitespaceMatcher();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        int wordEnd = end;
        int curOffset = offset;
        while (curOffset < end) {
            String symbol = symbolMatcher.match(text, curOffset, end);
            if (TextUtils.notEmpty(symbol)) {
                wordEnd = curOffset;
                break;
            }
            String whitespace = whitespaceMatcher.match(text, curOffset, end);
            if (TextUtils.notEmpty(whitespace)) {
                wordEnd = curOffset;
                break;
            }
            ++curOffset;
        }
        out.node = new Token("word", text.subSequence(offset, wordEnd).toString());
        out.offset = wordEnd;
        return true;
    }
}
