package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.Token;

/**
 * 通用文本解析器
 */
public abstract class CommonTextParser extends CommonParser implements TextParser {

    @Override
    public void parse(CharSequence text, int start) {
        onClear();
        setText(text);
        setTextStart(start);
        setTextEnd(start);
        onParse(text, start);
    }

    @Override
    public Token makeToken() {
        String str = getParseText().toString();
        return new CommonToken(getName(), str);
    }

    /**
     * 解析逻辑<br>
     * 解析过程中，必须调用{@link #finishParse(boolean, int) finishParse}来结束解析<br>
     * 如果出现错误，需要调用{@link #putError(String, String, int) putError}来设置错误信息<br>
     *
     * @param text  解析文本
     * @param start 文本开始
     */
    protected abstract void onParse(CharSequence text, int start);
}
