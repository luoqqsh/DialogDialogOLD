package x.com.hl_log.common;

import java.util.List;

import x.com.hl_log.config.LogDefaultConfig;
import x.com.hl_log.parser.BundleParse;
import x.com.hl_log.parser.CollectionParse;
import x.com.hl_log.parser.IntentParse;
import x.com.hl_log.parser.MapParse;
import x.com.hl_log.parser.Parser;
import x.com.hl_log.parser.ReferenceParse;
import x.com.hl_log.parser.ThrowableParse;

/**
 * @Description: 日志常量
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/12/11 10:55.
 */
public class LogConstant {

    public static final String STRING_OBJECT_NULL = "Object[object is null]";

    // 每行最大日志长度
    public static final int LINE_MAX = 1024 * 3;

    // 解析属性最大层级
    public static final int MAX_CHILD_LEVEL = 2;

    public static final int MIN_STACK_OFFSET = 5;

    // 换行符
    public static final String BR = System.getProperty("line.separator");

    // 分割线方位
    public static final int DIVIDER_TOP = 1;
    public static final int DIVIDER_BOTTOM = 2;
    public static final int DIVIDER_CENTER = 4;
    public static final int DIVIDER_NORMAL = 3;

    // 默认支持解析库
    public static final Class[] DEFAULT_PARSE_CLASS = new Class[]{
            BundleParse.class, IntentParse.class, CollectionParse.class,
            MapParse.class, ThrowableParse.class, ReferenceParse.class
    };

    /**
     * 获取默认解析类
     *
     * @return
     */
    public static List<Parser> getParsers() {
        return LogDefaultConfig.getInstance().getParseList();
    }
}
