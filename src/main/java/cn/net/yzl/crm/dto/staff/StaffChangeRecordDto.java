package cn.net.yzl.crm.dto.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * staff_change_record
 * @author 
 */
@ApiModel(value="staffChangeRecordDto")
@Data
public class StaffChangeRecordDto implements Serializable {
    /**
     * id 唯一标识
     */
    @ApiModelProperty(value="id 唯一标识")
    private Integer id;

    /**
     * 变动类型
     */
    @ApiModelProperty(value="变动类型")
    private Integer type;

    /**
     * 变动类型名称
     */
    @ApiModelProperty(value="变动类型名称")
    private String typeName;

    /**
     * 工号
     */
    @ApiModelProperty(value="工号")
    private String no;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value="员工姓名")
    private String name;

    /**
     * 英文名
     */
    @ApiModelProperty(value="英文名")
    private String enName;

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String phone;

    /**
     * 微信号
     */
    @ApiModelProperty(value="微信号")
    private String wechat;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 工作地点 字典表 type: workarea
     */
    @ApiModelProperty(value="工作地点 字典表 type: workarea")
    private Integer workplaceCode;

    /**
     * 工作地点 字典表 type: workarea
     */
    @ApiModelProperty(value="工作地点名称")
    private String workplaceName;

    /**
     * 属性(1:正编,2:外包)
     */
    @ApiModelProperty(value="属性(1:正编,2:外包)")
    private Integer nature;

    /**
     * 合作方id
     */
    @ApiModelProperty(value="合作方id")
    private Integer partnerCode;

    /**
     * 合作方名称
     */
    @ApiModelProperty(value="合作方id")
    private String partnerName;
    
    /**
     * 职场id(字典表)
     */
    @ApiModelProperty(value="职场id(字典表)")
    private Integer workCode;

    /**
     * 职场名称
     */
    @ApiModelProperty(value="职场名称")
    private String workName;

    /**
     * 在职标识(0.离职，1.在职)
     */
    @ApiModelProperty(value="在职标识(0.离职，1.在职)")
    private Integer workStatus;

    /**
     * 在职状态编码（字典type=post_status）
     */
    @ApiModelProperty(value="在职状态编码（字典type=post_status）")
    private Integer postStatusCode;

    /**
     * 在职状态
     */
    @ApiModelProperty(value="在职状态")
    private String postStatusName;

    /**
     * 入岗状态编码（字典表，type=post_state）
     */
    @ApiModelProperty(value="入岗状态编码（字典表，type=post_state）")
    private Integer enterStatus;

    /**
     * 入岗状态名
     */
    @ApiModelProperty(value="入岗状态名")
    private String enterStatusName;

    /**
     * 账号状态 0正常 1停用
     */
    @ApiModelProperty(value="账号状态 0正常 1停用")
    private Integer accountStatus;

    /**
     * 民族编号
     */
    @ApiModelProperty(value="民族编号")
    private Integer nationCode;

    /**
     * 民族名称
     */
    @ApiModelProperty(value="民族名称")
    private String nationName;

    /**
     * 学历编号(指向字典)
     */
    @ApiModelProperty(value="学历编号(指向字典)")
    private Integer degreeCode;

    /**
     * 学历名称
     */
    @ApiModelProperty(value="学历名称")
    private String degreeName;

    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    private String idCardNo;

    /**
     * 专业
     */
    @ApiModelProperty(value="专业")
    private String major;

    /**
     * 政治面貌(字典表)
     */
    @ApiModelProperty(value="政治面貌(字典表)")
    private Integer politicsStatusCode;

    /**
     * 政治面貌名称
     */
    @ApiModelProperty(value="政治面貌名称")
    private String politicsStatusName;


    /**
     * 是否是储备人才 0否 1是
     */
    @ApiModelProperty(value="是否是储备人才 0否 1是")
    private Integer reserveTalent;

    /**
     * 当前薪资
     */
    @ApiModelProperty(value="当前薪资")
    private Double currentSalary;

    /**
     * 领用的物品
     */
    @ApiModelProperty(value="领用的物品")
    private String article;

    /**
     * 已培训次数 
     */
    @ApiModelProperty(value="已培训次数 ")
    private Integer trainingTimes;

    /**
     *最近完成培训日期
     */
    @ApiModelProperty(value="最近完成培训日期 ")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingEndDate;


    /**
     * 最近培训课程编号
     */
    @ApiModelProperty(value="最近培训课程编号")
    private Integer trainingId;

    /**
     * 最近培训课程名称
     */
    @ApiModelProperty(value="最近培训课程名称")
    private String trainingCourseName;

    /**
     * 最近培训产品编号组
     */
    @ApiModelProperty(value="最近培训产品编号组")
    private String trainingProductId;

    /**
     * 最近培训产品名称组
     */
    @ApiModelProperty(value="最近培训产品名称组")
    private String trainingProductName;

    /**
     * 最近培训完成度
     */
    @ApiModelProperty(value="最近培训完成度")
    private String completion;

    /**
     * 最近培训完成度
     */
    @ApiModelProperty(value="最近培训完成度")
    private String grade;

    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id")
    private Integer roleId;

    /**
     * 部门id
     */
    @ApiModelProperty(value="部门id")
    private Integer departId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value="部门名称")
    private String departName;

    /**
     * 岗位id
     */
    @ApiModelProperty(value="岗位id")
    private Integer postId;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value="岗位名称")
    private String postName;

    /**
     * 部门岗位编号
     */
    @ApiModelProperty(value="部门岗位编号")
    private Integer departPostId;

    /**
     * 岗位级别编号
     */
    @ApiModelProperty(value="岗位级别编号")
    private Integer postLevelId;

    /**
     * 岗位级别名称
     */
    @ApiModelProperty(value="岗位级别名称")
    private String postLevelName;

    /**
     * 负责人工号
     */
    @ApiModelProperty(value="负责人工号")
    private String leaderNo;

    /**
     * 负责人姓名
     */
    @ApiModelProperty(value="负责人姓名")
    private String leaderName;

    /**
     * 1级组织结构部门编号
     */
    @ApiModelProperty(value="1级组织结构部门编号")
    private Integer level1DepartId;

    /**
     * 2级组织结构部门编号
     */
    @ApiModelProperty(value="2级组织结构部门编号")
    private Integer level2DepartId;

    /**
     * 3级组织结构部门编号
     */
    @ApiModelProperty(value="3级组织结构部门编号")
    private Integer level3DepartId;

    /**
     * 4级组织结构部门编号
     */
    @ApiModelProperty(value="4级组织结构部门编号")
    private Integer level4DepartId;

    /**
     * 5级组织结构部门编号
     */
    @ApiModelProperty(value="5级组织结构部门编号")
    private Integer level5DepartId;

    /**
     * 6级组织结构部门编号
     */
    @ApiModelProperty(value="6级组织结构部门编号")
    private Integer level6DepartId;

    /**
     * 7级组织结构部门编号
     */
    @ApiModelProperty(value="7级组织结构部门编号")
    private Integer level7DepartId;

    /**
     * 8级组织结构部门编号
     */
    @ApiModelProperty(value="8级组织结构部门编号")
    private Integer level8DepartId;

    /**
     * 9级组织结构部门编号
     */
    @ApiModelProperty(value="9级组织结构部门编号")
    private Integer level9DepartId;

    /**
     * 10级组织结构部门编号
     */
    @ApiModelProperty(value="10级组织结构部门编号")
    private Integer level10DepartId;

    /**
     * 1级组织结构部门名称
     */
    @ApiModelProperty(value="1级组织结构部门名称")
    private String level1DepartName;

    /**
     * 2级组织结构部门名称
     */
    @ApiModelProperty(value="2级组织结构部门名称")
    private String level2DepartName;

    /**
     * 3级组织结构部门名称
     */
    @ApiModelProperty(value="3级组织结构部门名称")
    private String level3DepartName;

    /**
     * 4级组织结构部门名称
     */
    @ApiModelProperty(value="4级组织结构部门名称")
    private String level4DepartName;

    /**
     * 5级组织结构部门名称
     */
    @ApiModelProperty(value="5级组织结构部门名称")
    private String level5DepartName;

    /**
     * 6级组织结构部门名称
     */
    @ApiModelProperty(value="6级组织结构部门名称")
    private String level6DepartName;

    /**
     * 7级组织结构部门名称
     */
    @ApiModelProperty(value="7级组织结构部门名称")
    private String level7DepartName;

    /**
     * 8级组织结构部门名称
     */
    @ApiModelProperty(value="8级组织结构部门名称")
    private String level8DepartName;

    /**
     * 9级组织结构部门名称
     */
    @ApiModelProperty(value="9级组织结构部门名称")
    private String level9DepartName;

    /**
     * 10级组织结构部门名称
     */
    @ApiModelProperty(value="10级组织结构部门名称")
    private String level10DepartName;

    /**
     * 1级组织结构负责人工号
     */
    @ApiModelProperty(value="1级组织结构负责人工号")
    private String level1LeaderNo;

    /**
     * 2级组织结构负责人工号
     */
    @ApiModelProperty(value="2级组织结构负责人工号")
    private String level2LeaderNo;

    /**
     * 3级组织结构负责人工号
     */
    @ApiModelProperty(value="3级组织结构负责人工号")
    private String level3LeaderNo;

    /**
     * 4级组织结构负责人工号
     */
    @ApiModelProperty(value="4级组织结构负责人工号")
    private String level4LeaderNo;

    /**
     * 5级组织结构负责人工号
     */
    @ApiModelProperty(value="5级组织结构负责人工号")
    private String level5LeaderNo;

    /**
     * 6级组织结构负责人工号
     */
    @ApiModelProperty(value="6级组织结构负责人工号")
    private String level6LeaderNo;

    /**
     * 7级组织结构负责人工号
     */
    @ApiModelProperty(value="7级组织结构负责人工号")
    private String level7LeaderNo;

    /**
     * 8级组织结构负责人工号
     */
    @ApiModelProperty(value="8级组织结构负责人工号")
    private String level8LeaderNo;

    /**
     * 9级组织结构负责人工号
     */
    @ApiModelProperty(value="9级组织结构负责人工号")
    private String level9LeaderNo;

    /**
     * 10级组织结构负责人工号
     */
    @ApiModelProperty(value="10级组织结构负责人工号")
    private String level10LeaderNo;

    /**
     * 1级组织结构负责人姓名
     */
    @ApiModelProperty(value="1级组织结构负责人姓名")
    private String level1LeaderName;

    /**
     * 2级组织结构负责人姓名
     */
    @ApiModelProperty(value="2级组织结构负责人姓名")
    private String level2LeaderName;

    /**
     * 3级组织结构负责人姓名
     */
    @ApiModelProperty(value="3级组织结构负责人姓名")
    private String level3LeaderName;

    /**
     * 4级组织结构负责人姓名
     */
    @ApiModelProperty(value="4级组织结构负责人姓名")
    private String level4LeaderName;

    /**
     * 5级组织结构负责人姓名
     */
    @ApiModelProperty(value="5级组织结构负责人姓名")
    private String level5LeaderName;

    /**
     * 6级组织结构负责人姓名
     */
    @ApiModelProperty(value="6级组织结构负责人姓名")
    private String level6LeaderName;

    /**
     * 7级组织结构负责人姓名
     */
    @ApiModelProperty(value="7级组织结构负责人姓名")
    private String level7LeaderName;

    /**
     * 8级组织结构负责人姓名
     */
    @ApiModelProperty(value="8级组织结构负责人姓名")
    private String level8LeaderName;

    /**
     * 9级组织结构负责人姓名
     */
    @ApiModelProperty(value="9级组织结构负责人姓名")
    private String level9LeaderName;

    /**
     * 10级组织结构负责人姓名
     */
    @ApiModelProperty(value="10级组织结构负责人姓名")
    private String level10LeaderName;

    /**
     * 预留字符串1
     */
    @ApiModelProperty(value="预留字符串1")
    private String ylstr1;

    /**
     * 预留字符串2
     */
    @ApiModelProperty(value="预留字符串2")
    private String ylstr2;

    /**
     * 预留字符串3
     */
    @ApiModelProperty(value="预留字符串3")
    private String ylstr3;

    /**
     * 预留字符串4
     */
    @ApiModelProperty(value="预留字符串4")
    private String ylstr4;

    /**
     * 预留字符串5
     */
    @ApiModelProperty(value="预留字符串5")
    private String ylstr5;

    /**
     * 预留字符串6
     */
    @ApiModelProperty(value="预留字符串6")
    private String ylstr6;

    /**
     * 预留数字1
     */
    @ApiModelProperty(value="预留数字1")
    private Integer ylnum1;

    /**
     * 预留数字2
     */
    @ApiModelProperty(value="预留数字2")
    private Integer ylnum2;

    /**
     * 预留数字3
     */
    @ApiModelProperty(value="预留数字3")
    private Integer ylnum3;

    /**
     * 预留数字4
     */
    @ApiModelProperty(value="预留数字4")
    private Integer ylnum4;

    /**
     * 离职说明
     */
    @ApiModelProperty(value="离职说明")
    private String leaveReason;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 创建用户工号
     */
    @ApiModelProperty(value="创建用户工号")
    private String creator;

    private static final long serialVersionUID = 1L;
}