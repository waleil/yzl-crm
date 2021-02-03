package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderAccountConfirmVO;

public interface OrderAccountConfirmService {

    /**
     * 添加收款记录
     * @param vo
     * @return
     */
    ComResponse<Boolean> saveOrderAccountConfirm(OrderAccountConfirmVO vo);
}
