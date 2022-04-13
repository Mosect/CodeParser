package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.text.CharTextParser;
import com.mosect.parser4java.core.util.CharUtils;
import com.mosect.parser4java.java.Constants;

/**
 * 文本块解析器
 */
public class TextBlockParser extends CommonTextParser {

    private final StringBuilder stringBuilder = new StringBuilder(512);
    private CharTextParser charTextParser = new CharTextParser();
    private String string;

    @Override
    public String getName() {
        return Constants.PARSER_TEXT_BLOCK;
    }

    @Override
    protected void onClear() {
        super.onClear();
        StringBuilder stringBuilder = getStringBuilder();
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(0, stringBuilder.length());
        }
        string = null;
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        String bound = "\"\"\"";
        StringBuilder stringBuilder = getStringBuilder();
        CharTextParser charTextParser = getCharTextParser();
        if (CharUtils.match(text, start, bound, false)) {
            int offset = start + 3;
            int end = text.length(); // 结束位置
            boolean endBound = false; // 是否含有结束边界
            boolean textStart = false; // 文本是否开始
            String lastLinefeed = null; // 上一个换行符
            int lineCount = 0; // 文本行数量
            while (offset < text.length()) {
                if (CharUtils.match(text, offset, bound, false)) {
                    // 文本区域结束
                    end = offset + 3;
                    endBound = true;
                    break;
                }

                String linefeed;
                if (CharUtils.match(text, offset, "\r\n", false)) {
                    linefeed = "\r\n";
                } else if (CharUtils.match(text, offset, "\r", false)) {
                    linefeed = "\r";
                } else if (CharUtils.match(text, offset, "\n", false)) {
                    linefeed = "\n";
                } else {
                    linefeed = null;
                }

                if (null != linefeed) {
                    // 遇到换行
                    if (textStart) {
                        ++lineCount;
                    }
                    offset += linefeed.length();
                    textStart = false;
                    lastLinefeed = linefeed;
                    continue;
                }

                if (textStart || !isWhitespaceChar(text.charAt(offset))) {
                    if (!textStart) textStart = true;

                    if (lineCount > 0) {
                        stringBuilder.append(lastLinefeed);
                    }
                    charTextParser.parse(text, offset);
                    if (charTextParser.isSuccess()) {
                        stringBuilder.append(charTextParser.getValue());
                        offset = charTextParser.getTextEnd();
                    } else {
                        stringBuilder.append(text.charAt(offset));
                        ++offset;
                    }
                } else {
                    ++offset;
                }
            }

            if (!endBound) {
                // 不存在结束边界符
                putError("TEXT_BLOCK_MISSING_END", "Missing end bound", end);
            }
            finishParse(true, end);
        } else {
            finishParse(false, start);
        }
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new StringToken(Constants.TOKEN_TEXT_BLOCK, text, getString());
    }

    protected boolean isWhitespaceChar(char ch) {
        switch (ch) {
            case '\t':
            case ' ':
                return true;
            default: // 非空白符
                return false;
        }
    }

    protected StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public CharTextParser getCharTextParser() {
        return charTextParser;
    }

    protected void setCharTextParser(CharTextParser charTextParser) {
        this.charTextParser = charTextParser;
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
}
