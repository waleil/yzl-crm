package cn.net.yzl.crm.service.impl.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.net.yzl.crm.dao.RequestMessageMapper;
import cn.net.yzl.crm.model.RequestMessage;
import cn.net.yzl.crm.service.order.IOrderCommonService;
import cn.net.yzl.crm.utils.BeanCopyUtils;
import cn.net.yzl.model.vo.OrderDistributeExpressVO;
import cn.net.yzl.model.vo.OrderProductVo;
import cn.net.yzl.order.model.db.order.OrderCouponDetail;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.vo.order.OrderDetailDTO;
import cn.net.yzl.order.model.vo.order.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单公共服务实现类
 * 
 * @author chengyu
 * @date 2021年1月31日,上午2:15:44
 */
@Service
@Slf4j
public class OrderCommonServiceImpl implements IOrderCommonService {
	@Resource
	private RequestMessageMapper requestMessageMapper;// 本地消息表mapper
	@Resource
	private ObjectMapper objectMapper;// json转换器
	@Value("${api.gateway.url}")
	private String apiGateWayUrl;// 应用网关地址

	@Override
	public <T> void insert(T object, String prefix, String suffix, String staffCode, String orderNo) {
		try {
			RequestMessage message = new RequestMessage();
			message.setMessageCode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));// 消息编码
			message.setBusCode(orderNo);// 订单号
			message.setRequestParam(this.objectMapper.writeValueAsString(object));// 请求参数
			message.setRequestUrl(String.format("%s%s%s", this.apiGateWayUrl, prefix, suffix));// 请求链接
			message.setCreateCode(staffCode);// 创建人
			message.setUpdateCode(staffCode);// 修改人
			// 插入本地消息记录表
			int row = this.requestMessageMapper.insert(message);
			log.info("插入{}条本地消息记录", row);
		} catch (Exception e) {
			log.error("ERROR", e);
		}
	}

	@Override
	public OrderDistributeExpressVO mkOrderDistributeExpressData(OrderInfoVo orderInfoVo) {

		OrderM order = orderInfoVo.getOrder();
		OrderDistributeExpressVO vo = new OrderDistributeExpressVO();
		vo.setOutStoreNo(orderInfoVo.getDetails().get(0).getStoreNo());
		vo.setOrderNo(order.getOrderNo());
		vo.setMediaType(String.valueOf(order.getMediaType()));
		vo.setMediaName(order.getMediaName());
		vo.setMemberName(order.getMemberName());
		vo.setMemberPhone(order.getMemberTelphoneNo());
		vo.setMemberNum(order.getMemberCardNo());
		vo.setPayType(order.getPayType());
		vo.setTotalAll(order.getTotalAll());
		vo.setCash(order.getCash());
		vo.setOrderTime(order.getCreateTime());

		vo.setTargetProvince(order.getReveiverProvinceName());
		vo.setReveiverProvince(order.getReveiverProvince());

		vo.setTargetCity(order.getReveiverCityName());
		vo.setReveiverCity(order.getReveiverCity());

		vo.setReveiverArea(order.getReveiverArea());
		vo.setTargetArea(order.getReveiverAreaName());

		vo.setAddr(order.getReveiverAddress());
		vo.setFinancialOwnerId(order.getFinancialOwner());
		vo.setFinancialOwner(order.getFinancialOwnerName());
		vo.setRemark(order.getRemark());
		vo.setStoreNo(vo.getOutStoreNo());
		vo.setStaffNo(order.getStaffCode());
		vo.setOrderOfficial(order.getTotal());
		vo.setOrderRebate(order.getReturnAmountCoupon());
		vo.setOrderIntegral(order.getPointsDeduction());
		vo.setOrderUseSpare(order.getAmountStored());
		vo.setOrderUseRed(order.getAmountRedEnvelope());
		vo.setOrderUseRebate(order.getAmountCoupon());
		vo.setOrderUseIntegral(order.getPointsDeduction());
		vo.setOrderAfterSpare(order.getOrderAfterSpare());
		vo.setOrderAfterRed(order.getOrderAfterRed());
		vo.setOrderAfterRebate(order.getOrderAfterRebate());
		vo.setOrderAfterIntegral(order.getOrderAfterIntegral());
		vo.setCreateUser(order.getStaffCode());
		List<OrderProductVo> list = mkOrderDetailList(orderInfoVo);

		vo.setOrderProductVoList(list);
		return vo;
	}

	private List<OrderProductVo> mkOrderDetailList(OrderInfoVo vo) {

		List<OrderDetail> orderDetails = vo.getDetails();
		List<OrderDetailDTO> list = BeanCopyUtils.transferList(orderDetails, OrderDetailDTO.class);
		List<OrderCouponDetail> couponDetails = vo.getCouponDetail();
		if (couponDetails != null && couponDetails.size() > 0) {
			Map<String, OrderCouponDetail> collect = couponDetails.stream()
					.collect(Collectors.toMap(OrderCouponDetail::getOrderDetailCode, Function.identity()));
			list.stream().forEach(map -> {
				OrderCouponDetail item = collect.get(map.getOrderDetailCode());
				if (item != null) {
					if (item.getCouponType() == 2) {
						map.setAmountRedEnvelope(item.getCouponAmt());
					} else if (item.getCouponType() == 1) {
						map.setAmountCoupon(item.getCouponAmt());
					} else if (item.getCouponType() == 4) {
						map.setPointsDeduction(item.getCouponAmt());
					}
				}
			});
		}

		return list.stream().map(map -> {
			OrderProductVo orderProductVo = new OrderProductVo();
			orderProductVo.setOutStoreNo(String.valueOf(map.getStoreNo()));// 仓库号
			orderProductVo.setOrderNo(map.getOrderNo());// 订单号
			orderProductVo.setProductCode(map.getProductCode());// 商品code
			orderProductVo.setProductName(map.getProductName());// 产品名称
			orderProductVo.setUnit(map.getUnit());// 产品规格
			orderProductVo.setNum(map.getProductCount());// 产品数量
			orderProductVo.setPrice(map.getTotal());// 产品金额
			orderProductVo.setUseRebate(map.getAmountCoupon());// 优惠券扣减
			orderProductVo.setUseSpareMoney(map.getPointsDeduction());// 积分扣减
			orderProductVo.setUseRedPacket(map.getAmountRedEnvelope());// 红包扣减
			orderProductVo.setCash(map.getCash());// 实收金额
			orderProductVo.setPackagingUnit(map.getPackageunit());
			orderProductVo.setProductNo(map.getProductNo());
			orderProductVo.setTotalUseNum(Integer.valueOf(map.getSpec()));

			return orderProductVo;
		}).collect(Collectors.toList());

	}
}
