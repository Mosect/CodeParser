package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;

public class ImportParser extends BaseParser {

    public ImportParser(ParserFactory factory) {
        super(factory);
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_IMPORT;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        return false;
    }
}
