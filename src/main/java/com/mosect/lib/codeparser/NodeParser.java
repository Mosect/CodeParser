package com.mosect.lib.codeparser;

/**
 * 节点解析器
 */
public interface NodeParser {

    /**
     * 解析节点
     *
     * @param text   文本
     * @param start  文本开始位置
     * @param offset 文本偏移量
     * @param end    文本结束位置
     * @param out    解析结果
     * @return true，含有解析内容；false，无解析内容
     */
    boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out);
}
