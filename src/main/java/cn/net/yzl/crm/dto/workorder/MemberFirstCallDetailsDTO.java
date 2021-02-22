package cn.net.yzl.crm.dto.workorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 顾客详情画像，呼入明细
 */
@Data
public class MemberFirstCallDetailsDTO implements Serializable {

    @ApiModelProperty(value = "顾客会员号")
    private String memberCard;

    @ApiModelProperty(value = "呼叫编号")
    private String callId;

    @ApiModelProperty(value = "来源媒体")
    private String sourceMedia;

    @ApiModelProperty(value = "进线咨询的商品")
    private String productName;

    @ApiModelProperty(value = "进线咨询的商品Id")
    private String productId;

    @ApiModelProperty(value = "广告名称")
    private String advName;

    @ApiModelProperty(value = "媒体号")
    private int mediaId;

    @ApiModelProperty(value = "媒体名称")
    private String mediaName;

    @ApiModelProperty(value = "活动id")
    private int activityId;

    @ApiModelProperty(value = "被叫号码")
    private String calledPhone;

    @ApiModelProperty(value = "被叫号码，400电话 ")
    private String phone;

    @ApiModelProperty(value = "记录生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "呼入时间列表")
    public List<String> CallInTimeList;
}
