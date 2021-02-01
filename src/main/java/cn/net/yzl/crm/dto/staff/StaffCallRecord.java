package cn.net.yzl.crm.dto.staff;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
public class StaffCallRecord {

    @Id
    @ApiModelProperty(value = "主键id")
    @JsonAlias("_id")
    private String id;

    @ApiModelProperty(value = "员工号")
    private String staffNo;

    @ApiModelProperty(value = "顾客会员号")
    private String memberCard;

    @ApiModelProperty(value = "呼叫编号")
    private String callId;

    @ApiModelProperty(value = "关联订单号")
    private String orderNo;

    @ApiModelProperty(value = "关联工单号")
    private long workOrderNo;

    @ApiModelProperty(value = "主叫号码")
    private String callerPhone;

    @ApiModelProperty(value = "被叫号码")
    private String calledPhone;

    @ApiModelProperty(value = "通话时长 单位为秒")
    private int duration;

    @ApiModelProperty(value = "记录mongoDB的一个key")
    private String content;

    @ApiModelProperty(value = "录音url地址")
    private String voiceUrl;

    @ApiModelProperty(value = "接听时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date answerTime;

    @ApiModelProperty(value = "记录生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "工单类型   1：热线，2：回访")
    private int workOrderType;

    @ApiModelProperty(value = "座席名称")
    private String staffName;

    @ApiModelProperty(value = "顾客名称")
    private String memberName;

    @ApiModelProperty(value = "部门编号")
    public int deptNo;

    @ApiModelProperty(value = "座席语音url地址")
    private String staffVoiceUrl;

    @ApiModelProperty(value = "通话状态 0、正常 1、无效 2、掐线 3、未接听")
    private Integer status;
}
