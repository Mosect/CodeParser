package com.mosect.parser4java.core;

import java.util.List;

/**
 * 父解析器，对于无法准确判断自己结束位置的节点，需要通过父解析来决定自己本身是否解析完毕
 */
public interface ParentParser {

    /**
     * 结束解析
     *
     * @param source    文本来源
     * @param outErrors 输出错误
     * @return true，表示父解析器已经解析完毕，子解析器不能再继续解析；false，没有解析完成，子解析器可以继续解析
     */
    boolean endParse(TextSource source, List<ParseError> outErrors);
}
