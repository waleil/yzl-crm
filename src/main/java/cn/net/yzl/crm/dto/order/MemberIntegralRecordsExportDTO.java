//package cn.net.yzl.crm.dto.order;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * @author zhouchangsong
// */
//@SuppressWarnings("serial")
//@Data
//@ApiModel("顾客积分——导出对象")
//public class MemberIntegralRecordsExportDTO implements Serializable {
//
//
//    @ExcelProperty(value = {"会员卡号"}, index = 0)
//    @ApiModelProperty("会员卡号")
//    private String memberCard;
//
//    @ExcelProperty(value = {"会员名称"}, index = 1)
//    @ApiModelProperty("会员名称")
//    private String memberName;
//
//    @ExcelProperty(value = {"订单编号"}, index = 2)
//    @ApiModelProperty("订单编号")
//    private String orderNo;
//
//    @ExcelProperty(value = {"财物归属"}, index = 3)
//    @ApiModelProperty("财物归属")
//    private String financialOwnerName;
//
//    @ExcelProperty(value = {"业务环节"}, index = 4)
//    @ApiModelProperty("业务环节（0、领取 1、可用 2、冻结 3、已使用 4、失效）")
//    private String statusName;
//
//    @ExcelProperty(value = {"使用积分"}, index = 5)
//    @ApiModelProperty("使用积分")
//    private Integer integral;
//
//    @ExcelProperty(value = {"创建时间"}, index = 6)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @ApiModelProperty("创建时间")
//    private Date createTime;
//
//    @ExcelProperty(value = {"对账时间"}, index = 7)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @ApiModelProperty("对账时间")
//    private Date reconciliationTime;
//}
