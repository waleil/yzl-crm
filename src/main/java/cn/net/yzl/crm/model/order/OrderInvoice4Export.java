package cn.net.yzl.crm.model.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInvoice4Export extends BaseRowModel implements Serializable {

    @ExcelProperty(value = {"订单编号"},index = 2)
    private String orderNo;

    @ExcelProperty(value = {"财务归主"} ,index = 3)
    private String financialOwnerName;




    @ExcelProperty(value = {"开票金额"},index = 2)
    private String taxMoney;


    @ExcelProperty(value = {"开票状态"},index = 2)
    private Integer invoiceFlag;

    @ApiModelProperty(value = "：0.待发货 1.部分发货 2.已发货  3.部分揽件 4.已揽件 5.运途中 6.部分签收 7.已签收 8.部分拒签 9.全部拒签 10.问题件" )
    private Integer logisticsStatus;

    @ApiModelProperty(value = "已申请发票金额 元" ,required = true)
    private String applayInvoiceAmt;




    @ApiModelProperty(value = "付款方式：0=货到付款，1=款到发货" ,required = true)
    private Integer payType;


    @ApiModelProperty(value = "收货人地址" ,required = true)
    private String reveiverAddress;

    @ApiModelProperty(value = "下单时间" ,required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;



    @ApiModelProperty(value = "开票状态")
    private Integer stats;


    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date invoiceTime;



}
