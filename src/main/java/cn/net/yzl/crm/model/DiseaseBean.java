package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DiseaseBean implements Serializable {
    private Integer id;

    private String name;

    private Integer pid;

    private Integer sort;

    private String createNo;

    private String updateNo;

    private Boolean delFlag;

    private Date createTime;

    private Date updateTime;

}