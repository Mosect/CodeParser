package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.Parser;
import com.mosect.parser4java.core.TextParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用解析器
 */
public abstract class CommonParser implements Parser {

    private boolean pass;
    private final List<ParseError> errors = new ArrayList<>();
    private CharSequence text;
    private int textStart;
    private int textEnd;
    private CharSequence parseText;

    @Override
    public boolean isPass() {
        return pass;
    }

    @Override
    public int getTextStart() {
        return textStart;
    }

    @Override
    public int getTextEnd() {
        return textEnd;
    }

    @Override
    public CharSequence getText() {
        return text;
    }

    @Override
    public CharSequence getParseText() {
        if (isPass()) {
            if (null == parseText) {
                parseText = text.subSequence(textStart, textEnd);
            }
            return parseText;
        }
        return null;
    }

    @Override
    public ParseError getError(int index) {
        return errors.get(index);
    }

    @Override
    public int getErrorCount() {
        return errors.size();
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
     * 获取错误列表
     *
     * @return 错误列表
     */
    protected List<ParseError> getErrors() {
        return errors;
    }

    /**
     * 添加其他解析器错误
     *
     * @param otherParser 其他解析器
     */
    protected void putErrorWithOther(TextParser otherParser) {
        if (otherParser.hasError()) {
            for (int i = 0; i < otherParser.getErrorCount(); i++) {
                ParseError error = otherParser.getError(i);
                putError(error);
            }
        }
    }

    /**
     * 结束解析
     *
     * @param pass    是否通过解析
     * @param textEnd 文本结束位置
     */
    protected void finishParse(boolean pass, int textEnd) {
        this.pass = pass;
        this.textEnd = textEnd;
    }

    /**
     * 使用别的解析器作为结果
     *
     * @param pass        是否通过
     * @param otherParser 其他解析器
     */
    protected void finishWithOther(boolean pass, TextParser otherParser) {
        finishParse(pass, otherParser.getTextEnd());
        putErrorWithOther(otherParser);
    }

    /**
     * 使用别的解析器作为结果
     *
     * @param otherParser 其他解析器
     */
    protected void finishWithOther(TextParser otherParser) {
        finishWithOther(otherParser.isPass(), otherParser);
    }

    /**
     * 清理缓存
     */
    protected void onClear() {
        errors.clear();
        parseText = null;
    }

    protected void setText(CharSequence text) {
        this.text = text;
    }

    protected void setTextEnd(int textEnd) {
        this.textEnd = textEnd;
    }

    protected void setTextStart(int textStart) {
        this.textStart = textStart;
    }
}
