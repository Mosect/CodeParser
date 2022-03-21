package com.mosect.parser4java.java;

import java.util.HashMap;
import java.util.Map;

/**
 * 关键字工厂
 */
public class KeywordFactory {

    protected Map<String, KeywordToken> keywordMap = new HashMap<>();

    public KeywordFactory() {
        putToken("abstract");
        putToken("assert");
        putToken("boolean");
        putToken("break");
        putToken("byte");
        putToken("case");
        putToken("catch");
        putToken("char");
        putToken("class");
        putToken("const");
        putToken("continue");
        putToken("default");
        putToken("do");
        putToken("double");
        putToken("else");
        putToken("enum");
        putToken("extends");
        putToken("final");
        putToken("finally");
        putToken("float");
        putToken("for");
        putToken("goto");
        putToken("if");
        putToken("implements");
        putToken("import");
        putToken("instanceof");
        putToken("int");
        putToken("interface");
        putToken("long");
        putToken("native");
        putToken("new");
        putToken("null");
        putToken("package");
        putToken("private");
        putToken("protected");
        putToken("public");
        putToken("return");
        putToken("short");
        putToken("static");
        putToken("strictfp");
        putToken("super");
        putToken("switch");
        putToken("synchronized");
        putToken("this");
        putToken("throw");
        putToken("throws");
        putToken("transient");
        putToken("try");
        putToken("void");
        putToken("volatile");
        putToken("while");
    }

    public KeywordToken getToken(String text) {
        return keywordMap.get(text);
    }

    protected void putToken(String text) {
        KeywordToken token = new KeywordToken(text);
        keywordMap.put(token.getId(), token);
    }
}
