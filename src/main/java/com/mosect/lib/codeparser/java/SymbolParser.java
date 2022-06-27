package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;
import com.mosect.lib.codeparser.TextMatcher;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.util.TextUtils;

public class SymbolParser implements NodeParser {

    protected TextMatcher symbolMatcher = new SymbolMatcher();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        String symbol = symbolMatcher.match(text, offset, end);
        if (TextUtils.notEmpty(symbol)) {
            out.node = new Token("symbol", symbol);
            out.offset = offset + symbol.length();
            return true;
        }
        return false;
    }
}
