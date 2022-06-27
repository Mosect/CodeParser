package com.mosect.lib.codeparser;

/**
 * 文本匹配器
 */
public interface TextMatcher {

    /**
     * 匹配文本
     *
     * @param text   文本
     * @param offset 文本偏移量
     * @param end    文本结束
     * @return 匹配的文本
     */
    String match(CharSequence text, int offset, int end);
}
