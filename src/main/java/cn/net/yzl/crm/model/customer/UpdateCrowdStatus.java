package cn.net.yzl.crm.model.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * @author lichanghong
 * @version 1.0
 * @title: UpdateCrowdStatus
 * @description todo
 * @date: 2021/1/22 8:35 下午
 */
@Data
public class UpdateCrowdStatus {
    @ApiModelProperty(name = "_id",value ="群组id")
    private String _id;

    @ApiModelProperty(name = "enable",value ="是否启用:0=否，1=是")
    @DecimalMin(value = "0",message = "非法状态")
    @DecimalMax(value = "1",message = "非法状态")
    private Integer enable;
}
