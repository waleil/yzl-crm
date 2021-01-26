package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;

public interface IOrderOprService {
    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    ComResponse<Boolean> cancleOrder(String orderNo);
}
