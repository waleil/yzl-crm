package cn.net.yzl.crm.dto.dmc;

import lombok.Data;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/31 18:38
 * @desc: 任务
 **/
@Data
public class TaskDto {

    private List<RelationProductDto> productList;

    private List<ProductSalesProductResponse> activityList;
}
