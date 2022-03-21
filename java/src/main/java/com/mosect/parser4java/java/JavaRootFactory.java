package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.common.CommonNode;
import com.mosect.parser4java.core.common.LinefeedNodeFactory;
import com.mosect.parser4java.core.common.WhitespaceNodeFactory;
import com.mosect.parser4java.core.javalike.CharNodeFactory;
import com.mosect.parser4java.core.javalike.CommentNodeFactory;
import com.mosect.parser4java.core.javalike.NumberNodeFactory;
import com.mosect.parser4java.core.javalike.NumberToken;
import com.mosect.parser4java.core.javalike.StringNodeFactory;
import com.mosect.parser4java.core.source.SourceWrapper;

import java.util.ArrayList;
import java.util.List;

public class JavaRootFactory implements NodeFactory {

    protected CommentNodeFactory commentNodeFactory = new CommentNodeFactory();
    protected StringNodeFactory stringNodeFactory = new StringNodeFactory();
    protected CharNodeFactory charNodeFactory = new CharNodeFactory();

    protected WhitespaceNodeFactory whitespaceNodeFactory = new WhitespaceNodeFactory();
    protected LinefeedNodeFactory linefeedNodeFactory = new LinefeedNodeFactory();
    protected SymbolNodeFactory symbolNodeFactory = new SymbolNodeFactory();

    protected NumberNodeFactory numberNodeFactory = new NumberNodeFactory();

    protected SymbolNodeFactory2 symbolNodeFactory2 = new SymbolNodeFactory2();

    protected KeywordFactory keywordFactory = new KeywordFactory();

    public CommentNodeFactory getCommentNodeFactory() {
        return commentNodeFactory;
    }

    public StringNodeFactory getStringNodeFactory() {
        return stringNodeFactory;
    }

    public CharNodeFactory getCharNodeFactory() {
        return charNodeFactory;
    }

    public WhitespaceNodeFactory getWhitespaceNodeFactory() {
        return whitespaceNodeFactory;
    }

    public LinefeedNodeFactory getLinefeedNodeFactory() {
        return linefeedNodeFactory;
    }

    public SymbolNodeFactory getSymbolNodeFactory() {
        return symbolNodeFactory;
    }

    public NumberNodeFactory getNumberNodeFactory() {
        return numberNodeFactory;
    }

    public SymbolNodeFactory2 getPointSymbolNodeParser() {
        return symbolNodeFactory2;
    }

    public KeywordFactory getKeywordFactory() {
        return keywordFactory;
    }

    @Override
    public String getName() {
        return "root";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        int pos = source.getPosition();
        SourceWrapper sourceWrapper = new SourceWrapper(source);
        CommonNode result = new CommonNode();

        List<List<Node>> nodesList = new ArrayList<>(5);
        try {
            List<Node> nodes = null;

            List<Node> nodes1 = new ArrayList<>(512);
            nodesList.add(nodes1);

            NodeFactory[] factory2List = new NodeFactory[]{
                    symbolNodeFactory2
            };

            // 第一步：解析注释、字符串、字符、操作符、数值、点操作符切割，并进行关键字整理

            // 关键字处理
            UnknownTokenHandler keywordTokenHandler = (start, end) -> {
                String text = source.getText().subSequence(start, end).toString();
                KeywordToken keywordToken = keywordFactory.getToken(text);
                if (null == keywordToken) {
                    // 非关键字
                    NameToken nameToken = new NameToken(text);
                    nodes1.add(nameToken);
                } else {
                    // 关键字
                    nodes1.add(keywordToken);
                }
            };
            // 数值处理
            UnknownTokenHandler numberTokenHandler = (start, end) -> {
                sourceWrapper.clip(end);
                sourceWrapper.setPosition(start);
                NumberToken numberToken = numberNodeFactory.parse(parentParser, sourceWrapper, outErrors);
                if (null == numberToken) {
                    // 非数值，使用SymbolNodeFactory2进行切割
                    parseWithFactories(parentParser, sourceWrapper, factory2List, outErrors, nodes1, keywordTokenHandler);
                } else {
                    // 数值
                    int len = numberToken.getText().length();
                    if (len != end - start) {
                        System.err.printf("%s >>> %s\n", source.getText().subSequence(start, end), numberToken.getText());
                        throw new ParseException("INVALID_NUMBER", sourceWrapper.getPosition());
                    }
                    nodes1.add(numberToken);
                }
            };
            parseWithFactories(parentParser, source, new NodeFactory[]{
                    commentNodeFactory,
                    stringNodeFactory,
                    charNodeFactory,
                    linefeedNodeFactory,
                    whitespaceNodeFactory,
                    symbolNodeFactory
            }, outErrors, nodes1, numberTokenHandler);
            nodes = nodes1;

            if (!nodes1.isEmpty()) {
                // 第二步：解析数值，点 切割

                // 第三步：识别关键字
                /*List<Node> nodes2 = parseUnknownTokens(
                        parentParser,
                        nodes1,
                        sourceWrapper,
                        new NodeFactory[]{
                                linefeedNodeFactory,
                                whitespaceNodeFactory
                        },
                        outErrors
                );
                nodes = nodes2;

                // 第三步：解析数值常量
                List<Node> nodes3 = parseUnknownTokens(
                        parentParser,
                        nodes2,
                        sourceWrapper,
                        new NodeFactory[]{
                                numberNodeFactory
                        },
                        outErrors
                );
                nodes = nodes3;*/
            }

            if (null != nodes)
                result.getChildren().addAll(nodes);
            return result;
        } catch (Exception e) {
            source.setPosition(pos);
            throw e;
        }
    }

    protected void parseWithFactories(
            ParentParser parentParser,
            TextSource source,
            NodeFactory[] factories,
            List<ParseError> outErrors,
            List<Node> outNodes,
            UnknownTokenHandler unknownTokenHandler) throws ParseException {
        int unknownStart = source.getPosition();
        while (source.hasMore()) {
            boolean ok = false;
            for (NodeFactory factory : factories) {
                int unknownEnd = source.getPosition();
                Node node = factory.parse(parentParser, source, outErrors);
                if (null != node) {
                    if (unknownEnd > unknownStart) {
                        unknownTokenHandler.processUnknownToken(unknownStart, unknownEnd);
                    }
                    outNodes.add(node);
                    unknownStart = source.getPosition();
                    ok = true;
                    break;
                }
            }
            if (ok) continue;

            source.offsetOne();
        }
        if (source.length() > unknownStart) {
            unknownTokenHandler.processUnknownToken(unknownStart, source.length());
        }
    }

    public interface UnknownTokenHandler {

        void processUnknownToken(int start, int end) throws ParseException;
    }
}
