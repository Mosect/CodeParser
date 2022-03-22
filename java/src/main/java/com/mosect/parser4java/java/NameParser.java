package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonTextParser;

/**
 * 名称解析器，解析各类名称和关键字
 */
public class NameParser extends CommonTextParser {

    private String nameText;
    private boolean keyword;

    @Override
    protected void onClear() {
        super.onClear();
        setNameText(null);
        setKeyword(false);
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        if (text.length() > start) {
            finishParse(true, text.length());
            String nameText = getParseText().toString();
            setNameText(nameText);
            setKeyword(isKeywordText(nameText));
        } else {
            finishParse(false, start);
        }
    }

    protected boolean isKeywordText(String text) {
        switch (text) {
            case "abstract":
            case "assert":
            case "boolean":
            case "break":
            case "byte":
            case "case":
            case "catch":
            case "char":
            case "class":
            case "const":
            case "continue":
            case "default":
            case "do":
            case "double":
            case "else":
            case "enum":
            case "extends":
            case "final":
            case "finally":
            case "float":
            case "for":
            case "goto":
            case "if":
            case "implements":
            case "import":
            case "instanceof":
            case "int":
            case "interface":
            case "long":
            case "native":
            case "new":
            case "null":
            case "package":
            case "private":
            case "protected":
            case "public":
            case "return":
            case "short":
            case "static":
            case "strictfp":
            case "super":
            case "switch":
            case "synchronized":
            case "this":
            case "throw":
            case "throws":
            case "transient":
            case "try":
            case "void":
            case "volatile":
            case "while":
                return true;
            default:
                return false;
        }
    }

    public String getNameText() {
        return nameText;
    }

    protected void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public boolean isKeyword() {
        return keyword;
    }

    protected void setKeyword(boolean keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getName() {
        return "java.name";
    }
}
