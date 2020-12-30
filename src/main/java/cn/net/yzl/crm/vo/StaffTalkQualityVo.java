package cn.net.yzl.crm.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @ Author     ：liufaguan
 * @ Date       ：2020/12/30 15:16
 * @ Description：员工话术质检vo
 * @Version: 1
 */
@ApiModel(value="员工话术质检")
@Data
public class StaffTalkQualityVo {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",required = true)
    private String staffTalkCode;

    /**
     * 质检部门 0.回访中心 1.热线中心 2.全部
     */
    @ApiModelProperty(value = "质检部门",required = true)
    private String qualityDepartment;

    /**
     * 触发条件：0.未检测到 1.已检测到
     */
    @ApiModelProperty(value = "触发条件")
    private String triggerFlag;

    /**
     * 质检名称
     */
    @ApiModelProperty(value = "质检名称",required = true)
    private String qualityName;

    /**
     * 质检关键词
     */
    @ApiModelProperty(value = "质检关键词",required = true)
    private String keyword;

    /**
     * 处罚说明
     */
    @ApiModelProperty(value = "处罚说明",required = true)
    private String punishDescription;

    /**
     * 状态 0.未使用 1.已使用 2.已停用
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人",required = true)
    private String operationName;

    /**
     * 操作人编码
     */
    @ApiModelProperty(value = "操作人编码",required = true)
    private String operationCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
