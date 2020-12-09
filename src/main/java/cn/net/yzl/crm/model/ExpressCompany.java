package cn.net.yzl.crm.model;

import lombok.Data;

import java.util.Date;

@Data
public class ExpressCompany {

    private String id;

    private Integer serialNo;

    private String code;

    private String name;

    private String outlets;

    private String outletsAddr;

    private String contacts;

    private String contactNumber;

    private Date cooperationStartTime;

    private Integer collectionPayment;

    private Integer status;

    private Date createTime;

    private String createCode;

    private Date updateTime;

    private String updateCode;


}