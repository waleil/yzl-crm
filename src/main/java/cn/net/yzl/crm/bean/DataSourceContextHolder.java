package cn.net.yzl.crm.bean;


/**
 * @author : zhangruisong
 * @date : 2020/12/8 17:55
 * @description:
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> DYNAMIC_DATASOURCE_CONTEXT = new ThreadLocal<>();
    public static void set(String datasourceType) {
        DYNAMIC_DATASOURCE_CONTEXT.set(datasourceType);
    }
    public static String get() {
        return DYNAMIC_DATASOURCE_CONTEXT.get();
    }
    public static void clear() {
        DYNAMIC_DATASOURCE_CONTEXT.remove();
    }
}
