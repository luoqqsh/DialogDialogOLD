package x.com.hl_log.parser;


import x.com.hl_log.common.LogConstant;

/**
 * @Description: 解析器接口
 */
public interface Parser<T> {
    String LINE_SEPARATOR = LogConstant.BR;

    Class<T> parseClassType();

    String parseString(T t);
}
