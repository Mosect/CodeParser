package com.mosect.parser4java.core.javalike;

public class SymbolNodeFactory extends com.mosect.parser4java.core.common.SymbolNodeFactory {

    public SymbolNodeFactory() {
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

        registerSymbol("&");
        registerSymbol("|");
        registerSymbol("~");
        registerSymbol("^");
        registerSymbol("<<");
        registerSymbol(">>");
        registerSymbol(">>>");

        registerSymbol("-");
        registerSymbol("+");
        registerSymbol("/");

        registerSymbol("+=");
        registerSymbol("-=");
        registerSymbol("*=");
        registerSymbol("/=");
        registerSymbol("|=");
        registerSymbol("^=");

        registerSymbol("!");
        registerSymbol("&&");
        registerSymbol("||");
        registerSymbol("!=");
        registerSymbol("==");
    }
}
