package cn.net.yzl.crm.service.order;

import cn.net.yzl.model.vo.OrderDistributeExpressVO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;

public interface IOrderCommonService {

    /**
     * 构建数据 （调用生成出库单输入参数数据）
     */
    public OrderDistributeExpressVO mkOrderDistributeExpressData(OrderInfoVo orderInfoVo);

}
