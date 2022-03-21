package com.mosect.parser4java.core.javalike;

public class SymbolNodeFactory extends com.mosect.parser4java.core.common.SymbolNodeFactory {

    public SymbolNodeFactory() {
        // >>> 优先级最高
        registerSymbol(">>>");

        // 其次到两个字符的操作符
        registerSymbol("<<");
        registerSymbol(">>");

        registerSymbol("-");
        registerSymbol("+");
        registerSymbol("/");

        registerSymbol("+=");
        registerSymbol("-=");
        registerSymbol("*=");
        registerSymbol("/=");
        registerSymbol("|=");
        registerSymbol("^=");

        registerSymbol("&&");
        registerSymbol("||");
        registerSymbol("!=");
        registerSymbol("==");

        // 一个字符的操作符，优先级最低
        registerSymbol("!");

        registerSymbol("(");
        registerSymbol(")");
        registerSymbol("=");
        registerSymbol("{");
        registerSymbol("}");
        registerSymbol("[");
        registerSymbol("]");
        registerSymbol("<");
        registerSymbol(">");

        registerSymbol("@");
        registerSymbol(",");
        registerSymbol("?");
        registerSymbol(":");
        registerSymbol("%");
        registerSymbol("*");
        registerSymbol(";");
//        registerSymbol(".");

        registerSymbol("&");
        registerSymbol("|");
        registerSymbol("~");
        registerSymbol("^");

    }
}
