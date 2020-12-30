package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BrandBean implements Serializable {

    private Integer id;

    private String name;

    private Boolean status;

    private String brandUrl;

    private Boolean delFlag;

    private Integer sort;

    private String descri;

    private String createNo;

    private Date createTime;

    private String updateNo;

    private Date updateTime;

}