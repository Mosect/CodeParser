package com.mosect.parser4java.java;

public class Constants {

    // token名称

    public final static String TOKEN_COMMENT = "java.token.comment";
    public final static String TOKEN_WHITESPACE = "java.token.whitespace";
    public final static String TOKEN_CHAR = "java.token.char";
    public final static String TOKEN_KEYWORD = "java.token.keyword";
    public final static String TOKEN_NAMED = "java.token.named";
    public final static String TOKEN_NUMBER = "java.token.number";
    public final static String TOKEN_STRING = "java.token.string";
    public final static String TOKEN_SYMBOL = "java.token.symbol";
    public final static String TOKEN_TEXT_BLOCK = "java.token.textBlock";

    // 节点类型

    public final static String NODE_SENTENCE = "java.node.sentence";
    public final static String NODE_TYPE = "java.node.type";
    public final static String NODE_METHOD = "java.node.method";

    public final static String NODE_BRACE = "java.node.brace";
    public final static String NODE_CURVES = "java.node.curves";
    public final static String NODE_SQUARE = "java.node.square";

    // 语句类型
    /**
     * 未知语句语句
     */
    public final static String SENTENCE_TYPE_UNKNOWN = "unknown";
    /**
     * import语句
     */
    public final static String SENTENCE_TYPE_IMPORT = "import";
    /**
     * package语句
     */
    public final static String SENTENCE_TYPE_PACKAGE = "package";
    /**
     * goto语句
     */
    public final static String SENTENCE_TYPE_GOTO = "goto";
    /**
     * if语句
     */
    public final static String SENTENCE_TYPE_IF = "if";
    /**
     * else语句
     */
    public final static String SENTENCE_TYPE_ELSE = "else";
    /**
     * for语句
     */
    public final static String SENTENCE_TYPE_FOR = "for";
    /**
     * switch语句
     */
    public final static String SENTENCE_TYPE_SWITCH = "switch";
    /**
     * do语句
     */
    public final static String SENTENCE_TYPE_DO = "do";
    /**
     * do语句
     */
    public final static String SENTENCE_TYPE_WHILE = "while";
    /**
     * throw语句
     */
    public final static String SENTENCE_TYPE_THROW = "throw";
    /**
     * synchronized语句
     */
    public final static String SENTENCE_TYPE_SYNCHRONIZED = "synchronized";
    /**
     * try语句
     */
    public final static String SENTENCE_TYPE_TRY = "try";
    /**
     * catch语句
     */
    public final static String SENTENCE_TYPE_CATCH = "catch";
    /**
     * finally语句
     */
    public final static String SENTENCE_TYPE_FINALLY = "synchronized";
    /**
     * assert语句
     */
    public final static String SENTENCE_TYPE_ASSERT = "assert";
    /**
     * break语句
     */
    public final static String SENTENCE_TYPE_BREAK = "break";
    /**
     * continue语句
     */
    public final static String SENTENCE_TYPE_CONTINUE = "continue";
    /**
     * case语句
     */
    public final static String SENTENCE_TYPE_CASE = "case";
    /**
     * return语句
     */
    public final static String SENTENCE_TYPE_RETURN = "return";

    // class类型
    /**
     * 类
     */
    public final static String CLASS_TYPE_CLASS = "class";
    /**
     * 接口
     */
    public final static String CLASS_TYPE_INTERFACE = "interface";
    /**
     * 注解
     */
    public final static String CLASS_TYPE_ANNOTATION = "annotation";
    /**
     * 枚举
     */
    public final static String CLASS_TYPE_ENUM = "enum";

    // 文本解析器

    public final static String PARSER_COMMENT = "java.parser.comment";
    public final static String PARSER_CHAR = "java.parser.char";
    public final static String PARSER_NAMED = "java.parser.named";
    public final static String PARSER_NUMBER = "java.parser.number";
    public final static String PARSER_STRING = "java.parser.string";
    public final static String PARSER_SYMBOL = "java.parser.symbol";
    public final static String PARSER_TEXT_BLOCK = "java.parser.textBlock";
    public final static String PARSER_WHITESPACE = "java.parser.whitespace";
}
