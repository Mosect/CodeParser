package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.util.TextUtils;

public class CharHandler {

    private char charValue;
    private int charEnd;
    private String error;

    public char getCharValue() {
        return charValue;
    }

    public int getCharEnd() {
        return charEnd;
    }

    public String getError() {
        return error;
    }

    public void handle(CharSequence text, int offset, int end) {
        int state = 0;
        int curOffset = offset;
        charValue = '\0';
        error = null;
        boolean loop = true;
        int size = 0;
        while (curOffset < end && loop) {
            char ch = text.charAt(curOffset);
            switch (state) {
                case 0:
                    if (ch == '\\') {
                        state = 1; // 开始转义
                    } else {
                        state = 2; // 普通字符
                        loop = false;
                    }
                    ++curOffset;
                    break;
                case 1:
                    if (ch == 'u') {
                        state = 3; // 16进制转义
                    } else if (TextUtils.isOctChar(ch)) {
                        state = 4; // 8进制转义
                        size = 1;
                    } else {
                        state = 5; // 普通转义
                        loop = false;
                    }
                    ++curOffset;
                    break;
                case 3: // 16进制转义
                    if (TextUtils.isHexChar(ch)) {
                        ++size;
                        if (size == 4) {
                            state = 6; // 16进制转义成功
                            loop = false;
                        }
                        ++curOffset;
                    } else {
                        state = 7; // 非法16进制转义
                        loop = false;
                    }
                    break;
                case 4: // 8进制转义
                    if (TextUtils.isOctChar(ch)) {
                        ++size;
                        if (size == 3) {
                            loop = false;
                        }
                        ++curOffset;
                    } else {
                        loop = false;
                    }
                    break;
            }
        }
        switch (state) {
            case 0:
                error = "NO_CHAR";
                break;
            case 1:
                error = "MISSING_ESCAPE_CONTENT";
                break;
            case 2:
                charValue = text.charAt(offset);
                break;
            case 3:
                error = "MISSING_HEX_CONTENT";
                break;
            case 4:
                charValue = (char) Integer.parseInt(text.subSequence(curOffset - size, curOffset).toString(), 8);
                break;
            case 5:
                char ch = text.charAt(curOffset - 1);
                switch (text.charAt(curOffset - 1)) {
                    case 'r':
                        charValue = '\r';
                        break;
                    case 'n':
                        charValue = '\n';
                        break;
                    case 't':
                        charValue = '\t';
                        break;
                    case 'b':
                        charValue = '\b';
                        break;
                    case 'f':
                        charValue = '\f';
                        break;
                    case '\'':
                        charValue = '\'';
                        break;
                    case '"':
                        charValue = '"';
                        break;
                    case '\\':
                        charValue = '\\';
                        break;
                    default:
                        charValue = ch;
                        break;
                }
                break;
            case 6:
                charValue = (char) Integer.parseInt(text.subSequence(curOffset - size, curOffset).toString(), 16);
                break;
            case 7:
                error = "INVALID_HEX_CONTENT";
                break;
        }
        charEnd = curOffset;
    }
}
