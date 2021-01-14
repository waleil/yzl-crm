package cn.net.yzl.crm.dto.ehr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * post
 * @author 
 */
@Data
public class PostDto implements Serializable {

    @ApiModelProperty("岗位编号")
    private Integer id;

    @ApiModelProperty("岗位名称")
    private String name;

    @ApiModelProperty("排序序号")
    private Integer sortNo;



    private static final long serialVersionUID = 1L;


}