package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.ErrorInfo;
import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;

public class StringParser implements NodeParser {

    protected CharHandler charHandler = new CharHandler();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        int curOffset = offset;
        int state = 0;
        boolean loop = true;
        String error = null;
        StringBuilder builder = null;

        while (curOffset < end && loop) {
            switch (state) {
                case 0:
                    if (text.charAt(curOffset) == getStartChar()) {
                        state = 1;
                        ++curOffset;
                        builder = new StringBuilder(32);
                    } else {
                        loop = false;
                    }
                    break;
                case 1:
                    if (text.charAt(curOffset) == getEndChar()) {
                        // 字符串结束
                        state = 2;
                        loop = false;
                        ++curOffset;
                    } else if (text.charAt(curOffset) == '\r' || text.charAt(curOffset) == '\n') {
                        loop = false;
                    } else {
                        charHandler.handle(text, curOffset, end);
                        error = charHandler.getError();
                        curOffset = charHandler.getCharEnd();
                        builder.append(charHandler.getCharValue());
                    }
                    break;
            }
        }

        if (state == 0) return false;
        if (state == 1) {
            error = "MISSING_STRING_END";
        }
        String str = text.subSequence(offset, curOffset).toString();
        out.offset = curOffset;
        out.node = createStringToken(str, builder.toString());
        out.node.setError(ErrorInfo.make(error, text, start, end, curOffset));
        return true;
    }

    protected StringToken createStringToken(String text, String value) {
        return new StringToken("string", text, value);
    }

    protected char getStartChar() {
        return '"';
    }

    protected char getEndChar() {
        return '"';
    }
}
