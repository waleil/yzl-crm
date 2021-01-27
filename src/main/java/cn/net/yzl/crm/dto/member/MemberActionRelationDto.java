package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * member_action_relation
 * @author
 */
@Data
@ApiModel(value = "顾客综合行为关联表",description = "memberActionRelation")
@Valid
public class MemberActionRelationDto implements Serializable {
    @ApiModelProperty(value = "编号主键,新增时不需填写;单个顾客综合行为关联全部删除时,传id为0和type有值的数据",name ="id" )
    private Integer id;

    @ApiModelProperty(value ="综合行为类型：1方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段",name="type")
    @NotNull
    @Min(0)
    private Integer type;

    @ApiModelProperty(value ="顾客卡号",name="memberCard")
    @NotBlank
    private String memberCard;

    @ApiModelProperty(value ="字典id(手动填写时可不填)",name="did")
    @Min(0)
    private Integer did;

    @ApiModelProperty(value ="字典名称(手动填写时必填)",name="dname")
    private String dname;

    @ApiModelProperty(value ="创建人",name="creator")
    @NotBlank
    private String creator;

    private static final long serialVersionUID = 1L;

}