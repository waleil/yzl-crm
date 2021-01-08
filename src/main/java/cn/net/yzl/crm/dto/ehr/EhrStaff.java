package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * EHR返回员工实体
 */
@Getter
@Setter
@ApiModel("EHR返回员工实体")
public class EhrStaff {
    @ApiModelProperty("异动状态code")
    private Integer abnoStatusCode;
    @ApiModelProperty("异动状态")
    private String abnoStatusCodeStr;
    @ApiModelProperty("异动时间")
    private String abnorTime;
    @ApiModelProperty("账号状态 0正常 1停用")
    private Integer accountStatus;
    @ApiModelProperty("部门id")
    private Integer departId;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("拼音名")
    private String enName;
    @ApiModelProperty("加入次数")
    private Integer entryTimes;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("属性(1:正编,2:外包)")
    private Integer nature;
    @ApiModelProperty("合作方id")
    private Integer partnerCode;
    @ApiModelProperty("合作方名称")
    private String partnerName;
    @ApiModelProperty("")
    private String pdepartId;
    @ApiModelProperty("")
    private String pdepartName;
    @ApiModelProperty("手机号")
    private String phone;
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
    @ApiModelProperty("性别 0:男,1:女")
    private Integer sex;
    @ApiModelProperty("员工工号")
    private String staffNo;
    @ApiModelProperty("职场id(字典表)")
    private Integer workCode;
    @ApiModelProperty("职场")
    private String workCodeStr;
    @ApiModelProperty("工作地点code")
    private Integer workplaceCode;
    @ApiModelProperty("工作地点")
    private String workplaceCodeStr;

}
