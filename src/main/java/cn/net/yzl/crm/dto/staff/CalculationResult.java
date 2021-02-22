package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/2/20 21:50
 * @desc: 试算返回用户详情
 **/
@Data
@ApiModel("试算返回圈选结果")
public class CalculationResult {

    @ApiModelProperty(value = "圈选数量")
    private Integer userNoSize;

    @ApiModelProperty(value = "示例数量")
    private Integer exampleSize;

    @ApiModelProperty(value = "返回用户详情")
    private List<CalculationUerDetail> calculationUerDetail;


}
