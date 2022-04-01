package com.mosect.parser4java.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 节点解析器工厂
 */
public abstract class NodeParserFactory {

    private final Map<String, LinkedList<NodeParser>> cache = new HashMap<>();

    /**
     * 使用名称获取解析器
     *
     * @param name 名称
     * @return 解析器
     */
    public NodeParser getByName(String name) {
        LinkedList<NodeParser> list = cache.computeIfAbsent(name, k -> new LinkedList<>());
        if (list.isEmpty()) {
            return createByName(name);
        }
        return list.removeFirst();
    }

    /**
     * 返回解析器
     *
     * @param parser 解析器
     */
    public void put(NodeParser parser) {
        LinkedList<NodeParser> list = cache.computeIfAbsent(parser.getName(), k -> new LinkedList<>());
        list.addLast(parser);
    }

    /**
     * 使用名称创建解析器
     *
     * @param name 名称
     * @return 解析器
     */
    protected abstract NodeParser createByName(String name);
}
