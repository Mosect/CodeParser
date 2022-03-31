package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.StandardNodeParser;

/**
 * 基础解析器
 */
public abstract class BaseParser extends StandardNodeParser {

    @Override
    protected int onProcessNodeToken(int offset, Token token) {
        switch (token.getType()) {
            case "java.comment": // 注释
            case "java.whitespace": // 空白符
                onAddNodeChild(offset, token);
                return offset + 1;
            default:
                return onProcessNodeValidToken(offset, token);
        }
    }

    /**
     * 处理节点有效token
     *
     * @param offset 偏移量
     * @param token  token
     * @return 解析后的位置
     */
    protected abstract int onProcessNodeValidToken(int offset, Token token);
}
