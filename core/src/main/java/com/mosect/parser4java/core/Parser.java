package com.mosect.parser4java.core;

/**
 * 解析器
 */
public interface Parser {

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    String getName();

    /**
     * 判断是否解析通过
     * 注意：解析通过并不代表解析成功，要通过hasError方法判断是否有错误
     *
     * @return true，解析通过；false，解析不通过，即当前文本不适合此解析器
     */
    boolean isPass();

    /**
     * 判断是否有错误
     *
     * @return true，有错误；false，没有错误
     */
    default boolean hasError() {
        return getErrorCount() > 0;
    }

    /**
     * 判断是否解析成功
     *
     * @return true，解析成功；false，解析失败
     */
    default boolean isSuccess() {
        return isPass() && !hasError();
    }

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    int getErrorCount();

    /**
     * 获取错误
     *
     * @param index 下标
     * @return 错误
     */
    ParseError getError(int index);

    /**
     * 获取文本开始位置
     *
     * @return 文本开始位置
     */
    int getTextStart();

    /**
     * 获取文本结束位置
     *
     * @return 文本结束位置
     */
    int getTextEnd();

    /**
     * 获取文本
     *
     * @return 文本
     */
    CharSequence getText();

    /**
     * 获取解析的文本
     *
     * @return 解析的文本
     */
    CharSequence getParseText();
}
