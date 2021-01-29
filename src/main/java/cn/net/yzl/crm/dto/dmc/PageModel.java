package cn.net.yzl.crm.dto.dmc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: liuChangFu
 * @date: 2020/12/23 14:38
 * @desc: 分页参数
 **/
@Data
public  class PageModel {

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量", name = "pageSize", example = "10")
    private int pageSize = 10;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", name = "pageNo", example = "1")
    private int pageNo = 1;
}
