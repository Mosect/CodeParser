package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;

import java.util.List;

public class JavaFactory {

    private final JavaRootFactory rootFactory = new JavaRootFactory();

    public JavaRootFactory getRootFactory() {
        return rootFactory;
    }

    public Node parse(TextSource source, List<ParseError> outErrors) {
        Node result = null;
        try {
            result = rootFactory.parse(null, source, outErrors);
        } catch (ParseException e) {
            e.printStackTrace();
            ParseError parseError = new ParseError(e.getErrorId(), e.getMessage(), e.getPosition());
            outErrors.add(parseError);
        }
        source.adjustErrors(outErrors);
        return result;
    }
}
