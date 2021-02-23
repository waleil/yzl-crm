package cn.net.yzl.crm.constant;

/**
 * cn.net.yzl.msg.model.enums
 * 2021/2/23 11:56
 *
 * @author yangxiaopeng
 */
public enum MsgItemToDoEnum {
    NEW_CUSTOMERS(1,"crm-智能工单-新分配顾客数"),
    VISIT_CUSTOMERS(2,"crm-智能工单-待回访顾客数"),
    HOT_LINE_CUSTOMERS(3,"crm-智能工单-热线顾客数"),
    PENDING_HOT_LINE(4,"crm-智能工单-待处理分配热线数"),
    DEAL_WITH_HOT_LINE(5,"crm-智能工单-已处理分配热线数");

    private String name;
    private Integer type;

    MsgItemToDoEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
