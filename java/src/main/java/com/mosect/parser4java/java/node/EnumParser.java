package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;

public class EnumParser extends BaseParser {

    public EnumParser(ParserFactory factory) {
        super(factory);
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_ENUM;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        return false;
    }
}
