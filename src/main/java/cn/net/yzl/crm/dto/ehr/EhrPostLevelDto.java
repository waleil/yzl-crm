package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("ehr岗位级别聚合实体")
@Getter
@Setter
public class EhrPostLevelDto {

    @ApiModelProperty("岗位id")
    private String postId;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("岗位级别集合")
    private List<EhrPostLevelListDto> postLevelList;
}
