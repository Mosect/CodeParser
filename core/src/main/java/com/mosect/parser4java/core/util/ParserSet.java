package com.mosect.parser4java.core.util;

import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.source.SourceWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析器集合
 */
public abstract class ParserSet {

    protected List<TextParser> parsers = new ArrayList<>();
    private ParserSet childParserSet = null;

    /**
     * 添加解析器
     *
     * @param parser 解析器
     */
    public void addParser(TextParser parser) {
        parsers.add(parser);
    }

    /**
     * 设置子解析集合
     *
     * @param childParserSet 子解析集合
     */
    public void setChildParserSet(ParserSet childParserSet) {
        this.childParserSet = childParserSet;
    }

    /**
     * 获取子解析集合
     *
     * @return 子解析结合
     */
    public ParserSet getChildParserSet() {
        return childParserSet;
    }

    /**
     * 解析
     *
     * @param source 源
     * @param start  开始位置
     */
    public void parse(TextSource source, int start) {
        CharSequence text = source.getText();
        SourceWrapper sourceWrapper = new SourceWrapper(source);
        int unknownStart = start;
        int offset = start;
        while (offset < text.length()) {
            TextParser validParser = null;
            for (TextParser parser : parsers) {
                if (canParse(source, parser, offset, unknownStart)) {
                    parser.parse(text, offset);
                    if (parser.isPass()) {
                        validParser = parser;
                        break;
                    }
                }
            }
            if (null == validParser) {
                ++offset;
            } else {
                offset = validParser.getTextEnd();
                handleChildParserSet(sourceWrapper, unknownStart, validParser.getTextStart());
                unknownStart = validParser.getTextEnd();
                processParser(source, validParser);
            }
        }
        handleChildParserSet(sourceWrapper, unknownStart, text.length());
    }

    protected boolean canParse(TextSource source, TextParser parser, int offset, int unknownStart) {
        return true;
    }

    /**
     * 处理字解析集合
     *
     * @param sourceWrapper 源
     * @param unknownStart  未知开始位置
     * @param unknownEnd    未知结束位置
     */
    protected void handleChildParserSet(SourceWrapper sourceWrapper, int unknownStart, int unknownEnd) {
        if (unknownEnd > unknownStart) {
            ParserSet childParserSet = getChildParserSet();
            sourceWrapper.clip(unknownEnd);
            if (null == childParserSet) {
                throw new IllegalStateException("Child parser set not found");
            }
            childParserSet.parse(sourceWrapper, unknownStart);
        }
    }

    /**
     * 处理解析器
     *
     * @param source 源
     * @param parser 解析器
     */
    protected abstract void processParser(TextSource source, TextParser parser);
}
