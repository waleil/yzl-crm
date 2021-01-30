package cn.net.yzl.crm.service.order;

import cn.net.yzl.model.vo.OrderDistributeExpressVO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;

/**
 * 订单公共服务
 * 
 * @author chengyu
 * @date 2021年1月31日,上午2:14:21
 */
public interface IOrderCommonService {

	/**
	 * 构建数据 （调用生成出库单输入参数数据）
	 */
	public OrderDistributeExpressVO mkOrderDistributeExpressData(OrderInfoVo orderInfoVo);

	/**
	 * 插入本地消息记录表
	 * 
	 * @param <T>       请求参数类型
	 * @param object    请求参数
	 * @param prefix    前缀
	 * @param suffix    后缀
	 * @param staffCode 员工编码
	 * @param orderNo   订单号
	 * @author zhangweiwei
	 * @date 2021年1月29日,下午9:08:22
	 */
	<T> void insert(T object, String prefix, String suffix, String staffCode, String orderNo);
}
