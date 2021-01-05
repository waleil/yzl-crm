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

    @ApiModelProperty(value = "获客来源渠道id")
    private Integer source;

    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Short memberStatus;

    @ApiModelProperty(value = "顾客级别  1 黄金，2 铂金")
    private Boolean mGradeId;


    @ApiModelProperty(value = "最后一次下单时间起始时间")
    private Date lastOrderTimeStart;


    @ApiModelProperty(value = "最后一次下单时间结束时间")
    private Date lastOrderTimeEnd;

    @ApiModelProperty(value = "病症id")
    private int diseaseId;

    @ApiModelProperty(value = "病症分类id")
    private int diseasePid;

}
