package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductBean implements Serializable {

    private Integer id;

    private String productCode;

    private Integer productNo;

    private String businessCode;

    private String num;

    private String name;

    private Integer withholdStock;

    private String nickname;

    private String engName;

    private Boolean goodsSource;

    private Integer brandNo;

    private Integer categoryDictCode;

    private String spec;

    private String unit;

    private String barCode;

    private String licence;

    private Boolean giftFlag;

    private Boolean jfExchangeFlag;

    private Integer jfPrice;

    private String financeCode;

    private Integer producterNo;

    private Integer supplierNo;

    private Integer salePrice;

    private Integer memberPrice;

    private Integer costPrice;

    private Integer bestPrice;

    private Integer ykPrice;

    private Integer jkPrice;

    private Integer zkPrice;

    private Integer stock;

    private Integer stockThreshold;

    private Integer availableStock;

    private Boolean salePattern;

    private Date saleStartTime;

    private Date saleEndTime;

    private Byte status;

    private Boolean patentNumFlag;

    private String batchCode;

    private Integer weight;

    private Float lengths;

    private Float wide;

    private Float high;

    private Float volume;

    private Integer expirationDate;

    private Date validDate;

    private Integer aqlDate;

    private String mpcg;

    private String totalUseNum;

    private String oneUseNum;

    private String avalue;

    private String oneToTimes;

    private String cjName;

    private Integer cjCountryNo;

    private Integer cjProvinceNo;

    private Integer cjCityNo;

    private Integer cjAreaNo;

    private String cjAddr;

    private Boolean delFlag;

    private Integer oldId;

    private String createNo;

    private Date createTime;

    private String updateNo;

    private Date updateTime;

}