package cn.net.yzl.crm.dto.staff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "StaffTrainProductDto",description = "员工培训过商品信息")
public class EhrStaffTrainProductDto {
    @ApiModelProperty(value = "产品code",name = "productCode")
    private String productCode;
    @ApiModelProperty(value = "成绩",name = "grade")
    private String grade;
    @ApiModelProperty(value = "商品名称",name = "productName")
    private String productName;
}
