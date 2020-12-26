package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CategoryBean implements Serializable{

    private Integer id;

    private String name;//分类名称

    private Integer pid;//父类id

    private Integer sort;//排序

    private Boolean displayFlag;//是否展示

    private Boolean displayAppFlag;//是否在移动端显示

    private Boolean status;//状态（是否有效）

    private String unit;//计量单位

    private String imageUrl;//分类图标的url

    private String keyWord;//关键词

    private String descri;//描述

    private Boolean delFlag;//是否删除

    private String createNo;//增加操作员id

    private Date createTime;//添加时间

    private String updateNo;//最近更新操作员id

    private Date updateTime;//最近更新时间

    private List<AttributeBean> attributeBeans;

}