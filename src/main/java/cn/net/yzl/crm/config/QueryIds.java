package cn.net.yzl.crm.config;

/**
 * @author: wangxiao
 * @date: 2020/12/16 19:00
 **/
public class QueryIds {

    private QueryIds() {
    }

    public static final ThreadLocal<String> tranceId = new ThreadLocal<>();
    public static final ThreadLocal<String> spanId = new ThreadLocal<>();
    public static final ThreadLocal<String> userNo = new ThreadLocal<>();
//    public static final ThreadLocal<String> userName = new ThreadLocal<>();

}
