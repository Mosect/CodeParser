package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用节点解析器
 */
public abstract class CommonNodeParser implements NodeParser {

    private List<Token> tokenList;
    private int tokenStart;
    private int tokenEnd;
    private boolean pass;
    private Node node;
    private final List<ParseError> errors = new ArrayList<>();

    @Override
    public void parse(List<Token> tokenList, int offset) {
        onClear();
        this.tokenList = tokenList;
        tokenStart = offset;
        tokenEnd = offset;
        onParse(tokenList, offset);
    }

    @Override
    public ParseError getError(int index) {
        return errors.get(index);
    }

    @Override
    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public boolean isPass() {
        return pass;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenEnd;
    }

    @Override
    public List<Token> getTokenList() {
        return tokenList;
    }

    @Override
    public Node getNode() {
        return node;
    }

    protected void onClear() {
        errors.clear();
        node = null;
    }

    protected void setNode(Node node) {
        this.node = node;
    }

    protected void finishParse(boolean pass, int tokenEnd) {
        this.pass = pass;
        this.tokenEnd = tokenEnd;
    }

    /**
     * 添加错误
     *
     * @param errorId       错误id
     * @param errorMsg      错误信息
     * @param errorPosition 错误位置
     */
    protected void putError(String errorId, String errorMsg, int errorPosition) {
        errors.add(new ParseError(errorId, errorMsg, errorPosition));
    }

    /**
     * 添加错误
     *
     * @param error 错误对象
     */
    protected void putError(ParseError error) {
        errors.add(error);
    }

    /**
     * 解析逻辑<br>
     * 期间需要调用{@link #finishParse(boolean, int) finishParse}结束解析<br>
     * 调用{@link #putError(ParseError)} 或者{@link #putError(String, String, int)}设置错误
     *
     * @param tokenList token列表
     * @param offset    偏移量
     */
    protected abstract void onParse(List<Token> tokenList, int offset);
}
