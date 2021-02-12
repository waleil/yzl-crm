package cn.net.yzl.crm.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel(value = "MemberServiceJournery", description = "顾客服务旅程实体信息")
@Data
public
class MemberServiceJournery {
    @ApiModelProperty(value = "顾客卡号", name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "员工工号", name = "staffNo")
    private String staffNo;
    @ApiModelProperty(value = "开始时间(yyyy年MM月dd日)", name = "startTime")
    @JsonFormat(pattern = "yyyy年MM月dd日",timezone="GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束时间(yyyy年MM月dd日)", name = "endTime")
    @JsonFormat(pattern = "yyyy年MM月dd日",timezone="GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "累计消费", name = "totalPrice")
    private double totalPrice;



}
