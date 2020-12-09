package cn.net.yzl.crm.model;

import java.util.Date;

public class ExpressAbnormalWaybill {
    private Integer rId;

    private String id;

    private String mailId;

    private String orderId;

    private Byte billStatus;

    private Date createTime;

    private String createCode;

    private Date updateTime;

    private String updateCode;

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId == null ? null : mailId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateCode() {
        return createCode;
    }

    public void setCreateCode(String createCode) {
        this.createCode = createCode == null ? null : createCode.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode == null ? null : updateCode.trim();
    }
}