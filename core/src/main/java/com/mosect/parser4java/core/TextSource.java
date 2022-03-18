package com.mosect.parser4java.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 文本源
 */
public abstract class TextSource {

    private int position;

    public void setPosition(int position) {
        this.position = Math.max(position, 0);
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return "unknown";
    }

    /**
     * 调整错误，主要是获取错误的行数、行偏移量
     *
     * @param errors 错误列表
     */
    public void adjustErrors(List<ParseError> errors) {
        if (errors.isEmpty()) return;

        List<ParseError> safeErrors = new ArrayList<>(errors);
        safeErrors.sort(Comparator.comparingInt(ParseError::getPosition));
        int curPos = errors.get(0).getPosition();
        int curIndex = 0;

        CharSequence text = getText();
        int lineNumber = 0;
        int lineOffset = 0;
        boolean _r = false;
        for (int charOffset = 0; charOffset < text.length() && curIndex < errors.size(); charOffset++) {
            char ch = text.charAt(charOffset);
            if (ch == '\r') {
                // \r换行
                ++lineNumber;
                lineOffset = 0;
                _r = true;
            } else if (ch == '\n') {
                // \n换行
                if (!_r) {
                    // \n
                    ++lineNumber;
                    lineOffset = 0;
                } else {
                    // \r\n 忽略，无需增加lineNumber
                    _r = false;
                }
            } else {
                if (_r) _r = false;
                ++lineOffset;
            }

            if (charOffset == curPos) {
                for (int errorIndex = 0; errorIndex < safeErrors.size(); errorIndex++) {
                    curIndex = errorIndex;
                    ParseError error = safeErrors.get(errorIndex);
                    if (error.getPosition() == charOffset) {
                        error.setLineOffset(lineOffset);
                        error.setLineNumber(lineNumber);
                    } else {
                        curPos = error.getPosition();
                        break;
                    }
                }
            }
        }
        for (int i = curIndex; i < safeErrors.size(); i++) {
            ParseError error = safeErrors.get(i);
            error.setLineNumber(lineNumber);
            error.setLineOffset(lineOffset);
        }
    }

    /**
     * 获取文本
     *
     * @return 文本
     */
    public abstract CharSequence getText();

    /**
     * 判断是否有更多字符
     *
     * @return true，有更多字符可以使用；false，已没有字符可以使用
     */
    public boolean hasMore() {
        return getPosition() < getText().length();
    }

    /**
     * 剩余可使用的字符数量
     *
     * @return 可使用的字符数量
     */
    public int hasCount() {
        return getText().length() - getPosition();
    }

    /**
     * 判断当前是否匹配字符串
     *
     * @param str 字符串
     * @return true，已匹配；false，无法匹配
     */
    public boolean match(String str) {
        int count = hasCount();
        if (count >= str.length()) {
            CharSequence text = getText();
            int pos = getPosition();
            for (int i = 0; i < str.length(); i++) {
                char strChar = str.charAt(i);
                char textChar = text.charAt(pos + i);
                if (strChar != textChar) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
