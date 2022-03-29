package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

/**
 * 文本块解析器
 */
public class TextBlockParser extends CommonTextParser {

    private final StringBuilder stringBuilder = new StringBuilder(512);
    private String string;

    @Override
    public String getName() {
        return "java.text.block";
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
        if (CharUtils.match(text, start, bound, false)) {
            int offset = start + 3;
            int end = text.length(); // 结束位置
            boolean endBound = false; // 是否含有结束边界
            int textStart = -1; // 文本开始位置
            String lastLinefeed = null; // 上一个换行符
            int lineCount = 0; // 文本行数量
            while (offset < text.length()) {
                if (CharUtils.match(text, offset, bound, false)) {
                    // 文本区域结束
                    end = offset + 3;
                    endBound = true;
                    if (textStart >= 0) {
                        stringBuilder.append(text, textStart, offset);
                    }
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
                    if (textStart >= 0) {
                        if (lineCount > 0) {
                            stringBuilder.append(lastLinefeed);
                        }
                        stringBuilder.append(text, textStart, offset);
                        ++lineCount;
                    }
                    offset += linefeed.length();
                    textStart = -1;
                    lastLinefeed = linefeed;
                    continue;
                }

                if (textStart < 0) {
                    if (!isWhitespaceChar(text.charAt(offset))) {
                        textStart = offset;
                    }
                }
                ++offset;
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
