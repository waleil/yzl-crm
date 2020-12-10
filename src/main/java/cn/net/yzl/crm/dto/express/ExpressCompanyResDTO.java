package cn.net.yzl.crm.dto.express;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="快递公司返回值类",description="返回值类" )
@Data
public class ExpressCompanyResDTO {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "序号")
    private Integer serialNo;

    @ApiModelProperty(value = "公司编码")
    private String code;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "合作网点")
    private String outlets;

    @ApiModelProperty(value = "合作网点地址")
    private String outletsAddr;

    @ApiModelProperty(value = "联系人/负责人")
    private String contacts;

    @ApiModelProperty(value = "联系人/负责人电话")
    private String contactNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "合作开始时间")
    private Date cooperationStartTime;

    @ApiModelProperty(value = "是否代收货款：0=否，1=是")
    private Integer collectionPayment;

    @ApiModelProperty(value = "状态：0=停用，1=启用")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;
}
