package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductBean implements Serializable {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("产品编号")
    private String productCode;

    @ApiModelProperty("商品spuId")
    private Integer productNo;

    @ApiModelProperty("商家编码")
    private String businessCode;

    @ApiModelProperty("快速搜索码")
    private String num;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("预扣库存")
    private Integer withholdStock;

    @ApiModelProperty("简称")
    private String nickname;

    @ApiModelProperty("英文名称")
    private String engName;

    @ApiModelProperty("商品来源（1：自营 2：三方）")
    private Boolean goodsSource;

    @ApiModelProperty("品牌id")
    private Integer brandNo;

    @ApiModelProperty("分类id")
    private Integer categoryDictCode;

    @ApiModelProperty("规格")
    private String spec;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("商品条形码")
    private String barCode;

    @ApiModelProperty("产品投放许可")
    private String licence;

    @ApiModelProperty("是否可作为赠品,0否,1:是")
    private Boolean giftFlag;

    @ApiModelProperty("是否可以积分兑换 0-否 1-是")
    private Boolean jfExchangeFlag;

    @ApiModelProperty("兑换所需积分")
    private Integer jfPrice;

    @ApiModelProperty("财务编码")
    private String financeCode;

    @ApiModelProperty("生产商id")
    private Integer producterNo;

    @ApiModelProperty("供应商id")
    private Integer supplierNo;

    @ApiModelProperty("市场价(售卖价)")
    private Integer salePrice;

    @ApiModelProperty("会员价(最低折扣优惠价),以分为单位")
    private Integer memberPrice;

    @ApiModelProperty("成本价,以分为单位")
    private Integer costPrice;

    @ApiModelProperty("特惠价,以分为单位")
    private Integer bestPrice;

    @ApiModelProperty("银卡价")
    private Integer ykPrice;

    @ApiModelProperty("金卡价")
    private Integer jkPrice;

    @ApiModelProperty("钻石价")
    private Integer zkPrice;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("库存阈值")
    private Integer stockThreshold;

    @ApiModelProperty("可供货量")
    private Integer availableStock;

    @ApiModelProperty("销售模式(0现售,1代表预售)")
    private Boolean salePattern;

    @ApiModelProperty("销售开始日期")
    private Date saleStartTime;

    @ApiModelProperty("销售结束日期")
    private Date saleEndTime;

    @ApiModelProperty("0-下架 1-上架")
    private Byte status;

    @ApiModelProperty("是否有发明专利号(0代表没有,1代表有)")
    private Boolean patentNumFlag;

    @ApiModelProperty("批次")
    private String batchCode;

    @ApiModelProperty("重量(g)")
    private Integer weight;

    @ApiModelProperty("长")
    private Float lengths;

    @ApiModelProperty("宽")
    private Float wide;

    @ApiModelProperty("高")
    private Float high;

    @ApiModelProperty("体积")
    private Float volume;

    @ApiModelProperty("保质期")
    private Integer expirationDate;

    @ApiModelProperty("有效期")
    private Date validDate;

    @ApiModelProperty("允收期")
    private Integer aqlDate;

    @ApiModelProperty("码盘层高")
    private String mpcg;

    @ApiModelProperty("商品总药量")
    private String totalUseNum;

    @ApiModelProperty("基准用药量")
    private String oneUseNum;

    @ApiModelProperty("商品动态属性")
    private String avalue;

    @ApiModelProperty("基准用药次数")
    private String oneToTimes;

    @ApiModelProperty("厂家名称")
    private String cjName;

    @ApiModelProperty("国家")
    private Integer cjCountryNo;

    @ApiModelProperty("省")
    private Integer cjProvinceNo;

    @ApiModelProperty("市")
    private Integer cjCityNo;

    @ApiModelProperty("区或县")
    private Integer cjAreaNo;

    @ApiModelProperty("详细地址")
    private String cjAddr;

    @ApiModelProperty("是否删除")
    private Boolean delFlag;

    @ApiModelProperty("旧id")
    private Integer oldId;

    @ApiModelProperty("添加操作员id")
    private String createNo;

    @ApiModelProperty("创建时期")
    private Date createTime;

    @ApiModelProperty("修改操作员id")
    private String updateNo;

    @ApiModelProperty("更新时期")
    private Date updateTime;

}