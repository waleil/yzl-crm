package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderTrackDetailDTO;

public interface IOrderTranckDetailService {
    /**
     * 添加跟踪记录
     * @param dto
     * @return
     */
    ComResponse<Boolean> saveOrderTranckDetail(OrderTrackDetailDTO dto);
}
