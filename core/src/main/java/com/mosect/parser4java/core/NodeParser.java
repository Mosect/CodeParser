package com.mosect.parser4java.core;

import java.util.List;

/**
 * 节点解析器
 *
 * @param <C> 上下文类型
 */
public interface NodeParser<C> extends Parser {

    /**
     * 解析节点
     *
     * @param text       原始文本，可为null
     * @param tokens     token列表
     * @param tokenStart 开始的token下标
     */
    void parse(CharSequence text, List<Token> tokens, int tokenStart);

    /**
     * 检查节点
     *
     * @param text 原始文本，可为null
     * @param node 节点
     */
    void checkNode(CharSequence text, Node node);

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    C getContext();

    /**
     * 构建节点
     *
     * @return 节点对象
     */
    Node makeNode();
}
