package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonTextParser;

/**
 * 名称解析器，解析各类名称和关键字
 */
public class NamedParser extends CommonTextParser {

    private String nameText;
    private KeywordToken keywordToken;
    private KeywordTokenFactory keywordTokenFactory;

    public NamedParser() {
        setKeywordTokenFactory(new KeywordTokenFactory());
    }

    @Override
    protected void onClear() {
        super.onClear();
        setNameText(null);
        setKeywordToken(null);
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        if (text.length() > start) {
            finishParse(true, text.length());
            String nameText = getParseText().toString();
            setNameText(nameText);
            KeywordToken keywordToken = getKeywordTokenFactory().findTokenByText(nameText);
            setKeywordToken(keywordToken);
        } else {
            finishParse(false, start);
        }
    }

    public String getNameText() {
        return nameText;
    }

    protected void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public KeywordToken getKeywordToken() {
        return keywordToken;
    }

    protected void setKeywordToken(KeywordToken keywordToken) {
        this.keywordToken = keywordToken;
    }

    @Override
    public String getName() {
        return "java.name";
    }

    protected void setKeywordTokenFactory(KeywordTokenFactory keywordTokenFactory) {
        this.keywordTokenFactory = keywordTokenFactory;
    }

    public KeywordTokenFactory getKeywordTokenFactory() {
        return keywordTokenFactory;
    }
}
