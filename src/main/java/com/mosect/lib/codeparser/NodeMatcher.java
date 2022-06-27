package com.mosect.lib.codeparser;

import java.util.List;

/**
 * 节点匹配器
 */
public interface NodeMatcher {

    boolean matchNodes(Node node, int start, int end);

    boolean matchNodes(List<Node> nodes, int start, int end);
}
