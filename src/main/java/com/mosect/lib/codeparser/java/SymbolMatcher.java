package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.ListTextMatcher;

public class SymbolMatcher extends ListTextMatcher {

    public SymbolMatcher() {
        // >>> 优先级最高
        register(">>>");
        register("...");

        // 其次到两个字符的操作符
        register("<<");
        register(">>");

        register("+=");
        register("-=");
        register("*=");
        register("/=");
        register("|=");
        register("^=");

        register("&&");
        register("||");
        register("!=");
        register("==");

        // 一个字符的操作符，优先级最低
        register("!");

        register("(");
        register(")");
        register("=");
        register("{");
        register("}");
        register("[");
        register("]");
        register("<");
        register(">");

        register("@");
        register(",");
        register("?");
        register(":");
        register("%");
        register("*");
        register(";");

        register(".");
        register("-");
        register("+");
        register("/");

        register("&");
        register("|");
        register("~");
        register("^");

        sort();
    }
}
