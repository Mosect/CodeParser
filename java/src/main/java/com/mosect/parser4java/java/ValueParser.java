package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.javalike.NumberParser;

/**
 * 值解析器
 */
public class ValueParser extends CommonTextParser {

    private NumberParser numberParser = new NumberParser();

    @Override
    protected void onParse(CharSequence text, int start) {
        NumberParser numberParser = getNumberParser();
        numberParser.parse(text, start);
        if (numberParser.isPass()) {
            finishParse(true, text.length());
            putErrorWithOther(numberParser);
            if (numberParser.getTextEnd() < text.length()) {
                putError("VALUE_INVALID_END", "Invalid value end", numberParser.getTextEnd());
            }
        } else {
            finishParse(false, start);
        }
    }

    public NumberParser getNumberParser() {
        return numberParser;
    }

    protected void setNumberParser(NumberParser numberParser) {
        this.numberParser = numberParser;
    }

    @Override
    public String getName() {
        return "java.value";
    }
}
