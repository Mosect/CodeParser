package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

import java.util.Collection;
import java.util.TreeSet;

/**
 * 符号解析器
 */
public class SymbolParser extends CommonTextParser {

    private final TreeSet<String> symbolSet = new TreeSet<>();
    private String symbol;

    public SymbolParser() {
        // >>> 优先级最高
        registerSymbol(">>>");

        // 其次到两个字符的操作符
        registerSymbol("<<");
        registerSymbol(">>");

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

        registerSymbol(".");
        registerSymbol("-");
        registerSymbol("+");
        registerSymbol("/");

        registerSymbol("&");
        registerSymbol("|");
        registerSymbol("~");
        registerSymbol("^");
    }

    @Override
    protected void onClear() {
        super.onClear();
        setSymbol(null);
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        for (String symbol : allSymbols()) {
            if (CharUtils.match(text, start, symbol, false)) {
                setSymbol(symbol);
                finishParse(true, start + symbol.length());
                return;
            }
        }

        finishParse(false, start);
    }

    /**
     * 注册符号
     *
     * @param symbol 符号
     */
    public void registerSymbol(String symbol) {
        symbolSet.add(symbol);
    }

    /**
     * 注销符号
     *
     * @param symbol 符号
     */
    public void unregisterSymbol(String symbol) {
        symbolSet.remove(symbol);
    }

    /**
     * 获取所有符号
     *
     * @return 所有符合
     */
    public Collection<String> allSymbols() {
        return symbolSet;
    }

    /**
     * 获取解析到的符号
     *
     * @return 解析到的符号
     */
    public String getSymbol() {
        return symbol;
    }

    protected void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getName() {
        return "java.symbol";
    }
}
