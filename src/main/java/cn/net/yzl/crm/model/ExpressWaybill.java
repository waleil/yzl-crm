package cn.net.yzl.crm.model;

import lombok.Data;

import java.util.Date;

@Data
public class ExpressWaybill {

    private String id;

    private String mailId;

    private String orderId;

    private String expressCompanyId;

    private Date deliveryTime;

    private Date receivingTime;

    private Date createTime;

    private String createCode;

    private Date updateTime;

    private String updateCode;


}