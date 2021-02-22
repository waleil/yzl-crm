package cn.net.yzl.crm.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.excel.util.CollectionUtils;
import com.mysql.cj.util.StringUtils;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.SnowFlakeUtil;
import cn.net.yzl.crm.client.order.NewOrderClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressMsgDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.member.MemberAddressAndLevelDTO;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.service.order.INewOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.RedisUtil;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.order.constant.CommonConstant;
import cn.net.yzl.order.enums.OrderLogisticsStatus;
import cn.net.yzl.order.enums.RedisKeys;
import cn.net.yzl.order.model.db.order.OrderDetail;
import cn.net.yzl.order.model.db.order.OrderM;
import cn.net.yzl.order.model.db.order.OrderTemp;
import cn.net.yzl.order.model.db.order.OrderTempProduct;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
import cn.net.yzl.order.model.vo.order.OrderInfo4Mq;
import cn.net.yzl.order.model.vo.order.OrderTempVO;
import cn.net.yzl.order.model.vo.order.Product4OrderDTO;
import cn.net.yzl.order.util.MathUtils;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.product.model.vo.product.vo.BatchProductVO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductReduceVO;

@Service
public class NewOrderServiceImpl implements INewOrderService {
	private static final Logger log = LoggerFactory.getLogger(NewOrderServiceImpl.class);
	// 每次从获取最大的客户数量
	private static final int MAX_SIZE = 100;

	@Value("${api.gateway.url}")
	private String path;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RestTemplate template;

	@Autowired
	private NewOrderClient newOrderClient;

	ThreadLocal<List<Map<String, Object>>> local = new ThreadLocal<>();
	@Autowired
	private ProductClient productClient;
	@Autowired
	private MemberGroupFeign memberGroupFeign;

	@Autowired
	private EhrStaffClient ehrStaffClient;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private MemberFien memberFien;

	/**
	 * 新建订单
	 */
	@Override
	public ComResponse<Boolean> newOrder(NewOrderDTO dto) {
		ComResponse orderRes = null;
		AtomicInteger totalCount = null;// 总人数
//		AtomicInteger successCnt = new AtomicInteger();// 成功人数
//		AtomicInteger failCnt = new AtomicInteger();// 失败人数
		try {
			local.set(new ArrayList<>());
			// 查询坐席时间
			ComResponse<StaffImageBaseInfoDto> response = ehrStaffClient.getDetailsByNo(dto.getUserNo());
			if (response.getCode().compareTo(Integer.valueOf(200)) != 0) {
				throw new BizException(response.getCode(), response.getMessage());
			}
			Integer wordCode = response.getData().getWorkCode();
			Integer departId = response.getData().getDepartId();
			dto.setUserName(response.getData().getName());
			Integer financialOwner = null;
			String financialOwnerName = "";
			// 生成批次号
			String batchNo = SnowFlakeUtil.getId() + "";

			// 根据部门编号获取财务归属
			ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(departId);
			// 如果存在该部门
			if (ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
				DepartDto depart = dresponse.getData();
				financialOwner = depart.getFinanceDepartId();
				financialOwnerName = depart.getFinanceDepartName();
			} else {
				throw new BizException(dresponse.getCode(), "查询财务归属失败>>" + dresponse.getMessage());
			}

			// 处理数据
			String productCodes = dto.getProducts().stream().map(Product4OrderDTO::getProductCode)
					.collect(Collectors.joining(","));

			List<OrderTempProduct> productDTOS = searchProducts(productCodes, dto.getProducts().size(), batchNo);
			Map<String, Product4OrderDTO> collect = dto.getProducts().stream()
					.collect(Collectors.toMap(Product4OrderDTO::getProductCode, product4OrderDTO -> product4OrderDTO));
			Map<String, OrderTempProduct> product4OrderDTOMap = productDTOS.stream()
					.collect(Collectors.toMap(OrderTempProduct::getProductCode, orderTempProduct -> orderTempProduct));

			// 组织商品明细
			productDTOS.stream().forEach(map -> {
				Product4OrderDTO product4OrderDTO = collect.get(map.getProductCode());
				map.setCount(product4OrderDTO.getCount());
			});

			// 计算总额 校验上送金额与计算商品总额是否一致
			int total = productDTOS.stream().mapToInt(m -> m.getCount() * m.getPrice()).sum();
			if (new BigDecimal(MathUtils.priceFenConvertYun(total)).compareTo(dto.getTotalAllAMT()) != 0) {
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品总金额计算不正确");
			}

			// 查询群组信息
			List<CrowdGroup> groups = searchGroups(dto.getCustomerGroupIds());
			// 组织群组信息
			OrderTemp orderTemp = mkOrderTemp(groups, batchNo, dto, String.valueOf(departId), wordCode, financialOwner,
					financialOwnerName);

			// 总人数
			totalCount = new AtomicInteger(groups.stream().mapToInt(CrowdGroup::getPerson_count).sum());

			BatchProductVO vo = new BatchProductVO();
			vo.setBatchNo(batchNo);
			List<ProductReduceVO> reduceVOS = new ArrayList<>();

			AtomicInteger finalTotalCount = totalCount;
			productDTOS.forEach(map -> {
				ProductReduceVO reduceVO = new ProductReduceVO();
				reduceVO.setProductCode(map.getProductCode());
				reduceVO.setNum(map.getCount() * finalTotalCount.intValue());
				reduceVOS.add(reduceVO);

			});
			// 校验库存
			reduceVOS.forEach(map -> {
				OrderTempProduct product = product4OrderDTOMap.get(map.getProductCode());
				if (map.getNum() > product.getStock()) {
					throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),
							"库存不足，商品名称：" + product.getProductName());
				}

			});
			vo.setProductReduceVOS(reduceVOS);
			// 调用扣减库存接口（根据批次号扣减库存）
			orderRes = productClient.productReduce(vo);

			if (orderRes.getCode().compareTo(Integer.valueOf(200)) != 0) {
				throw new BizException(orderRes.getCode(),"扣减库存失败" +  orderRes.getMessage());
			}
			OrderTempVO orderTempVO = new OrderTempVO();
			orderTempVO.setOrderTemp(orderTemp);
			orderTempVO.setProducts(productDTOS);

			// 调用新建订单接口
			orderRes = newOrderClient.newOrderTemp(orderTempVO);

			if (orderRes.getCode().compareTo(Integer.valueOf(200)) != 0) {
				throw new BizException(orderRes.getCode(),"创建批量生产记录失败" + orderRes.getMessage());
			}

		} catch (BizException e) {
			log.error(e.getMessage(), e);
			throw new BizException(e.getCode(), e.getMessage());
		}

		return ComResponse.success(true);
	}

	@Override
	public ComResponse<Boolean> sendHKOrderTask() {
		// 查询未处理的会刊订单记录
		ComResponse<List<OrderTempVO>> listComResponse = newOrderClient.selectUnOpredHKOrder();
		if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
			throw new BizException(listComResponse.getCode(), listComResponse.getMessage());
		}
		List<OrderTempVO> data = listComResponse.getData();
		if (data == null || data.size() == 0) {
			return ComResponse.success(true);
		}
		data.forEach(map -> {
			AtomicInteger successCnt = new AtomicInteger(0);
			AtomicInteger failCnt = new AtomicInteger(0);

			// 将预下单并发送到mq
			prepareAndSendMessage(map.getOrderTemp(), map.getProducts(), successCnt, failCnt);
			// 根据无效客户的数量，回退库存
			if (failCnt.intValue() > 0) {
				ComResponse<?> comResponse = increaseStore(failCnt, map);
				if (ResponseCodeEnums.SUCCESS_CODE.getCode().compareTo(comResponse.getCode()) != 0) {
					throw new BizException(comResponse.getCode(), comResponse.getMessage());
				}
			}

			map.getOrderTemp().setFailCount(failCnt.intValue());
			map.getOrderTemp().setSuccessCount(successCnt.intValue());
			map.getOrderTemp().setOprCount(map.getOrderTemp().getFailCount() + map.getOrderTemp().getSuccessCount());
			map.getOrderTemp().setOprStats(2);
			// 更新订单模板表
			ComResponse<Boolean> res = newOrderClient.updateResult(map.getOrderTemp());
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
				throw new BizException(res.getCode(), res.getMessage());
			}
		});

		return ComResponse.success(true);
	}

	/**
	 * 回退库存
	 * 
	 * @param orderTempVO
	 * @param failCnt
	 * @return
	 */
	private ComResponse<?> increaseStore(AtomicInteger failCnt, OrderTempVO orderTempVO) {
		List<ProductReduceVO> reduceVOS = new ArrayList<>();
		orderTempVO.getProducts().forEach(map -> {
			ProductReduceVO reduceVO = new ProductReduceVO();
			reduceVO.setProductCode(map.getProductCode());
			reduceVO.setNum(map.getCount() * failCnt.intValue());
			reduceVOS.add(reduceVO);

		});
		OrderProductVO vo = new OrderProductVO();
		vo.setOrderNo(orderTempVO.getOrderTemp().getOrderTempCode());
		vo.setProductReduceVOS(reduceVOS);
		vo.setUpdateNo(orderTempVO.getOrderTemp().getCreateCode());
		return productClient.increaseStock(vo);
	}

	/**
	 * 准备发送mq数据，并发送
	 * 
	 * @param orderTemp
	 * @param product
	 * @param successCnt
	 * @param failCnt
	 */
	private void prepareAndSendMessage(OrderTemp orderTemp, List<OrderTempProduct> product, AtomicInteger successCnt,
			AtomicInteger failCnt) {
		List<String> groupids = Arrays.asList(orderTemp.getGroupId().split(","));
		groupids.forEach(map -> {
			ComResponse<List<GroupRefMember>> listComResponse = memberGroupFeign.queryMembersByGroupId(map);
			if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
				// 标记查询失败的群组
				log.error("获取群组中顾客信息失败");
			} else {
				List<GroupRefMember> data = listComResponse.getData();
				// 分批次查询群组中的顾客
				int offset = 0;
				while (offset < data.size()) {

					int toIndex = offset + MAX_SIZE > data.size() ? data.size() : offset + MAX_SIZE;

					String membercards = data.subList(offset, toIndex).stream().map(GroupRefMember::getMemberCard)
							.collect(Collectors.joining(","));
					offset = toIndex ;
					ComResponse<List<MemberAddressAndLevelDTO>> res = memberFien
							.getMembereAddressAndLevelByMemberCards(membercards);
					if (res.getCode().compareTo(Integer.valueOf(200)) != 0) {
						throw new BizException(res.getCode(), res.getMessage());
					}
					List<MemberAddressAndLevelDTO> members = res.getData();

					members.stream().forEach(item -> {
						OrderInfo4Mq vo = mkOrderInfo(item, orderTemp, product, item);
						if (vo == null) {// 说明是无效客户，记录失败数量
							failCnt.incrementAndGet();
						} else {
							sendMessage(vo);
							successCnt.incrementAndGet();
						}

					});

				}

				data.clear();
			}

		});

	}

	/**
	 * 组织订单数据
	 * 
	 * @param item
	 * @param orderTemp
	 * @param product
	 * @return
	 */
	private OrderInfo4Mq mkOrderInfo(MemberAddressAndLevelDTO item, OrderTemp orderTemp, List<OrderTempProduct> product,
			MemberAddressAndLevelDTO member) {

		// 订单号
		String orderNo = redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6);
		ReveiverAddressMsgDTO addressMsgDTO = checkMember(member);
		if (addressMsgDTO == null) {
			return null;
		}

		OrderInfo4Mq orderInfo = new OrderInfo4Mq();
		// 订单主表
		OrderM orderM = mkOrderM(orderNo, orderTemp, product, member, addressMsgDTO);
		orderInfo.setOrderM(orderM);
		// 订单详情表
		List<OrderDetail> orderDetails = this.selectAndMkDetail(orderNo, orderM, product, member);
		orderInfo.setDetailList(orderDetails);
		return orderInfo;

	}

	/**
	 * 校验是否为有效客户 联系电话 收货地址 不存在 则返回nul 校验失败
	 * 
	 * @param member
	 * @return
	 */
	private ReveiverAddressMsgDTO checkMember(MemberAddressAndLevelDTO member) {
		ReveiverAddressMsgDTO result = null;
		if (CollectionUtils.isEmpty(member.getPhoneNumbers())) {
			return result;
		}
		result = member.getReveiverInformations().stream()
				.filter(p -> this.check(p)).findFirst().orElse(null);

		return result;
	}

	private boolean check(ReveiverAddressMsgDTO p) {
		if(StringUtils.isNullOrEmpty(p.getDetailedReceivingAddress()) ||
				StringUtils.isNullOrEmpty(p.getCityName()) ||
				p.getCityNo()==null ||
				StringUtils.isNullOrEmpty(p.getProvinceName())||
				p.getProvinceNo() == null ||
				StringUtils.isNullOrEmpty(p.getCountyName())||
				p.getCountyNo()== null  ){
			return false;
		}
		return true;

	}

	/**
	 * 订单主表
	 * 
	 * @param orderNo
	 * @param orderTemp
	 * @param products
	 * @param member
	 * @param addressMsgDTO
	 * @return
	 */
	private OrderM mkOrderM(String orderNo, OrderTemp orderTemp, List<OrderTempProduct> products,
			MemberAddressAndLevelDTO member, ReveiverAddressMsgDTO addressMsgDTO) {
		OrderM orderM = new OrderM();
		orderM.setOrderNo(orderNo);
		orderM.setDepartId(orderTemp.getDepartId());
		orderM.setWorkOrderNo(-1);// 会刊订单 没有工单号，值设置为-1
		orderM.setWorkBatchNo("-1");// 工单流水号 默认-1
		orderM.setMediaType(-1);
		orderM.setMediaNo(-1);
		orderM.setMediaName(null);
		orderM.setMediaChannel(-1);
		orderM.setActivityNo(-1);
		orderM.setActivityName(null);
		orderM.setAdvisorNo(-1);
		orderM.setAdvisorName(null);

		orderM.setMemberName(member.getMemberName());
		orderM.setMemberTelphoneNo(member.getPhoneNumbers().stream().collect(Collectors.joining(",")));
		orderM.setMemberCardNo(member.getMemberCard());

		orderM.setTotal(orderTemp.getTotalAllAmt());// 实收
		orderM.setTotalAll(orderTemp.getTotalAllAmt());// 订单总额
		orderM.setPfee(0);
		orderM.setPfeeFlag(0);
		orderM.setCash(orderTemp.getTotalAllAmt());// 应收
		orderM.setCash1(0);// 预存
		orderM.setSpend(orderTemp.getTotalAmt());// 消费金额

		orderM.setLogisticsStatus(OrderLogisticsStatus.ORDER_LOGIST_STATUS_0.getCode());
		orderM.setFirstOrderFlag(0);

		orderM.setAmountStored(0);
		orderM.setAmountRedEnvelope(0);
		orderM.setAmountCoupon(0);
		orderM.setPointsDeduction(0);
		orderM.setReturnAmountRedEnvelope(0);
		orderM.setReturnAmountCoupon(0);
		orderM.setReturnPointsDeduction(0);

		orderM.setInvoiceFlag(0);
		orderM.setPayType(CommonConstant.PAY_TYPE_0);// 支付方式 货到付款
		orderM.setPayMode(CommonConstant.PAY_MODE_K);// 快递代收
		orderM.setPayStatus(null);// 未收款
		orderM.setDistrubutionMode(CommonConstant.DISTRUBUTION_MODE_KD);// 配送方式 快递

		orderM.setExpressCompanyFlag(0);
		orderM.setExpressCompanyCode(null);
		orderM.setExpressCompanyName(null);
		orderM.setExpressNumber(null);
		orderM.setRelationOrder(orderTemp.getRelationOrder());
		// todo 地址唯一标识
		orderM.setReveiverAddressNo(addressMsgDTO.getId());
		orderM.setReveiverProvince(String.valueOf(addressMsgDTO.getProvinceNo()));
		orderM.setReveiverProvinceName(addressMsgDTO.getProvinceName());
		orderM.setReveiverCity(String.valueOf(addressMsgDTO.getCityNo()));
		orderM.setReveiverCityName(addressMsgDTO.getCityName());
		orderM.setReveiverArea(String.valueOf(addressMsgDTO.getCountyNo()));
		orderM.setReveiverAreaName(addressMsgDTO.getCountyName());
		orderM.setReveiverName(addressMsgDTO.getConsignee());
		orderM.setReveiverTelphoneNo(addressMsgDTO.getRecieverPhone());
		orderM.setReveiverAddress(addressMsgDTO.getDetailedReceivingAddress());
		orderM.setRemark(orderTemp.getRemark());

		orderM.setStaffCode(orderTemp.getCreateCode());
		orderM.setStaffName(orderTemp.getCreateName());
		orderM.setUpdateCode(orderTemp.getCreateCode());
		orderM.setUpdateName(orderTemp.getCreateName());

		orderM.setShippingTime(null);
		orderM.setEstimateArrivalTime(null);
		orderM.setSignTime(null);

		orderM.setMemberLevelBefor(member.getGradeCode());
		orderM.setMemberTypeBefor(member.getMemberType());
		orderM.setMemberLevelAfter(null);
		orderM.setMemberTypeAfter(null);

		orderM.setFinancialOwner(orderTemp.getFinancialOwner());
		orderM.setFinancialOwnerName(orderTemp.getFinancialOwnerName());
		orderM.setRelationReissueOrderNo(CommonConstant.RELATION_REISSUE_ORDER_NO);
		orderM.setLogisticsClaims(null);
		orderM.setRelationReissueOrderTotal(null);
		// todo 等dmc数据
		orderM.setOrderAfterIntegral(0);
		orderM.setOrderAfterRebate(0);
		orderM.setOrderAfterRed(0);
		orderM.setOrderAfterSpare(MathUtils.strPriceToLong(String.valueOf(member.getTotalMoney())));// 单后余额

		orderM.setCreateTime(new Date());
		orderM.setUpdateTime(new Date());
		orderM.setOrderChanal(CommonConstant.ORDER_CHANAL_2);
		orderM.setIsHistory(CommonConstant.IS_HISTORY_0);

		return orderM;
	}

	/**
	 * 组织订单明细表表数据
	 * 
	 * @param orderNo
	 * @param order
	 * @param products
	 * @param member
	 * @return
	 */
	private List<OrderDetail> selectAndMkDetail(String orderNo, OrderM order, List<OrderTempProduct> products,
			MemberAddressAndLevelDTO member) {
		AtomicInteger no = new AtomicInteger(10);
		List<OrderDetail> result = new ArrayList<>();

		products.forEach(map -> {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderDetailCode(orderNo + no.incrementAndGet());
			orderDetail.setOrderNo(orderNo);
			orderDetail.setProductCode(map.getProductCode());
			orderDetail.setProductBarCode(map.getProductBarCode());
			orderDetail.setProductName(map.getProductName());
			orderDetail.setProductCount(map.getCount());
			orderDetail.setSpec(map.getSpec());
			orderDetail.setUnit(map.getUnit());
			orderDetail.setPackageunit(map.getPackageunit());
			orderDetail.setProductUnitPrice(map.getPrice());
			orderDetail.setMealFlag(0);
			orderDetail.setMealNo(null);
			orderDetail.setMealName(null);
			orderDetail.setMealCount(null);
			orderDetail.setMealPrice(null);
			orderDetail.setTotal(map.getCount() * Integer.valueOf(map.getPrice()));
			orderDetail.setCash(map.getCount() * Integer.valueOf(map.getPrice()));
			orderDetail.setGiftFlag(0);
			orderDetail.setReturnGoodsCnt(0);
			orderDetail.setDetailStatus(0);
			orderDetail.setSignTime(null);
			orderDetail.setExpressNo(null);
			orderDetail.setEstimateArrivalTime(null);
			orderDetail.setExpressCompanyCode(null);
			orderDetail.setLogisticsStatus(0);
			orderDetail.setDepartId("0");
			orderDetail.setMemberName(member.getMemberName());
			orderDetail.setMemberCardNo(member.getMemberCard());
			orderDetail.setReturnGoodsCnt(0);

			orderDetail.setShippingTime(null);
			orderDetail.setCreateTime(null);
			orderDetail.setStaffCode(order.getStaffCode());
			orderDetail.setUpdateCode(order.getStaffName());
			orderDetail.setPackageunit(map.getPackageunit());
			orderDetail.setProductNo(map.getProductNo());
			orderDetail.setCreateTime(new Date());
			orderDetail.setUpdateTime(new Date());
			orderDetail.setIsHistory(String.valueOf(CommonConstant.IS_HISTORY_0));
			result.add(orderDetail);

		});

		return result;
	}

	private void sendMessage(OrderInfo4Mq infoVO) {

		try {
			this.rabbitTemplate.convertAndSend(CommonConstant.NEW_ORDER_EXCHANGE_NAME,
					CommonConstant.NEW_ORDER_ROOT_KEY, infoVO);

			System.err.println(infoVO);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// todo 将

		}

	}

	/**
	 * 执行失败回调方法
	 * 
	 * @param list
	 */
	private void revert(List<Map<String, Object>> list) {
		if (list != null && list.size() > 0) {
			list.forEach(map -> {
				sentHttpRequest(map.get("url").toString(), map.get("parm"));
			});
		}

	}

	/**
	 * 发送http请求，用于回调
	 */
	protected void sentHttpRequest(String url, Object param) {
		template.postForObject(url, param, ComResponse.class);
	}

	/**
	 * 查询群组信息
	 *
	 * @param groupCodes
	 * @return
	 */
	protected List<CrowdGroup> searchGroups(List<String> groupCodes) {
		String groupsStr = groupCodes.stream().collect(Collectors.joining(","));
		// 查询顾客群组接口
		ComResponse<List<CrowdGroup>> response = memberGroupFeign.getCrowdGroupList(groupsStr);
		if (response.getCode().compareTo(Integer.valueOf(200)) != 0) {
			throw new BizException(response.getCode(), "查询群组信息失败>>" + response.getMessage());
		}
		if (response.getData() != null && (groupCodes.size() != response.getData().size())) {
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "部分群组已失效");
		}
		response.getData().forEach(map ->{
			if(map.getEnable() == 0){
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),
						"群组：" + map.get_id() + " 群组名称： " + map.getName() +",已失效" );
			}
		});

		return response.getData();
	}

	private OrderTemp mkOrderTemp(List<CrowdGroup> groups, String batchNo, NewOrderDTO dto, String departId,
			Integer wordCode, Integer financialOwner, String financialOwnerName) {
//		List<OrderTemp> list = new ArrayList<>();

		groups.forEach(map -> {
			if (map.getPerson_count() == null) {
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "无效群组，群组号：" + map.get_id());
			}
		});

		String groupIds = groups.stream().map(CrowdGroup::get_id).collect(Collectors.joining(","));
		int total = groups.stream().mapToInt(CrowdGroup::getPerson_count).sum();
		OrderTemp orderTemp = new OrderTemp();
		orderTemp.setOrderTempCode(batchNo);
		orderTemp.setGroupId(groupIds);
		orderTemp.setGroupCount(total);
		orderTemp.setOprCount(0);
		orderTemp.setSuccessCount(0);
		orderTemp.setFailCount(0);
		orderTemp.setTotalAmt(MathUtils.strPriceToLong(dto.getTotalAMT().toString()));
		orderTemp.setTotalAllAmt(MathUtils.strPriceToLong(dto.getTotalAllAMT().toString()));
		orderTemp.setRemark(dto.getRemark());
		orderTemp.setRelationOrder(dto.getRelationOrder());
		orderTemp.setExpressFlag(dto.getAssignExpressFlag());
		orderTemp.setExpressCode(dto.getExpressCode());
		orderTemp.setOprStats(0);
		orderTemp.setCreateCode(dto.getUserNo());
		orderTemp.setCreateName(dto.getUserName());
		orderTemp.setUpdateCode(dto.getUserNo());
		orderTemp.setUpdateName(dto.getUserName());
		orderTemp.setDepartId(departId);
		orderTemp.setWorkCode(wordCode.toString());
		orderTemp.setFinancialOwner(financialOwner);
		orderTemp.setFinancialOwnerName(financialOwnerName);

		return orderTemp;
	}

	/**
	 * 查询商品列表
	 * 
	 * @param productCodes
	 * @param
	 * @return
	 */
	protected List<OrderTempProduct> searchProducts(String productCodes, int prodCnt, String tempCode) {
		ComResponse<List<ProductMainDTO>> prd = productClient.queryByProductCodes(productCodes);
		if (prd.getCode().compareTo(200) != 0) {
			throw new BizException(prd.getCode(), "查询商品信息失败>>" + prd.getMessage());
		}
		if (prd.getData() != null && (prodCnt != prd.getData().size())) {
			throw new BizException(ResponseCodeEnums.REPEAT_ERROR_CODE.getCode(), "部分商品已下架");
		}

		List<OrderTempProduct> list = prd.getData().stream().map(m -> {
			if(CommonConstant.PRODUCT_AND_MEAL_STATUS_0 == m.getStatus()){
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),
						m.getName()+",商品已下架");
			}
			OrderTempProduct product = new OrderTempProduct();
			product.setOrderTempProductCode(SnowFlakeUtil.getId() + "");
			product.setOrderTempCode(tempCode);
			product.setProductCode(m.getProductCode());
			product.setProductBarCode(m.getBarCode());
			product.setProductName(m.getName());
			product.setPrice(MathUtils.strPriceToLong(m.getSalePrice()));
			product.setSpec(m.getTotalUseNum() + "");
			product.setUnit(m.getUnit());
			product.setStock(m.getStock());
			product.setPackageunit(m.getPackagingUnit());
			product.setProductNo(m.getProductNo());
			return product;
		}).collect(Collectors.toList());

		return list;
	}

	@Override
	public void oprData(List list) {

	}

}
