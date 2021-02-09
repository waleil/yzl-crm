package cn.net.yzl.crm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author zhouchangsong
 */

@Getter
@AllArgsConstructor
public enum EhrActivityStatus {

    /**
     * 售后订单-售后单方式
     */
    EHR_ACTIVITY_STATUS_0(0, "领取"),
    EHR_ACTIVITY_STATUS_1(1, "可用"),
    EHR_ACTIVITY_STATUS_2(2, "冻结"),
    EHR_ACTIVITY_STATUS_3(3, "已使用"),
    EHR_ACTIVITY_STATUS_4(4, "失效"),
    ;

    private int code;
    private String name;

    public byte getByteCode() {
        return (byte) this.code;
    }

    public static String getName(Integer code) {
        return Arrays.stream(values()).filter(p -> String.valueOf(p.code).equals(String.valueOf(code))).findFirst()
                .map(EhrActivityStatus::getName).orElse(null);
    }
}
