package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.NodeParser;
import com.mosect.parser4java.core.NodeParserFactory;
import com.mosect.parser4java.java.NameConstants;

/**
 * 解析器工厂
 */
public class ParserFactory extends NodeParserFactory {

    @Override
    protected NodeParser createByName(String name) {
        switch (name) {
            case NameConstants.PARSER_IGNORE: // 忽略解析器
                return new IgnoreParser(this);
            case NameConstants.PARSER_PACKAGE: // 包解析器
                return new PackageParser(this);
            case NameConstants.PARSER_IMPORT: // import解析器
                return new ImportParser(this);
            case NameConstants.PARSER_CLASS: // 类解析器
                return new ClassParser(this);
            case NameConstants.PARSER_ENUM: // 枚举解析器
                return new EnumParser(this);
            case NameConstants.PARSER_INTERFACE: // 接口解析器
                return new InterfaceParser(this);
            case NameConstants.PARSER_ANNOTATION: // 注解解析器
                return new AnnotationParser(this);
            case NameConstants.PARSER_REF_PATH:
                return new RefPathParser(this);
            default:
                throw new IllegalArgumentException("Unsupported parser name: " + name);
        }
    }
}
