package com.mosect.parser4java.core;

/**
 * 文本解析器
 */
public interface TextParser extends Parser {

    /**
     * 解析文本
     *
     * @param text  文本
     * @param start 开始位置
     */
    void parse(CharSequence text, int start);

    /**
     * 构建token
     *
     * @return token对象
     */
    Token makeToken();
}
