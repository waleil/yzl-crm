package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderCheckDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderOprDTO;

public interface IOrderOprService {
    /**
     * 取消订单
     * @param dto
     * @return
     */
    ComResponse<Boolean> cancleOrder(OrderOprDTO dto);

    /**
     * 订单审核
     * @param dto
     * @return
     */
    ComResponse checkOrder(OrderCheckDetailDTO dto);
}
