package com.mosect.parser4java.core;

import java.util.List;

/**
 * 节点解析器
 */
public interface NodeParser {

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    String getName();

    /**
     * 解析节点
     *
     * @param tokenList token列表
     * @param offset    偏移量
     */
    void parse(List<Token> tokenList, int offset);

    /**
     * 获取错误
     *
     * @param index 下标
     * @return 错误
     */
    ParseError getError(int index);

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    int getErrorCount();

    /**
     * 判断是否有错误
     *
     * @return true，有错误；false，没有错误
     */
    default boolean hasError() {
        return getErrorCount() > 0;
    }

    /**
     * 判断是否通过解析
     *
     * @return true，通过解析；false，未通过解析
     */
    boolean isPass();

    /**
     * 判断是否解析成功
     *
     * @return true，解析成功；false，解析失败
     */
    default boolean isSuccess() {
        return isPass() && !hasError();
    }

    /**
     * 获取开始的token下标
     *
     * @return 开始的token下标
     */
    int getTokenStart();

    /**
     * 获取结束的token
     *
     * @return 结束的token
     */
    int getTokenEnd();

    /**
     * 获取token列表
     *
     * @return token列表
     */
    List<Token> getTokenList();

    /**
     * 获取解析的节点
     *
     * @return 解析的节点
     */
    Node getNode();
}
