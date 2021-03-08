package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.order.OrderInfoVO;
import cn.net.yzl.crm.model.order.OrderLogistcInfo;

public interface IOrderSearchService {
	/**
	 * 查询订单基本信息 包含订单基础信息和购买人信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public ComResponse<OrderInfoVO> selectOrderInfo(String orderNo);

	/**
	 *
	 *
	 * @param orderNo
	 * @param companyCode
	 * @param dto
	 * @return
	 */
	ComResponse<OrderLogistcInfo> selectLogisticInfo(String orderNo, String companyCode, String dto);
}
