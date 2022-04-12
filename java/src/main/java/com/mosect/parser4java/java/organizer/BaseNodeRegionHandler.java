package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.core.organizer.NodeRegionHandler;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.token.KeywordToken;
import com.mosect.parser4java.java.util.NodeUtils;

import java.util.Objects;

public abstract class BaseNodeRegionHandler implements NodeRegionHandler {

    @Override
    public int getRegionStart(NodeContext context, NodeRegion parent, int start, int offset) {
        Node srcNode = context.getSource().get(offset);
        if (srcNode.isToken()) {
            switch (srcNode.getType()) {
                case NameConstants.TOKEN_KEYWORD:
                    return getRegionStartWithKeyword(context, parent, start, offset, (KeywordToken) srcNode);
                case NameConstants.TOKEN_SYMBOL:
                    return getRegionStartWithSymbol(context, parent, start, offset, (Token) srcNode);
                default:
                    break;
            }
        }
        return -1;
    }

    @Override
    public EndState getRegionEnd(NodeContext context, NodeRegion region, int offset) {
        Node srcNode = context.getSource().get(offset);
        if (srcNode.isToken()) {
            switch (srcNode.getType()) {
                case NameConstants.TOKEN_KEYWORD:
                    return getRegionEndWithKeyword(context, region, offset, (KeywordToken) srcNode);
                case NameConstants.TOKEN_SYMBOL:
                    return getRegionEndWithSymbol(context, region, offset, (Token) srcNode);
                default:
                    break;
            }
        }
        return EndState.NONE;
    }

    @Override
    public int getForceEndIndex(NodeContext context, NodeRegion region) {
        return NodeUtils.trimEnd(context, region.getStart(), context.getRegionCount());
    }

    @Override
    public void onRegionAdded(NodeContext context, NodeRegion region) {

    }

    @Override
    public void onRegionClosed(NodeContext context, NodeRegion region) {

    }

    /**
     * 判断是否必须结束于某个节点，不会被其他区域影响而关闭
     *
     * @return 是否必须结束于某个节点
     */
    public boolean isMustEndWithNode() {
        return false;
    }

    /**
     * 判断区域区间是否为兄弟区域
     *
     * @param other 其他区域
     * @return 区域区间是否为兄弟区域
     */
    public boolean isBrother(NodeRegionHandler other) {
        switch (other.getName()) {
            case NameConstants.REGION_HANDLER_IMPORT:
            case NameConstants.REGION_HANDLER_PACKAGE:
                return true;
        }
        return Objects.equals(getName(), other.getName());
    }

    protected int getRegionStartWithKeyword(NodeContext context, NodeRegion parent, int start, int offset, KeywordToken token) {
        if (isRegionStartKeyword(token)) {
            if (isTrimStart()) {
                return NodeUtils.trimStart(context, start, offset);
            }
            return offset;
        }
        return -1;
    }

    protected int getRegionStartWithSymbol(NodeContext context, NodeRegion parent, int start, int offset, Token token) {
        if (isRegionStartSymbol(token)) {
            if (isTrimStart()) {
                return NodeUtils.trimStart(context, start, offset);
            }
            return offset;
        }
        return -1;
    }

    protected EndState getRegionEndWithKeyword(NodeContext context, NodeRegion region, int offset, KeywordToken token) {
        if (isRegionEndKeyword(token)) {
            return EndState.CLOSED_AND_CONSUMED;
        }
        return EndState.NONE;
    }

    protected EndState getRegionEndWithSymbol(NodeContext context, NodeRegion region, int offset, Token token) {
        if (isRegionEndSymbol(token)) {
            return EndState.CLOSED_AND_CONSUMED;
        }
        return EndState.NONE;
    }

    protected boolean isTrimStart() {
        return true;
    }

    protected boolean isRegionStartKeyword(KeywordToken token) {
        return false;
    }

    protected boolean isRegionStartSymbol(Token token) {
        return false;
    }

    protected boolean isRegionEndKeyword(KeywordToken token) {
        return false;
    }

    protected boolean isRegionEndSymbol(Token token) {
        return false;
    }
}
