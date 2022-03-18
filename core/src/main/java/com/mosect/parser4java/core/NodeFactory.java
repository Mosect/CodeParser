package com.mosect.parser4java.core;

import java.util.List;

/**
 * 节点工厂
 */
public interface NodeFactory {

    /**
     * 工厂名称
     *
     * @return 工厂名称
     */
    String getName();

    /**
     * 解析节点
     *
     * @param parentParser 父解析器
     * @param source       文本来源
     * @param outErrors    输出的错误
     * @return 节点
     * @throws ParseException 解析异常
     */
    Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException;
}
