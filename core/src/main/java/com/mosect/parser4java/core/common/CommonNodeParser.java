package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeParser;
import com.mosect.parser4java.core.Token;

import java.io.IOException;
import java.util.List;

/**
 * 通用节点解析器
 */
public abstract class CommonNodeParser<C> extends CommonParser implements NodeParser<C> {

    private C context;

    public CommonNodeParser(C context) {
        setContext(context);
    }

    public CommonNodeParser() {
    }

    @Override
    public void parse(CharSequence text, List<Token> tokens, int tokenStart) {
        onClear();
        onParse(text, tokens);
    }

    @Override
    public void checkNode(CharSequence text, Node node) {
        onClear();
        onCheck(text, node);
        // 结束解析，检查节点默认全部通过解析，使用isSuccess()或者hasError判断是否有错误
        finishParse(true, -1);
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public Node makeNode() {
        return new CommonNode(getName());
    }

    @Override
    public CharSequence getParseText() {
        if (isPass()) {
            try {
                StringBuilder stringBuilder = new StringBuilder(128);
                makeNode().append(stringBuilder);
                return stringBuilder;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    protected void setContext(C context) {
        this.context = context;
    }

    /**
     * 解析逻辑<br>
     * 解析过程中，必须调用{@link #finishParse(boolean, int) finishParse}来结束解析<br>
     * 如果出现错误，需要调用{@link #putError(String, String, int) setError}来设置错误信息<br>
     *
     * @param text   原始文本
     * @param tokens token列表
     */
    protected abstract void onParse(CharSequence text, List<Token> tokens);

    /**
     * 检查节点逻辑<br>
     * 如果出现错误，需要调用{@link #putError(String, String, int) putError}来设置错误信息<br>
     *
     * @param text 原始文本
     * @param node 节点
     */
    protected abstract void onCheck(CharSequence text, Node node);
}
