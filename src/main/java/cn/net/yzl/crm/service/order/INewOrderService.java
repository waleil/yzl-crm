package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;

public interface INewOrderService extends IExcelService {
    /**
     * 新建订单
     * @param dto
     * @return
     */
    ComResponse<Boolean> newOrder(NewOrderDTO dto);
}
