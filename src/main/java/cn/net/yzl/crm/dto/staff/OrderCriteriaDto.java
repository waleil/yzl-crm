package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("订单列表请求实体")
@Getter
@Setter
public class OrderCriteriaDto {
    @ApiModelProperty(name = "staffNo", value = "员工工号")
    private String staffNo;
    @ApiModelProperty(name = "timeType", value = "时间类型 1昨日 2近七天 3近15天  4近一个月")
    private Integer timeType=1;
    @ApiModelProperty(name = "status", value = "状态 0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4..物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成 9.拒收'")
    private Integer status;
    @ApiModelProperty(name = "pageNo", value = "当前页")
    private Integer pageNo;
    @ApiModelProperty(name = "pageSize", value = "每页数量")
    private Integer pageSize;
}
