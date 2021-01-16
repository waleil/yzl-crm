package cn.net.yzl.crm.utils;

import lombok.Data;

/**
 * 用户信息
 */
public class UserInfoUtil {

    /**
     * 用户标识
     */
    private static String userId;

    /**
     * 用户名称
     */
    private static String userName;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserInfoUtil.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfoUtil.userName = userName;
    }
}
