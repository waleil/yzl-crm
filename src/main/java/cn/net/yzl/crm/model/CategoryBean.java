package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CategoryBean implements Serializable{

    private Integer id;

    private String name;

    private Integer pid;

    private Integer sort;

    private Boolean displayFlag;

    private Boolean displayAppFlag;

    private Boolean status;

    private String unit;

    private String imageUrl;

    private String keyWord;

    private String descri;

    private Boolean delFlag;

    private String createNo;

    private Date createTime;

    private String updateNo;

    private Date updateTime;

    private List<AttributeBean> attributeBeans;

}