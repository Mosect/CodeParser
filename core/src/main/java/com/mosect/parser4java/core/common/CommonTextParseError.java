package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.TextParseError;

public class CommonTextParseError implements TextParseError {

    private final String errorId;
    private final String errorMsg;
    private final int errorPosition;

    public CommonTextParseError(String errorId, String errorMsg, int errorPosition) {
        this.errorId = errorId;
        this.errorMsg = errorMsg;
        this.errorPosition = errorPosition;
    }

    @Override
    public String getErrorId() {
        return errorId;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public int getErrorPosition() {
        return errorPosition;
    }

    @Override
    public String toString() {
        return "CommonTextParseError{" +
                "errorId='" + errorId + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", errorPosition=" + errorPosition +
                '}';
    }
}
