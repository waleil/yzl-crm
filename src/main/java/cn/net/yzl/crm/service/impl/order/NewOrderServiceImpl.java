package cn.net.yzl.crm.service.impl.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.SnowFlakeUtil;
import cn.net.yzl.crm.client.order.NewOrderClient;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressMsgDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.member.MemberAddressAndLevelDTO;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.dto.order.MemberCouponInfoDTO;
import cn.net.yzl.crm.dto.staff.StaffChangeRecordDto;
import cn.net.yzl.crm.service.micservice.ActivityClient;
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
import com.alibaba.excel.util.CollectionUtils;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NewOrderServiceImpl implements INewOrderService {
	private static final Logger log = LoggerFactory.getLogger(NewOrderServiceImpl.class);
	// 每次从获取最大的客户数量
	private static final int MAX_SIZE = 100;

	//新建缓存前缀
	private static final String CACHEKEYPREFIX = "crm-newOrder:" ;

	//新建缓存前缀
	private static final long CACHEKEY_TIMEOUT = 4*60*60*1000 ;


	@Value("${api.gateway.url}")
	private String path;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RestTemplate template;

	@Autowired
	private NewOrderClient newOrderClient;


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

	@Autowired
	private OrderSearchClient orderSearchClient;

	@Autowired
	private ActivityClient activityClient;

	/**
	 * 新建订单
	 */
	@Override
	public ComResponse<Boolean> newOrder(NewOrderDTO dto) {
		ComResponse orderRes = null;
		AtomicInteger totalCount = null;// 总人数

		try {

			// 查询坐席
			ComResponse<StaffChangeRecordDto> response = ehrStaffClient.getStaffLastChangeRecord(dto.getUserNo());
			if (response.getCode().compareTo(Integer.valueOf(200)) != 0) {
				log.error("查询坐席详情失败>>"  ,response.getCode()+"," +response.getMessage());
				throw new BizException(response.getCode(), response.getMessage());
			}
			if(response.getCode() == null){
				log.error("查询坐席详细信息失败，坐席号：" + dto.getUserNo());
				throw new BizException(response.getCode(),
						"查询坐席详细信息失败，坐席号：" + dto.getUserNo()+response.getMessage());
			}

			Integer wordCode = response.getData().getWorkCode();
			Integer departId = response.getData().getDepartId();
			dto.setUserName(response.getData().getName());
			String workCodeStr = response.getData().getWorkName();
			Integer financialOwner = null;
			String financialOwnerName = "";
			String personChangId = String.valueOf(response.getData().getId());
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
				log.error("查询财务归属失败>>"  ,response.getCode()+"," +response.getMessage());
				throw new BizException(dresponse.getCode(), "查询财务归属失败>>" + dresponse.getMessage());
			}
			log.info("查询坐席详细信息：" + dresponse.getData());
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
				log.error("商品总金额计算不正确,价格应为："  + MathUtils.priceFenConvertYun(total));
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "商品总金额计算不正确");
			}

			// 查询群组信息
			List<CrowdGroup> groups = searchGroups(dto.getCustomerGroupIds());
			// 组织群组信息
			OrderTemp orderTemp = mkOrderTemp(groups, batchNo, dto, String.valueOf(departId), wordCode, financialOwner,
					financialOwnerName,workCodeStr,personChangId);

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
				 if (product.getStock() != -1 && map.getNum() > product.getStock()) {
					throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),
							"库存不足，商品名称：" + product.getProductName());
				}

			});
			vo.setProductReduceVOS(reduceVOS);
			// 调用扣减库存接口（根据批次号扣减库存）
			orderRes = productClient.productReduce(vo);

			if (orderRes.getCode().compareTo(Integer.valueOf(200)) != 0) {
				log.error("扣减库存失败:" + vo);
				throw new BizException(orderRes.getCode(),"扣减库存失败" +  orderRes.getMessage());
			}
			OrderTempVO orderTempVO = new OrderTempVO();
			orderTempVO.setOrderTemp(orderTemp);
			orderTempVO.setProducts(productDTOS);


			redisUtil.set(CACHEKEYPREFIX + "fail" +"-"+ orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
			redisUtil.set(CACHEKEYPREFIX + "opr" +"-"+orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
			redisUtil.set(CACHEKEYPREFIX +  "suc" +"-"+ orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
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
	  boolean continueFlag = true;
       do{

		   // 查询未处理的会刊订单记录
		   ComResponse<List<OrderTempVO>> listComResponse = newOrderClient.selectUnOpredHKOrder();
		   if (listComResponse == null ||!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
			  log.error("查询待执行的会刊任务失败！ {}-{}",listComResponse.getCode(),listComResponse.getMessage());
			   throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "调用订单服务查询待执行会刊任务失败");
		   }
		   List<OrderTempVO> data = listComResponse.getData();
		   if (data == null || data.size() == 0) {
			   continueFlag = false;
			   break;
		   }
		   OrderTempVO map = data.get(0);

		   // 将预下单并发送到mq
		   prepareAndSendMessage(map.getOrderTemp(), map.getProducts());


		   // 根据无效客户的数量，回退库存
		   int failCnt = (Integer)redisUtil.get(CACHEKEYPREFIX + "fail" +"-" + map.getOrderTemp().getOrderTempCode());
		   int oprCnt = (Integer)redisUtil.get(CACHEKEYPREFIX + "opr"  +"-" + map.getOrderTemp().getOrderTempCode());
		   int sucCnt = (Integer)redisUtil.get(CACHEKEYPREFIX +  "suc"   +"-" + map.getOrderTemp().getOrderTempCode());
		   if (failCnt > 0) {
			   ComResponse<?> comResponse = increaseStore(failCnt, map);
			   if (ResponseCodeEnums.SUCCESS_CODE.getCode().compareTo(comResponse.getCode()) != 0) {
				   throw new BizException(comResponse.getCode(), comResponse.getMessage());
			   }
		   }

		   map.getOrderTemp().setFailCount(failCnt);
		   map.getOrderTemp().setSuccessCount(sucCnt);
		   map.getOrderTemp().setOprCount(oprCnt);

		   // 更新订单模板表
		   ComResponse<Boolean> res = newOrderClient.updateResult(map.getOrderTemp());
		   if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
			   throw new BizException(res.getCode(), res.getMessage());
		   }



	   }while(continueFlag);



		return ComResponse.success(true);
	}

	/**
	 * 回退库存
	 * 
	 * @param orderTempVO
	 * @param failCnt
	 * @return
	 */
	private ComResponse<?> increaseStore(int failCnt, OrderTempVO orderTempVO) {
		List<ProductReduceVO> reduceVOS = new ArrayList<>();
		orderTempVO.getProducts().forEach(map -> {
			ProductReduceVO reduceVO = new ProductReduceVO();
			reduceVO.setProductCode(map.getProductCode());
			reduceVO.setNum(map.getCount() * failCnt);
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

	 */
	private void prepareAndSendMessage(OrderTemp orderTemp, List<OrderTempProduct> product) {
		List<String> groupids = Arrays.asList(orderTemp.getGroupId().split(","));
		for (String map : groupids) {
			ComResponse<List<GroupRefMember>> listComResponse = memberGroupFeign.queryMembersByGroupId(map);
			if (listComResponse==null ||!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
				// 标记查询失败的群组
				log.error("获取群组中顾客信息失败！{}-{}" , listComResponse.getCode() ,listComResponse.getMessage());
				throw new BizException(listComResponse.getCode(),"获取群组信息失败!" );
			} else {
				List<GroupRefMember> data = listComResponse.getData();
				if (CollectionUtils.isEmpty(data)) {
					log.info("创建会刊订单失败，原因为：根据群组id未查询到人员,群组号：" + map);
					orderTemp.setOprStats(3);//异常
					continue;
				}
				// 分批次查询群组中的顾客
				//查询群组已经处理的顾客数
				Object o = redisUtil.get(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode() + "-" + map);

				int offset = o == null ? 0 : (Integer) o;
				while (offset < data.size()) {

					int toIndex = offset + MAX_SIZE > data.size() ? data.size() : offset + MAX_SIZE;

					String membercards = data.subList(offset, toIndex).stream().map(GroupRefMember::getMemberCard)
							.collect(Collectors.joining(","));

					ComResponse<List<MemberAddressAndLevelDTO>> res = memberFien
							.getMembereAddressAndLevelByMemberCards(membercards);
					if (res.getCode().compareTo(Integer.valueOf(200)) != 0) {
						throw new BizException(res.getCode(), res.getMessage());
					}
					List<MemberAddressAndLevelDTO> members = res.getData();
					//查询顾客当前积分优惠券情况
					ComResponse<List<MemberCouponInfoDTO>> accountByMemberCardsRes = activityClient.getAccountByMemberCards(data.subList(offset, toIndex).stream()
							.map(GroupRefMember::getMemberCard).collect(Collectors.toList()));
					if(accountByMemberCardsRes == null || Integer.compare(accountByMemberCardsRes.getCode(),Integer.valueOf(200))!=0){
						log.error("调用dmc查询顾客优惠信息失败",accountByMemberCardsRes);
						throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),"调用dmc查询顾客优惠信息失败");
					}
					List<MemberCouponInfoDTO> memberCouponInfoDTOS = accountByMemberCardsRes.getData();
					Map<String, MemberCouponInfoDTO> collect = memberCouponInfoDTOS.stream().collect(Collectors.toMap(MemberCouponInfoDTO::getMemberCard, memberCouponInfoDTO -> memberCouponInfoDTO));
					offset = toIndex;
					members.stream().forEach(item -> {
						MemberCouponInfoDTO memberCouponInfoDTO = collect.get(item.getMemberCard());
						OrderInfo4Mq vo = mkOrderInfo(item, orderTemp, product, item,memberCouponInfoDTO);
						redisUtil.incr(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode() + "-" + map);
						redisUtil.incr(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode());
						if (vo == null) {// 说明是无效客户，记录失败数量
							redisUtil.incr(CACHEKEYPREFIX + "fail" + "-" +  orderTemp.getOrderTempCode());
//							failCnt.incrementAndGet();
						} else {
							sendMessage(vo);
							log.info("新建会刊订单，批次号：{},订单号：{}",orderTemp.getOrderTempCode(),vo.getOrderM().getOrderNo());
							redisUtil.incr(CACHEKEYPREFIX + "suc" + "-" +  orderTemp.getOrderTempCode());
//							successCnt.incrementAndGet();
						}

					});

				}

				data.clear();
			}

		}

		int totalopr = (Integer) redisUtil.get(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode());
		log.info("新建会刊订单批次号：" + orderTemp.getOrderTempCode() +"共新建 " + totalopr +" 个订单");
		if(totalopr <orderTemp.getGroupCount()){
			orderTemp.setOprStats(3);
		}else{
			orderTemp.setOprStats(2);
		}
	}

	/**
	 * 组织订单数据
	 * 
	 * @param item
	 * @param orderTemp
	 * @param product
	 * @param memberCouponInfoDTO
	 * @return
	 */
	private OrderInfo4Mq mkOrderInfo(MemberAddressAndLevelDTO item, OrderTemp orderTemp, List<OrderTempProduct> product,
									 MemberAddressAndLevelDTO member, MemberCouponInfoDTO memberCouponInfoDTO) {

		// 订单号
		String orderNo = redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6);
		ReveiverAddressMsgDTO addressMsgDTO = checkMember(member);
		if (addressMsgDTO == null) {
			log.info("新建会刊订单，因客户联系地址不全，无法创建，顾客编号{}",member.getMemberCard());
			return null;
		}

		OrderInfo4Mq orderInfo = new OrderInfo4Mq();
		// 订单主表
		OrderM orderM = mkOrderM(orderNo, orderTemp, product, member, addressMsgDTO,memberCouponInfoDTO);
		if(orderM != null){
			orderInfo.setOrderM(orderM);
			// 订单详情表
			List<OrderDetail> orderDetails = this.selectAndMkDetail(orderNo, orderM, product, member);
			if(orderDetails == null){
				return null;
			}
			orderInfo.setDetailList(orderDetails);
		}


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
	 * @param memberCouponInfoDTO
	 * @return
	 */
	private OrderM mkOrderM(String orderNo, OrderTemp orderTemp, List<OrderTempProduct> products,
							MemberAddressAndLevelDTO member, ReveiverAddressMsgDTO addressMsgDTO, MemberCouponInfoDTO memberCouponInfoDTO) {
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
        if(!StringUtils.isNullOrEmpty(orderTemp.getExpressCode())){
			orderM.setExpressCompanyFlag(1);
			orderM.setExpressCompanyCode(orderTemp.getExpressCode());
			orderM.setExpressCompanyName(orderTemp.getExpressName());
		}

		orderM.setRelationOrder(orderTemp.getRelationOrder());
		//  地址唯一标识
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

		orderM.setMemberLevelBefor(member.getGradeId());
		orderM.setMemberTypeBefor(member.getMemberType());
		orderM.setMemberLevelAfter(member.getGradeId());
		orderM.setMemberTypeAfter(member.getMemberType());
		orderM.setOrderAfterSpare(MathUtils.strPriceToLong(String.valueOf(member.getTotalMoney())));// 单后余额

		orderM.setFinancialOwner(orderTemp.getFinancialOwner());
		orderM.setFinancialOwnerName(orderTemp.getFinancialOwnerName());
		orderM.setRelationReissueOrderNo(CommonConstant.RELATION_REISSUE_ORDER_NO);
		orderM.setLogisticsClaims(null);
		orderM.setRelationReissueOrderTotal(null);
        //单后优惠信息
		orderM.setOrderAfterIntegral(memberCouponInfoDTO.getMemberIntegral()==null?0:memberCouponInfoDTO.getMemberIntegral());//单后积分
		orderM.setOrderAfterRebate(memberCouponInfoDTO.getMemberCouponSize()==null?0:memberCouponInfoDTO.getMemberRedBag());//单后优惠券
		orderM.setOrderAfterRed(memberCouponInfoDTO.getMemberRedBag()==null?0:memberCouponInfoDTO.getMemberRedBag());//单后红包
		orderM.setOrderAfterSpare(MathUtils.strPriceToLong(String.valueOf(member.getTotalMoney())));// 单后余额

		orderM.setCreateTime(new Date());
		orderM.setUpdateTime(new Date());
		orderM.setOrderChanal(CommonConstant.ORDER_CHANAL_2);
		orderM.setIsHistory(CommonConstant.IS_HISTORY_0);
		orderM.setRemark(orderTemp.getRemark());
		orderM.setWorkCode(orderTemp.getWorkCode());
		orderM.setWorkCodeStr(orderTemp.getWorkCodeStr());
		orderM.setPersonChangeId(orderTemp.getPersonChangeId());




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

		//根据顾客的省份，获取发货仓库
		ComResponse<String> storeRes = orderSearchClient.selectSplitStore(Integer.valueOf(order.getReveiverProvince()));
		if(storeRes == null ||Integer.compare(storeRes.getCode(),Integer.valueOf(200))!=0){
			log.error("新建订单，调用订单服务查询发货仓失败");
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode()
					,"新建订单，调用订单服务查询发货仓失败!");
		}
		if(StringUtils.isNullOrEmpty(storeRes.getData())){
			log.info("新建订单，为顾客卡号：{} 创建订单失败，原因找不到发货仓",member.getMemberCard());
			return Collections.emptyList();
		}
		products.forEach(map -> {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderDetailCode(orderNo + no.incrementAndGet());
			orderDetail.setOrderNo(orderNo);
			orderDetail.setStoreNo(storeRes.getData());
			orderDetail.setProductCode(map.getProductCode());
			orderDetail.setProductBarCode(map.getProductBarCode());
			orderDetail.setProductName(map.getProductName());
			orderDetail.setProductCount(map.getCount());
			orderDetail.setSpec(map.getSpec());
			orderDetail.setUnit(map.getUnit());
			orderDetail.setPackageUnit(map.getPackageunit());
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
			orderDetail.setPackageUnit(map.getPackageunit());
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
						"群组已失效! 群组名称： " + map.getCrowd_name());
			}
		});

		return response.getData();
	}

	private OrderTemp mkOrderTemp(List<CrowdGroup> groups, String batchNo, NewOrderDTO dto, String departId,
								  Integer wordCode, Integer financialOwner, String financialOwnerName, String workCodeStr
								,String personChangId) {
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
		orderTemp.setExpressName(dto.getExpressName());
		orderTemp.setOprStats(0);
		orderTemp.setCreateCode(dto.getUserNo());
		orderTemp.setCreateName(dto.getUserName());
		orderTemp.setUpdateCode(dto.getUserNo());
		orderTemp.setPersonChangeId(personChangId);
		orderTemp.setDepartId(departId);
		orderTemp.setWorkCode(wordCode);
		orderTemp.setFinancialOwner(financialOwner);
		orderTemp.setFinancialOwnerName(financialOwnerName);
		orderTemp.setWorkCodeStr(workCodeStr);

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
