package cn.net.yzl.crm.model.order;

import cn.net.yzl.logistics.model.ExpressTraceResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel(
        value = "订单详情查询输出参数类",
        description = "参数类"
)
@Data
public class OrderLogistcInfo {
    @ApiModelProperty("快递单号")
    private String mailId;

    @ApiModelProperty("快递公司名称")
    private String companyName;

    @ApiModelProperty("快递跟踪信息")
    List<ExpressTraceResDTO> list;
}
