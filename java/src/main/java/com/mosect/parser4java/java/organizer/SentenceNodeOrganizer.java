package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.node.SentenceNode;
import com.mosect.parser4java.java.node.TypeNode;
import com.mosect.parser4java.java.util.NodeUtils;

/**
 * 语句节点组织器
 */
public class SentenceNodeOrganizer extends JavaNodeOrganizer {

    @Override
    protected JavaNodeRegion onMatchRegion(NodeList nodeList, Node node, int start, int offset) {
        if (node.isToken()) {
            Token token = (Token) node;
            if (Constants.TOKEN_KEYWORD.equals(token.getType())) {
                switch (token.getText()) {
                    case "class":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_CLASS, nodeList, start, offset);
                    case "interface":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_INTERFACE, nodeList, start, offset);
                    case "@interface":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_ANNOTATION, nodeList, start, offset);
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
                    case "synchronized":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_SYNCHRONIZED, nodeList, offset);
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
                switch (token.getText()) {
                    case ";": // 未知语句
                    case ":":
                }
            }
        } else {
            switch (node.getType()) {
                case Constants.NODE_CURVES: // 圆括号
            }
        }
        return null;
    }

    @Override
    protected int onMatchRegionEnd(JavaNodeRegion region, int unclosedIndex, int offset, int maxEnd) {
        return super.onMatchRegionEnd(region, unclosedIndex, offset, maxEnd);
    }
}
