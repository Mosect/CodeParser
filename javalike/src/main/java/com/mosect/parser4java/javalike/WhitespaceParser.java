package com.mosect.parser4java.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

/**
 * 空白字符解析器
 */
public class WhitespaceParser extends CommonTextParser {

    private String charName;

    @Override
    protected void onClear() {
        super.onClear();
        setCharName(null);
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        if (text.length() > start) {
            char ch = text.charAt(start);
            String name = getWhitespaceName(ch);
            if (null != name) {
                setCharName(name);
                finishParse(true, start + 1);
            } else if (CharUtils.match(text, start, "\r\n", false)) {
                setCharName("crlf");
                finishParse(true, start + 2);
            } else if (CharUtils.match(text, start, "\r", false)) {
                setCharName("cr");
                finishParse(true, start + 1);
            } else if (CharUtils.match(text, start, "\n", false)) {
                setCharName("lf");
                finishParse(true, start + 1);
            } else {
                finishParse(false, start);
            }
        } else {
            finishParse(false, start);
        }
    }

    /**
     * 获取字符名称<br>
     * {@link #isSuccess()}为true，此值有效
     *
     * @return 空白字符名称
     */
    public String getCharName() {
        return charName;
    }

    /**
     * 设置空白字符名称
     *
     * @param charName 空白字符名称
     */
    protected void setCharName(String charName) {
        this.charName = charName;
    }

    protected String getWhitespaceName(char ch) {
        switch (ch) {
            case ' ':
                return "space";
            case '\t':
                return "tab";
            default:
                return null;
        }
    }

    @Override
    public String getName() {
        return "javalike.whitespace";
    }
}
