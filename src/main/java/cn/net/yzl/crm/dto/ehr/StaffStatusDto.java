package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("Ehr返回员工职场实体")
public class StaffStatusDto {
    @ApiModelProperty("字典编码")
    private String dictCode;
    @ApiModelProperty("字典排序")
    private Integer dictSort;
    @ApiModelProperty("字典标签")
    private String dictLabel;
    @ApiModelProperty("字典类型")
    private String dictValue;
    @ApiModelProperty("字典类型")
    private String dictType;
    @ApiModelProperty("字典类型名称")
    private String dictTypeName;
    @ApiModelProperty("是否默认（1是 0否）")
    private Integer isDefault;
    @ApiModelProperty(" 状态（0正常 1停用）")
    private Integer status;
    @ApiModelProperty("备注")
    private String remark;
}
