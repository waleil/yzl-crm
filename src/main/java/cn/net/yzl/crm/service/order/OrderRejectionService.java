package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.order.OrderRejectionAddDTO;

public interface OrderRejectionService {
    /**
     * 变更
     * @param orderRejectionAddDTO
     * @param userNo
     * @return
     */
    ComResponse addOrderRejection(OrderRejectionAddDTO orderRejectionAddDTO, String userNo);
}
