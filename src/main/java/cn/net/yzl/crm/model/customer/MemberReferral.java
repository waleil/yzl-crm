package cn.net.yzl.crm.model.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("转介绍用户")
public class MemberReferral {

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名",required = true)
    private String memberName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别：0代表男，1代表女，2代表未知",required = true)
    private Integer sex;

    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value = "年龄",required = true)
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号",required = true)
    private String MemberPhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "qq")
    private Integer qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "所属省份")
    private Integer provinceCode;
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    @ApiModelProperty(value = "所属城市id")
    private Integer cityCode;
    @ApiModelProperty(value = "所属城市名称")
    private String cityName;

    @ApiModelProperty(value = "所属区域编号")
    private Integer areaCode;
    @ApiModelProperty(value = "所属区域")
    private String areaName;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "活跃度 1：高，2：中，3：底")
    private Integer activity;

    @ApiModelProperty(value = "广告id")
    private Integer advId;

    @ApiModelProperty(value = "广告名称")
    private String advName;

    @ApiModelProperty(value = "分配状态：0:未分配，1：自动分配，2：人工分配")
    private Integer allocateStatus;

    private Integer mediaId;
    @ApiModelProperty(value = "媒体名称")
    private String mediaName;


}
