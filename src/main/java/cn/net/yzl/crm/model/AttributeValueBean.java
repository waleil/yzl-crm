package cn.net.yzl.crm.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AttributeValueBean implements Serializable {

    private Integer id;
    private Integer attributeId;
    private String avalue;
    private Integer delFlag;
    private Date createTime;
    private Date updateTime;

}
