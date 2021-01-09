package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 员工画像基本信息实体类
 */
@ApiModel("员工画像基本信息实体类")
@Getter
@Setter
public class StaffImageBaseInfoDto {
    @ApiModelProperty("地址详情")
    private String address;
//    @ApiModelProperty("领取物品")
//    private String article;
//    @ApiModelProperty("银行卡号")
//    private String bankCard;
//    @ApiModelProperty("开户行名称")
//    private String bankName;
//    @ApiModelProperty("生日日期")
//    private String birthdate;
//    @ApiModelProperty("市id")
//    private Integer cityId;
//    @ApiModelProperty("区/县id")
//    private Integer countyId;
//    @ApiModelProperty("学历code")
//    private Integer degreeCode;
//    @ApiModelProperty("学历")
//    private String degreeCodeStr;
    @ApiModelProperty("部门ids")
    private String departIds;
    @ApiModelProperty("部门ids对应的名称集合")
    private String departNames;
//    @ApiModelProperty("电子邮件")
//    private String email;
//    @ApiModelProperty("紧急联系人")
//    private String emergencyContact;
//    @ApiModelProperty("紧急联系人手机号")
//    private String emergencyPhone;
//    @ApiModelProperty("拼音名")
//    private String enName;
    @ApiModelProperty("入岗状态 0待入岗 1已入岗")
    private Integer enterStatus;
    @ApiModelProperty("入职时间")
    private String entryTime;
//    @ApiModelProperty("入职次数")
//    private Integer entryTimes;
//    @ApiModelProperty("初试密码")
//    private String firstPassword;
//    @ApiModelProperty("身份证号")
//    private String idCardNo;
//    @ApiModelProperty("保险备注")
//    private String insuraRemark;
//    @ApiModelProperty("介绍人")
//    private String introdName;
//    @ApiModelProperty("介绍人工号")
//    private String introdNo;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("民族code")
    private Integer nationCode;
    @ApiModelProperty("民族")
    private String nationCodeStr;
    @ApiModelProperty("属性(1:正编,2:外包)")
    private Integer nature;
//    @ApiModelProperty("合作方code")
//    private Integer partnerCode;
//    @ApiModelProperty("合作方名称")
//    private String partnerName;
    @ApiModelProperty("手机号")
    private String phone;
//    @ApiModelProperty("政治code")
//    private Integer politicsStatusCode;
//    @ApiModelProperty("政治")
//    private String politicsStatusCodeStr;
    @ApiModelProperty("转正时间")
    private String positiveTime;
    @ApiModelProperty("岗位id")
    private Integer postId;
    @ApiModelProperty("岗位级别id")
    private Integer postLevelId;
    @ApiModelProperty("岗位级别名称")
    private String postLevelName;
    @ApiModelProperty("岗位名称")
    private String postName;
    @ApiModelProperty("在岗状态")
    private Integer postStatusCode;
    @ApiModelProperty("在岗状态名称")
    private String postStatusCodeStr;
    @ApiModelProperty("入岗时间")
    private String postTime;
//    @ApiModelProperty("省id")
//    private String provinceId;
//    @ApiModelProperty("是否住宿 0否 1是")
//    private Integer putUp;
//    @ApiModelProperty("角色id")
//    private Integer roleId;
//    @ApiModelProperty("角色名称")
//    private String roleName;
//    @ApiModelProperty("是否有销售经验 0否 1是")
//    private Integer salesFlag;
    @ApiModelProperty("性别 0:男,1:女")
    private Integer sex;
    @ApiModelProperty("员工工号(用户工号)")
    private String staffNo;
//    @ApiModelProperty("街道id")
//    private Integer streetId;
//    @ApiModelProperty("工号(用户工号)")
//    private String userNo;
//    @ApiModelProperty("微信号")
//    private String wechat;
    @ApiModelProperty("职场id(字典表)")
    private Integer workCode;
    @ApiModelProperty("职场")
    private String workCodeStr;
    @ApiModelProperty("工作地点code")
    private Integer workplaceCode;
    @ApiModelProperty("工作地点")
    private String workplaceCodeStr;
    @ApiModelProperty("专业")
    private String major;
    @ApiModelProperty("资质证照")
    private String staffQuaName;
    @ApiModelProperty("户口地址")
    private String registeredAddress;
    @ApiModelProperty("培训状态(0:未完成,1:已完成)")
    private Integer trainingStatus;
    @ApiModelProperty("商品优势")
    private List<String> productAdvanced;
    @ApiModelProperty("病症优势")
    private List<String> diseaseAdvanced;
    @ApiModelProperty("培训商品历史")
    private List<String> trainProductHistory;
}
