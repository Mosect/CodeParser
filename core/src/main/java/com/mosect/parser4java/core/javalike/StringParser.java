package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CharTextParser;
import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

/**
 * 字符串解析器
 */
public class StringParser extends CommonTextParser {

    private final StringBuilder stringBuilder;
    private String string;
    private CharTextParser charTextParser = new CharTextParser();

    public StringParser() {
        stringBuilder = new StringBuilder(512);
    }

    @Override
    protected void onClear() {
        super.onClear();
        string = null;
        StringBuilder stringBuilder = getStringBuilder();
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        CharTextParser charTextParser = getCharTextParser();
        if (CharUtils.match(text, start, "\"", false)) {
            // " 开头
            int offset = start + 1;
            boolean loop = true;
            while (offset < text.length() && loop) {
                char ch = text.charAt(offset);
                switch (ch) {
                    case '"': // 字符串结束
                        loop = false;
                        ++offset;
                        break;
                    case '\r':
                    case '\n': // 字符串遇到意外换行，错误的字符串
                        putError("STRING_CONTAINS_LINEFEED", "String contains linefeed", offset);
                        loop = false;
                        break;
                    default:
                        charTextParser.parse(text, offset);
                        if (charTextParser.isSuccess()) {
                            // 解析字符成功
                            stringBuilder.append(charTextParser.getValue());
                            offset = charTextParser.getTextEnd();
                        } else {
                            // 无法解析
                            if (charTextParser.hasError()) {
                                putErrorWithOther(charTextParser);
                            } else {
                                putError("STRING_INVALID", "Invalid string content", offset);
                            }
                            ++offset;
                        }
                        break;
                }
            }

            if (offset == start + 1) {
                putError("STRING_MISSING_END", "Missing string end char", offset);
            }
            finishParse(true, offset);
        } else {
            finishParse(false, start);
        }
    }

    protected StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public String getString() {
        if (isSuccess()) {
            if (null == string) {
                string = getStringBuilder().toString();
            }
            return string;
        }
        return null;
    }

    public CharTextParser getCharTextParser() {
        return charTextParser;
    }

    protected void setCharTextParser(CharTextParser charTextParser) {
        this.charTextParser = charTextParser;
    }

    @Override
    public String getName() {
        return "javalike.string";
    }
}
