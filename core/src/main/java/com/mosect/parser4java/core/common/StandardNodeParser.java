package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;

import java.util.List;

/**
 * 标准的节点解析器
 */
public abstract class StandardNodeParser extends CommonNodeParser {

    @Override
    protected void onParse(List<Token> tokenList, int offset) {
        if (offset < tokenList.size()) {
            Token token = tokenList.get(offset);
            if (isNodeStart(offset, token)) {
                // 节点开始
                Node node = createNode();
                setNode(node);
                onAddNodeChild(offset, token);
                int curOffset = offset + 1;
                int tokenEnd = tokenList.size();
                while (curOffset < tokenList.size()) {
                    Token curToken = tokenList.get(curOffset);
                    if (isNodeEnd(curOffset, curToken)) {
                        // 节点结束
                        onAddNodeChild(curOffset, curToken);
                        tokenEnd = curOffset + 1;
                        break;
                    }
                    curOffset = onProcessNodeToken(curOffset, curToken);
                }
                finishParse(true, tokenEnd);
            } else {
                finishParse(false, offset);
            }
        } else {
            finishParse(false, offset);
        }
    }

    /**
     * 为节点添加token
     *
     * @param offset token偏移量
     * @param child  子节点
     */
    protected void onAddNodeChild(int offset, Node child) {
        getNode().addChild(child);
    }

    /**
     * 判断节点是否开始
     *
     * @param offset 偏移量
     * @param token  token
     * @return true，节点开始；false，节点未开始
     */
    protected boolean isNodeStart(int offset, Token token) {
        return false;
    }

    /**
     * 判断节点是否结束
     *
     * @param offset 偏移量
     * @param token  token
     * @return true，节点结束；false，节点未结束
     */
    protected boolean isNodeEnd(int offset, Token token) {
        return false;
    }

    /**
     * 处理节点token
     *
     * @param offset 偏移量
     * @param token  token
     * @return 处理后的token偏移量
     */
    protected int onProcessNodeToken(int offset, Token token) {
        return offset + 1;
    }

    /**
     * 创建空白节点
     *
     * @return 空白节点
     */
    protected Node createNode() {
        return new CommonNode(getName());
    }
}
