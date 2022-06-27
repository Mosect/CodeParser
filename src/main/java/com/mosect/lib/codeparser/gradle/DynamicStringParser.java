package com.mosect.lib.codeparser.gradle;

import com.mosect.lib.codeparser.ErrorInfo;
import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.java.CharHandler;
import com.mosect.lib.codeparser.java.StringToken;
import com.mosect.lib.codeparser.util.TextUtils;

public class DynamicStringParser implements NodeParser {

    protected CharHandler charHandler = new CharHandler();
    protected PlainStringParser plainStringParser = new PlainStringParser();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        int curOffset = offset;
        int state = 0;
        boolean loop = true;
        boolean inner = false;
        StringBuilder builder = null;
        String error = null;
        while (curOffset < end && loop) {
            if (state == 0) {
                if (text.charAt(curOffset) == '"') {
                    state = 1;
                    ++curOffset;
                    builder = new StringBuilder(32);
                } else {
                    loop = false;
                }
            } else {
                if (inner) {
                    if (text.charAt(curOffset) == '}') {
                        inner = false;
                        builder.append('}');
                        ++curOffset;
                    } else if (plainStringParser.parse(text, start, curOffset, end, out)) {
                        curOffset = out.offset;
                        builder.append(((Token) out.node).getText());
                    } else if (parse(text, start, curOffset, end, out)) {
                        curOffset = out.offset;
                        builder.append(((Token) out.node).getText());
                    } else {
                        builder.append(text.charAt(curOffset));
                        ++curOffset;
                    }
                } else {
                    if (TextUtils.match(text, curOffset, end, "${")) {
                        inner = true;
                        curOffset += 2;
                        builder.append("${");
                    } else if (text.charAt(curOffset) == '"') {
                        state = 2;
                        loop = false;
                        ++curOffset;
                    } else {
                        charHandler.handle(text, curOffset, end);
                        curOffset = charHandler.getCharEnd();
                        builder.append(charHandler.getCharValue());
                        error = charHandler.getError();
                    }
                }
            }
        }

        if (state == 0) return false;
        if (state == 1) {
            error = "MISSING_DYNAMIC_STRING_END";
        }
        String value = builder.toString();
        out.node = createStringToken(text.subSequence(offset, curOffset).toString(), value);
        out.node.setError(ErrorInfo.make(error, text, start, end, curOffset));
        out.offset = curOffset;
        return true;
    }

    protected StringToken createStringToken(String text, String value) {
        return new StringToken("string.dynamic", text, value);
    }
}
