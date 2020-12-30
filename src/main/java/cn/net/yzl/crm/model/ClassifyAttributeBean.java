package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ClassifyAttributeBean implements Serializable {

    private Integer classifyId;

    private Integer attributeId;

    private Date createTime;

    private Date updateTime;

}