package cn.net.yzl.crm.model.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInvoice4Export  implements Serializable {

    @ExcelProperty(value = {"订单编号"},index = 1)
    private String orderNo;

    @ExcelProperty(value = {"财务归属"} ,index = 2)
    private String financialOwnerName;

    @ExcelProperty(value = {"开票方式"} ,index = 3)
    private String taxMode;

    @ExcelProperty(value = {"开票状态"} ,index = 4)
    private String statsStr;

    @ExcelProperty(value = {"付款方式"},index = 5)
    private String payType;

    @ExcelProperty(value = {"发货状态"},index = 6)
    private String logisticsStatus;

    @ExcelProperty(value = {"客户地址"},index = 7)
    private String reveiverAddress;

    @ExcelProperty(value = {"创建时间"},index = 8)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ExcelProperty(value = {"开票时间"},index = 9)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date invoiceTime;



}
