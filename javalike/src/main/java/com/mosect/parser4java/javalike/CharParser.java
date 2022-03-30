package com.mosect.parser4java.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.text.CharTextParser;
import com.mosect.parser4java.core.util.CharUtils;

/**
 * 字符文本解析器
 */
public class CharParser extends CommonTextParser {

    private CharTextParser charTextParser = new CharTextParser();

    @Override
    protected void onParse(CharSequence text, int start) {
        CharTextParser charTextParser = getCharTextParser();
        if (CharUtils.match(text, start, "'", false)) {
            int offset = start + 1;
            charTextParser.parse(text, offset);
            offset = charTextParser.getTextEnd();
            if (charTextParser.isSuccess()) {
                // 解析字符成功
                if (offset < text.length()) {
                    char endChar = text.charAt(offset);
                    ++offset;
                    if (endChar != '\'') {
                        // 丢失结束字符
                        putError("CHAR_INVALID_END", "Invalid char end", offset);
                    }
                } else {
                    putError("CHAR_MISSING_END", "Missing char end", offset);
                }
            } else {
                // 解析字符失败
                putError("CHAR_MISSING_CONTENT", "Missing char content", offset);
            }
            finishParse(true, offset);
        } else {
            finishParse(false, start);
        }
    }

    public char getValue() {
        if (isSuccess()) {
            return getCharTextParser().getValue();
        }
        return 0;
    }

    public CharTextParser getCharTextParser() {
        return charTextParser;
    }

    protected void setCharTextParser(CharTextParser charTextParser) {
        this.charTextParser = charTextParser;
    }

    @Override
    public String getName() {
        return "javalike.char";
    }
}
