package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.common.CommonNode;

/**
 * 语句节点
 */
public class SentenceNode extends CommonNode {

    private final String sentenceType;

    public SentenceNode(String type, String sentenceType) {
        super(type);
        this.sentenceType = sentenceType;
    }

    /**
     * 获取语句类型
     *
     * @return 语句类型
     */
    public String getSentenceType() {
        return sentenceType;
    }
}
