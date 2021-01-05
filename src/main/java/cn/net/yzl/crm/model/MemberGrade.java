package cn.net.yzl.crm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="会员级别实体类",description="会员级别实体类" )
@Data
public class MemberGrade {
    private int id;

    @ApiModelProperty("级别编号")
    private String no;

    @ApiModelProperty("级别名称")
    private String name;
}

