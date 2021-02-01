package cn.net.yzl.crm.dto.ehr;

import cn.net.yzl.crm.dto.product.ProduceDto;
import lombok.Data;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/31 18:38
 * @desc: 本次营销目标
 **/
@Data
public class MarketTargetDto {

    private List<ProduceDto> productList;

    private List<ProduceDto> activityList;
}
