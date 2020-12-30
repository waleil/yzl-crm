package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductDiseaseBean implements Serializable {
    private Integer id;

    private Integer productId;

    private String productCode;

    private Integer diseaseId;

    private Boolean dtype;

    private Date createTime;

    private Date updateTime;

}