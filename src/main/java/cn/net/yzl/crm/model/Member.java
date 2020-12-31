package cn.net.yzl.crm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@ApiModel(value="会员基础信息vo类",description="会员基础信息vo类" )
@Data
public class Member {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "会员卡号")
    @NotEmpty
    private String memberCard;
    @ApiModelProperty(value = "顾客名称")
    private String memberName;
    @ApiModelProperty(value = "称谓")
    private String nickName;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别：0代表男，1代表女")
    private Boolean sex;
    @ApiModelProperty(value = "广告id")
    private Integer adverCode;
    @ApiModelProperty(value = "会员级别id  1 黄金，2 铂金")
    private String mGradeId;
    @ApiModelProperty(value = "0未发卡1已发卡未激活2已激活")
    private Short isActive;
    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Byte memberStatus;
    @ApiModelProperty(value = "活跃度 1 活跃 2 冷淡 3 一般")
    private Boolean activity;
    @ApiModelProperty(value = "所属区")
    private Integer regionCode;
    @ApiModelProperty(value = "所属省份")
    private Integer provinceCode;
    @ApiModelProperty(value = "所属城市id")
    private Integer cityCode;
    @ApiModelProperty(value = "累计消费金额")
    private Integer totalAmount;
    @ApiModelProperty(value = "qq")
    private Integer qq;
    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "获客来源渠道id")
    private Integer source;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "会员生日")
    private Date birthday;
    @ApiModelProperty(value = "所属行业")
    private Integer jobId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "首单下单时间")
    private Date firstOrderTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后一次下单时间")
    private Date lastOrderTime;
    @ApiModelProperty(value = "主客户会员卡号")
    private Integer masterCard;
    @ApiModelProperty(value = "首单下单员工")
    private Integer firstOrderStaffNo;
    @ApiModelProperty(value = "真正首单金额")
    private Integer firstOrderAm;
    @ApiModelProperty(value = "订购次数")
    private Integer orderNum;
    @ApiModelProperty(value = "媒体名称（冗余，暂定第一次进线）")
    private String mediaName;
    @ApiModelProperty(value = "媒体类型id")
    private Integer mediaTypeCode;
    @ApiModelProperty(value = "媒体类型名称（冗余，暂定第一次进线）")
    private String mediaTypeName;

}