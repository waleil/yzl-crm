package cn.net.yzl.crm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="会员基础信息查询条件输入参数类",description="参数类" )
@Data
public class MemberSerchDTO extends PageDTO {
    @ApiModelProperty(value = "顾客名称")
    private String memberName;

    @ApiModelProperty(value = "媒体id")
    private String media_id;

    @ApiModelProperty(value = "广告id")
    private String adver_code;

    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Integer memberStatus;

    @ApiModelProperty(value = "顾客级别  WK 无卡，PK 普卡，TK 铜卡，YK 银卡，JK 金卡，ZS钻卡，VIP VIP，CVIP 超级VIP")
    private String mGradeCode;


    @ApiModelProperty(value = "最后一次下单时间起始时间")
    private Date lastOrderTimeStart;


    @ApiModelProperty(value = "最后一次下单时间结束时间")
    private Date lastOrderTimeEnd;

}
