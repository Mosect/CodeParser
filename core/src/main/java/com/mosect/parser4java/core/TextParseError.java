package com.mosect.parser4java.core;

/**
 * 文本解析错误
 */
public interface TextParseError {
    String getErrorId();

    String getErrorMsg();

    int getErrorPosition();
}
