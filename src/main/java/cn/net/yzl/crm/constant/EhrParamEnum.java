package cn.net.yzl.crm.constant;

/**
 * 调用ehr系统固定参数枚举
 */
public enum EhrParamEnum {

    EhrParamEnum() {
    };
    /**
     * 岗位字典   在职状态
     */
    public static final  String EHR_DICT_STAFF_STATUS = "post_status";
    /**
     * 职场
     */
    public static final  String EHR_DICT_WORKPLACE_STATUS = "workplace";

    /**
     * 业务属性   热线
     */
    public static final  int EHR_HOTLINE_BUSINESS_ATTRIBUTE_CODE = 42;
    /**
     * 业务属性   回访
     */
    public static final  int EHR_VISIT_BUSINESS_ATTRIBUTE_CODE = 43;
}
