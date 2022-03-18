package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;

import java.util.List;

/**
 * 空白字符节点解析器
 */
public class WhitespaceNodeFactory implements NodeFactory {

    @Override
    public String getName() {
        return "whitespace";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        int pos = source.getPosition();
        CharSequence text = source.getText();
        char first = text.charAt(pos);
        if (isWhitespaceChar(first)) {
            int end = text.length();
            for (int i = pos + 1; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (!isWhitespaceChar(ch)) {
                    end = i;
                    break;
                }
            }
            String tokenText = text.subSequence(pos, end).toString();
            source.setPosition(end);
            return new CommonToken("", "", "whitespace", tokenText);
        }
        return null;
    }

    /**
     * 创建空白字符节点
     *
     * @param whitespaceText 空白字符串
     * @return 空白字符节点
     */
    public Token create(String whitespaceText) {
        if (whitespaceText.length() > 0) {
            for (int i = 0; i < whitespaceText.length(); i++) {
                char ch = whitespaceText.charAt(i);
                if (!isWhitespaceChar(ch)) {
                    throw new IllegalArgumentException("Invalid whitespace text");
                }
            }
            return new CommonToken("", "", "whitespace", whitespaceText);
        } else {
            throw new IllegalArgumentException("Empty whitespace text");
        }
    }

    /**
     * 判断是否为空白字符
     *
     * @param ch 字符
     * @return true，为空白字符；false，非空白字符
     */
    public boolean isWhitespaceChar(char ch) {
        switch (ch) {
            case '\t':
            case ' ':
                return true;
            default:
                return false;
        }
    }
}
