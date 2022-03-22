package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.TextParseError;
import com.mosect.parser4java.core.TextParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用文本解析器
 */
public abstract class CommonTextParser implements TextParser {

    private boolean pass;
    private final List<TextParseError> errors = new ArrayList<>();
    private CharSequence text;
    private CharSequence parseText;
    private int textStart;
    private int textEnd;

    @Override
    public void parse(CharSequence text, int start) {
        onClear();
        this.text = text;
        this.textStart = start;
        onParse(text, start);
    }

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
    public TextParseError getError(int index) {
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
        errors.add(new CommonTextParseError(errorId, errorMsg, errorPosition));
    }

    /**
     * 添加错误
     *
     * @param error 错误对象
     */
    protected void putError(TextParseError error) {
        errors.add(error);
    }

    /**
     * 添加其他解析器错误
     *
     * @param otherParser 其他解析器
     */
    protected void putErrorWithOther(TextParser otherParser) {
        if (otherParser.hasError()) {
            for (int i = 0; i < otherParser.getErrorCount(); i++) {
                TextParseError error = otherParser.getError(i);
                putError(error);
            }
        }
    }

    /**
     * 清理缓存
     */
    protected void onClear() {
        errors.clear();
        parseText = null;
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
     * 解析逻辑<br>
     * 解析过程中，必须调用{@link #finishParse(boolean, int) finishParse}来结束解析<br>
     * 如果出现错误，需要调用{@link #putError(String, String, int) setError}来设置错误信息<br>
     *
     * @param text  解析文本
     * @param start 文本开始
     */
    protected abstract void onParse(CharSequence text, int start);
}
