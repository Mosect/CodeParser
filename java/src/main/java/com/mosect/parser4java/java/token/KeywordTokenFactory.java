package com.mosect.parser4java.java.token;

import com.mosect.parser4java.java.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 关键字token工厂
 */
public class KeywordTokenFactory {

    private final Map<String, KeywordToken> tokenMap = new HashMap<>();

    public KeywordTokenFactory() {
        register("abstract", new KeywordToken.Data()
                .setModifyClass(true)
                .setModifyMethod(true)
        );
        register("assert", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("boolean", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("break", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("byte", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("case", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("catch", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("char", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("class", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("const", new KeywordToken.Data()
        );
        register("continue", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("default", new KeywordToken.Data()
                .setInstructed(true)
                .setModifyMethod(true)
        );
        register("do", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("double", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("else", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("enum", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("extends", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("false", new KeywordToken.Data()
                .setValue(true)
        );
        register("final", new KeywordToken.Data()
                .setModifyMethod(true)
                .setModifyClass(true)
                .setModifyField(true)
        );
        register("finally", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("float", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("for", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("goto", new KeywordToken.Data()
        );
        register("if", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("implements", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("import", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("instanceof", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("int", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("interface", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("long", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("native", new KeywordToken.Data()
                .setModifyMethod(true)
        );
        register("new", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("null", new KeywordToken.Data()
                .setValue(true)
        );
        register("package", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("private", new KeywordToken.Data()
                .setModifyMethod(true)
                .setModifyClass(true)
                .setModifyField(true)
        );
        register("protected", new KeywordToken.Data()
                .setModifyMethod(true)
                .setModifyClass(true)
                .setModifyField(true)
        );
        register("public", new KeywordToken.Data()
                .setModifyMethod(true)
                .setModifyClass(true)
                .setModifyField(true)
        );
        register("return", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("short", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("static", new KeywordToken.Data()
                .setModifyMethod(true)
                .setModifyClass(true)
                .setModifyField(true)
        );
        register("strictfp", new KeywordToken.Data()
                .setModifyMethod(true)
        );
        register("super", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("switch", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("synchronized", new KeywordToken.Data()
                .setModifyClass(true)
                .setModifyMethod(true)
        );
        register("this", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("throw", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("throws", new KeywordToken.Data()
                .setDefinition(true)
        );
        register("transient", new KeywordToken.Data()
                .setModifyField(true)
        );
        register("true", new KeywordToken.Data()
                .setValue(true)
        );
        register("try", new KeywordToken.Data()
                .setInstructed(true)
        );
        register("void", new KeywordToken.Data()
                .setPrimitive(true)
        );
        register("volatile", new KeywordToken.Data()
                .setModifyField(true)
        );
        register("while", new KeywordToken.Data()
                .setInstructed(true)
        );
    }

    public void register(String text, KeywordToken.Data data) {
        tokenMap.put(text, new KeywordToken(Constants.TOKEN_KEYWORD, text, data));
    }

    public void unregister(String text) {
        tokenMap.remove(text);
    }

    public KeywordToken createTokenByText(String text) {
        KeywordToken token = tokenMap.get(text);
        if (null != token) {
            return new KeywordToken(token);
        }
        return null;
    }
}
