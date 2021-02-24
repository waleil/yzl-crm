package cn.net.yzl.crm.dto.ehr;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.util.Date;

@Data
public class StaffTrainDto implements Serializable {

    /**  */
	private static final long serialVersionUID = -665759591466858638L;

	@ApiModelProperty("异动类型(字典表 )")
    private Integer type;

    @ApiModelProperty("异动类型名称")
    private String typeName;

    @ApiModelProperty("异动内容")
    private String content;

    @ApiModelProperty("调整前部门")
    private Integer adjustDepartFront;

    @ApiModelProperty("调整前部门名")
    private String adjustDepartNameFront;

    @ApiModelProperty("调整后部门")
    private Integer adjustDepartLater;

    @ApiModelProperty("调整后部门名")
    private String adjustDepartNameLater;

    @ApiModelProperty("调整前岗位")
    private Integer adjustPostFront;

    @ApiModelProperty("调整前岗位名")
    private String adjustPostNameFront;

    @ApiModelProperty("调整后岗位")
    private Integer adjustPostLater;

    @ApiModelProperty("调整后岗位名")
    private String adjustPostNameLater;

    @ApiModelProperty("调整前薪资")
    private Integer adjustSalaryFront;

    @ApiModelProperty("调整后薪资")
    private Integer adjustSalaryLater;

    @ApiModelProperty("异动时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date abnorTime;

    @ApiModelProperty("调整前岗位等级")
    private Integer adjustPostLevelFront;

    @ApiModelProperty("调整前岗位等级名")
    private String adjustPostLevelFrontName;


    @ApiModelProperty("调整后岗位等级")
    private Integer adjustPostLevelLater;

    @ApiModelProperty("调整后岗位等级名")
    private String adjustPostLevelLaterName;


    @ApiModelProperty("转正时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date positiveTime;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("培训课程名称")
    private String  courseName;


}
