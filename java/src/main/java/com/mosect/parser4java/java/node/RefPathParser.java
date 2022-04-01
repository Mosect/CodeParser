package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;

/**
 * 引用路径解析器
 */
public class RefPathParser extends BaseParser {

    public RefPathParser(ParserFactory factory) {
        super(factory);
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_REF_PATH;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        return false;
    }
}
