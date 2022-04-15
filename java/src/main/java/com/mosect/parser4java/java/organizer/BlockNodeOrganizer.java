package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.node.MethodNode;
import com.mosect.parser4java.java.node.SentenceNode;
import com.mosect.parser4java.java.token.KeywordToken;
import com.mosect.parser4java.java.util.NodeUtils;

/**
 * 块节点组织器
 */
public class BlockNodeOrganizer extends JavaNodeOrganizer {

    @Override
    protected JavaNodeRegion onMatchRegion(NodeList nodeList, Node node, int start, int offset) {
        if (node.isToken()) {
            Token token = (Token) node;
            if (Constants.TOKEN_KEYWORD.equals(token.getType())) {
                switch (token.getText()) {
                    case "class":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_CLASS, nodeList, start, offset);
                    case "interface": {
                        int index = NodeUtils.beforeToken(Constants.TOKEN_SYMBOL, "@", nodeList, start, offset);
                        if (index >= 0) {
                            // 注解
                            return createTypeNodeRegion(Constants.CLASS_TYPE_ANNOTATION, nodeList, start, offset);
                        }
                        // 接口
                        return createTypeNodeRegion(Constants.CLASS_TYPE_INTERFACE, nodeList, start, offset);
                    }
                    case "enum":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_ENUM, nodeList, start, offset);
                    case "package":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_PACKAGE, nodeList, offset);
                    case "import":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_IMPORT, nodeList, offset);
                    case "goto":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_GOTO, nodeList, offset);
                    case "if":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_IF, nodeList, offset);
                    case "else":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_ELSE, nodeList, offset);
                    case "for":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_FOR, nodeList, offset);
                    case "switch":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_SWITCH, nodeList, offset);
                    case "do":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_DO, nodeList, offset);
                    case "while":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_WHILE, nodeList, offset);
                    case "throw":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_THROW, nodeList, offset);
                    case "try":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_TRY, nodeList, offset);
                    case "catch":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_CATCH, nodeList, offset);
                    case "finally":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_FINALLY, nodeList, offset);
                    case "assert":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_ASSERT, nodeList, offset);
                    case "break":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_BREAK, nodeList, offset);
                    case "continue":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_CONTINUE, nodeList, offset);
                    case "case":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_CASE, nodeList, offset);
                    case "return":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_RETURN, nodeList, offset);
                }
            } else if (Constants.TOKEN_SYMBOL.equals(token.getType())) {
                if (";".equals(token.getText())) { // 未知语句
                    return createUnknownSentence(nodeList, start, offset);
                }
            }
        } else {
            switch (node.getType()) {
                case Constants.NODE_CURVES: {
                    // 圆括号
                    int syncIndex = NodeUtils.beforeToken(Constants.TOKEN_KEYWORD, "synchronized", nodeList, start, offset);
                    if (syncIndex >= 0) {
                        // synchronized语句
                        SentenceNode sentenceNode = new SentenceNode(Constants.NODE_SENTENCE, Constants.SENTENCE_TYPE_SYNCHRONIZED);
                        return new JavaNodeRegion(sentenceNode, nodeList, syncIndex, offset + 1);
                    }
                    int namedIndex = NodeUtils.beforeToken(Constants.TOKEN_NAMED, null, nodeList, start, offset);
                    if (namedIndex >= 0) {
                        // 方法：可能是方法定义或者是方法调用

                        // 查找是否存在方法类型
                        int typeIndex = NodeUtils.beforeToken(nodeList, start, namedIndex, token -> {
                            if (Constants.TOKEN_NAMED.equals(token.getText())) {
                                return true;
                            }
                            if (Constants.TOKEN_KEYWORD.equals(token.getText())) {
                                KeywordToken keywordToken = (KeywordToken) token;
                                // 基本类型关键字
                                return keywordToken.isBaseType();
                            }
                            return false;
                        });
                        if (typeIndex >= 0) {
                            // 查找到了类型，此处为方法定义
                            int regionStart = NodeUtils.trimStart(nodeList, start, typeIndex);
                            MethodNode methodNode = new MethodNode(Constants.NODE_METHOD);
                            return new JavaNodeRegion(methodNode, nodeList, regionStart, offset + 1);
                        } else {
                            // 方法调用语句
                            int regionStart = NodeUtils.trimStart(nodeList, start, namedIndex);
                            SentenceNode sentenceNode = new SentenceNode(Constants.NODE_SENTENCE, Constants.SENTENCE_TYPE_UNKNOWN);
                            return new JavaNodeRegion(sentenceNode, nodeList, regionStart, offset + 1);
                        }
                    }
                    break;
                }
                case Constants.NODE_BRACE: {
                    // 花括号
                    int staticIndex = NodeUtils.beforeToken(Constants.TOKEN_KEYWORD, "static", nodeList, start, offset);
                    if (staticIndex >= 0) {
                        // 静态方法块
                        MethodNode methodNode = new MethodNode(Constants.NODE_METHOD);
                        JavaNodeRegion region = new JavaNodeRegion(methodNode, nodeList, staticIndex, offset + 1);
                        region.setClosed(true);
                        return region;
                    }
                    break;
                }
            }
        }
        return null;
    }

    @Override
    protected int onMatchRegionEnd(JavaNodeRegion region, int unclosedIndex, int offset, int maxEnd) {
        Node node = region.getSrc().getNode(offset);
        String type = region.getNode().getType();
        if (Constants.NODE_SENTENCE.equals(type)) {
            SentenceNode sentenceNode = (SentenceNode) region.getNode();
            if (node.isToken()) {
                if (Constants.TOKEN_SYMBOL.equals(node.getType())) {
                    String symbol = ((Token) node).getText();
                    if (";".equals(symbol)) {
                        switch (sentenceNode.getSentenceType()) {
                            case Constants.SENTENCE_TYPE_GOTO:
                            case Constants.SENTENCE_TYPE_ASSERT:
                            case Constants.SENTENCE_TYPE_THROW:
                            case Constants.SENTENCE_TYPE_BREAK:
                            case Constants.SENTENCE_TYPE_CONTINUE:
                            case Constants.SENTENCE_TYPE_RETURN:
                                return offset + 1;
                        }
                    }
                }
            } else {
                if (Constants.NODE_BRACE.equals(node.getType())) {
                    // 花括号
                    switch (sentenceNode.getSentenceType()) {
                        case Constants.SENTENCE_TYPE_IF:
                        case Constants.SENTENCE_TYPE_ELSE:
                        case Constants.SENTENCE_TYPE_FOR:
                        case Constants.SENTENCE_TYPE_SWITCH:
                        case Constants.SENTENCE_TYPE_WHILE:
                        case Constants.SENTENCE_TYPE_TRY:
                        case Constants.SENTENCE_TYPE_CATCH:
                        case Constants.SENTENCE_TYPE_FINALLY:
                            return offset + 1;
                    }
                }
            }
        }
        return REGION_END_NOT_MATCH_AND_CONSUMED;
    }

    @Override
    protected void onRegionClosed(JavaNodeRegion region, int unclosedIndex) {
        super.onRegionClosed(region, unclosedIndex);
    }
}
