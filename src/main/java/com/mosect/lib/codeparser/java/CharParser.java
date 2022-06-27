package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.ErrorInfo;
import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;

public class CharParser implements NodeParser {

    protected CharHandler charHandler = new CharHandler();

    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        int curOffset = offset;
        int state = 0;
        boolean loop = true;
        String error = null;
        char value = '\0';

        while (curOffset < end && loop) {
            switch (state) {
                case 0:
                    if (text.charAt(curOffset) == '\'') {
                        state = 1; // 符合字符开头
                        ++curOffset;
                    } else {
                        loop = false;
                    }
                    break;
                case 1:
                    if (text.charAt(curOffset) == '\'') {
                        state = 2; // 丢失字符内容
                        ++curOffset;
                        loop = false;
                    } else {
                        charHandler.handle(text, curOffset, end);
                        error = charHandler.getError();
                        curOffset = charHandler.getCharEnd();
                        value = charHandler.getCharValue();
                        state = 3; // 已解析内容
                    }
                    break;
                case 3:
                    if (text.charAt(curOffset) == '\'') {
                        state = 4; // 合法的字符
                        ++curOffset;
                    }
                    loop = false;
                    break;
            }
        }
        switch (state) {
            case 1:
                error = "MISSING_CHAR_CONTENT_AND_END";
                break;
            case 2:
                error = "MISSING_CHAR_CONTENT";
                break;
            case 3:
                error = "MISSING_CHAR_END";
                break;
            case 4:
                break;
            default:
                return false;
        }
        out.node = new CharToken("char", text.subSequence(offset, curOffset).toString(), value);
        out.node.setError(ErrorInfo.make(error, text, start, end, curOffset));
        out.offset = curOffset;
        return true;
    }
}
