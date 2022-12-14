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
	// ????????????????????????????????????
	private static final int MAX_SIZE = 100;

	//??????????????????
	private static final String CACHEKEYPREFIX = "crm-newOrder:" ;

	//??????????????????
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
	 * ????????????
	 */
	@Override
	public ComResponse<Boolean> newOrder(NewOrderDTO dto) {
		ComResponse orderRes = null;
		AtomicInteger totalCount = null;// ?????????

		try {

			// ????????????
			ComResponse<StaffChangeRecordDto> response = ehrStaffClient.getStaffLastChangeRecord(dto.getUserNo());
			if (response.getCode().compareTo(Integer.valueOf(200)) != 0) {
				log.error("????????????????????????>>"  ,response.getCode()+"," +response.getMessage());
				throw new BizException(response.getCode(), response.getMessage());
			}
			if(response.getCode() == null){
				log.error("?????????????????????????????????????????????" + dto.getUserNo());
				throw new BizException(response.getCode(),
						"?????????????????????????????????????????????" + dto.getUserNo()+response.getMessage());
			}

			Integer wordCode = response.getData().getWorkCode();
			Integer departId = response.getData().getDepartId();
			dto.setUserName(response.getData().getName());
			String workCodeStr = response.getData().getWorkName();
			Integer financialOwner = null;
			String financialOwnerName = "";
			String personChangId = String.valueOf(response.getData().getId());
			// ???????????????
			String batchNo = SnowFlakeUtil.getId() + "";

			// ????????????????????????????????????
			ComResponse<DepartDto> dresponse = this.ehrStaffClient.getDepartById(departId);
			// ?????????????????????
			if (ResponseCodeEnums.SUCCESS_CODE.getCode().equals(dresponse.getCode())) {
				DepartDto depart = dresponse.getData();
				financialOwner = depart.getFinanceDepartId();
				financialOwnerName = depart.getFinanceDepartName();

			} else {
				log.error("????????????????????????>>"  ,response.getCode()+"," +response.getMessage());
				throw new BizException(dresponse.getCode(), "????????????????????????>>" + dresponse.getMessage());
			}
			log.info("???????????????????????????" + dresponse.getData());
			// ????????????
			String productCodes = dto.getProducts().stream().map(Product4OrderDTO::getProductCode)
					.collect(Collectors.joining(","));

			List<OrderTempProduct> productDTOS = searchProducts(productCodes, dto.getProducts().size(), batchNo);
			Map<String, Product4OrderDTO> collect = dto.getProducts().stream()
					.collect(Collectors.toMap(Product4OrderDTO::getProductCode, product4OrderDTO -> product4OrderDTO));
			Map<String, OrderTempProduct> product4OrderDTOMap = productDTOS.stream()
					.collect(Collectors.toMap(OrderTempProduct::getProductCode, orderTempProduct -> orderTempProduct));

			// ??????????????????
			productDTOS.stream().forEach(map -> {
				Product4OrderDTO product4OrderDTO = collect.get(map.getProductCode());
				map.setCount(product4OrderDTO.getCount());
			});

			// ???????????? ???????????????????????????????????????????????????
			int total = productDTOS.stream().mapToInt(m -> m.getCount() * m.getPrice()).sum();
			if (new BigDecimal(MathUtils.priceFenConvertYun(total)).compareTo(dto.getTotalAllAMT()) != 0) {
				log.error("??????????????????????????????,???????????????"  + MathUtils.priceFenConvertYun(total));
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "??????????????????????????????");
			}

			// ??????????????????
			List<CrowdGroup> groups = searchGroups(dto.getCustomerGroupIds());
			// ??????????????????
			OrderTemp orderTemp = mkOrderTemp(groups, batchNo, dto, String.valueOf(departId), wordCode, financialOwner,
					financialOwnerName,workCodeStr,personChangId);

			// ?????????
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
			// ????????????
			reduceVOS.forEach(map -> {
				OrderTempProduct product = product4OrderDTOMap.get(map.getProductCode());
				 if (product.getStock() != -1 && map.getNum() > product.getStock()) {
					throw new BizException(ResponseCodeEnums.RESUME_EXIST_ERROR_CODE.getCode(),
							"??????????????????????????????" + product.getProductName());
				}

			});
			vo.setProductReduceVOS(reduceVOS);
			// ?????????????????????????????????????????????????????????
			orderRes = productClient.productReduce(vo);

			if (orderRes.getCode().compareTo(Integer.valueOf(200)) != 0) {
				log.error("??????????????????:" + vo);
				throw new BizException(orderRes.getCode(),"??????????????????" +  orderRes.getMessage());
			}
			OrderTempVO orderTempVO = new OrderTempVO();
			orderTempVO.setOrderTemp(orderTemp);
			orderTempVO.setProducts(productDTOS);


			redisUtil.set(CACHEKEYPREFIX + "fail" +"-"+ orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
			redisUtil.set(CACHEKEYPREFIX + "opr" +"-"+orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
			redisUtil.set(CACHEKEYPREFIX +  "suc" +"-"+ orderTemp.getOrderTempCode(),0,CACHEKEY_TIMEOUT);
			// ????????????????????????
			orderRes = newOrderClient.newOrderTemp(orderTempVO);

			if (orderRes.getCode().compareTo(Integer.valueOf(200)) != 0) {
				throw new BizException(orderRes.getCode(),"??????????????????????????????" + orderRes.getMessage());
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

		   // ????????????????????????????????????
		   ComResponse<List<OrderTempVO>> listComResponse = newOrderClient.selectUnOpredHKOrder();
		   if (listComResponse == null ||!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
			  log.error("??????????????????????????????????????? {}-{}",listComResponse.getCode(),listComResponse.getMessage());
			   throw new BizException(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(), "???????????????????????????????????????????????????");
		   }
		   List<OrderTempVO> data = listComResponse.getData();
		   if (data == null || data.size() == 0) {
			   continueFlag = false;
			   break;
		   }
		   OrderTempVO map = data.get(0);

		   // ????????????????????????mq
		   prepareAndSendMessage(map.getOrderTemp(), map.getProducts());


		   // ??????????????????????????????????????????
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

		   // ?????????????????????
		   ComResponse<Boolean> res = newOrderClient.updateResult(map.getOrderTemp());
		   if (!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(res.getCode())) {
			   throw new BizException(res.getCode(), res.getMessage());
		   }



	   }while(continueFlag);



		return ComResponse.success(true);
	}

	/**
	 * ????????????
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
	 * ????????????mq??????????????????
	 * 
	 * @param orderTemp
	 * @param product

	 */
	private void prepareAndSendMessage(OrderTemp orderTemp, List<OrderTempProduct> product) {
		List<String> groupids = Arrays.asList(orderTemp.getGroupId().split(","));
		for (String map : groupids) {
			ComResponse<List<GroupRefMember>> listComResponse = memberGroupFeign.queryMembersByGroupId(map);
			if (listComResponse==null ||!ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
				// ???????????????????????????
				log.error("????????????????????????????????????{}-{}" , listComResponse.getCode() ,listComResponse.getMessage());
				throw new BizException(listComResponse.getCode(),"????????????????????????!" );
			} else {
				List<GroupRefMember> data = listComResponse.getData();
				if (CollectionUtils.isEmpty(data)) {
					log.info("???????????????????????????????????????????????????id??????????????????,????????????" + map);
					orderTemp.setOprStats(3);//??????
					continue;
				}
				// ?????????????????????????????????
				//????????????????????????????????????
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
					//???????????????????????????????????????
					ComResponse<List<MemberCouponInfoDTO>> accountByMemberCardsRes = activityClient.getAccountByMemberCards(data.subList(offset, toIndex).stream()
							.map(GroupRefMember::getMemberCard).collect(Collectors.toList()));
					if(accountByMemberCardsRes == null || Integer.compare(accountByMemberCardsRes.getCode(),Integer.valueOf(200))!=0){
						log.error("??????dmc??????????????????????????????",accountByMemberCardsRes);
						throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),"??????dmc??????????????????????????????");
					}
					List<MemberCouponInfoDTO> memberCouponInfoDTOS = accountByMemberCardsRes.getData();
					Map<String, MemberCouponInfoDTO> collect = memberCouponInfoDTOS.stream().collect(Collectors.toMap(MemberCouponInfoDTO::getMemberCard, memberCouponInfoDTO -> memberCouponInfoDTO));
					offset = toIndex;
					members.stream().forEach(item -> {
						MemberCouponInfoDTO memberCouponInfoDTO = collect.get(item.getMemberCard());
						OrderInfo4Mq vo = mkOrderInfo(item, orderTemp, product, item,memberCouponInfoDTO);
						redisUtil.incr(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode() + "-" + map);
						redisUtil.incr(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode());
						if (vo == null) {// ??????????????????????????????????????????
							redisUtil.incr(CACHEKEYPREFIX + "fail" + "-" +  orderTemp.getOrderTempCode());
//							failCnt.incrementAndGet();
						} else {
							sendMessage(vo);
							log.info("?????????????????????????????????{},????????????{}",orderTemp.getOrderTempCode(),vo.getOrderM().getOrderNo());
							redisUtil.incr(CACHEKEYPREFIX + "suc" + "-" +  orderTemp.getOrderTempCode());
//							successCnt.incrementAndGet();
						}

					});

				}

				data.clear();
			}

		}

		int totalopr = (Integer) redisUtil.get(CACHEKEYPREFIX + "opr" + "-" + orderTemp.getOrderTempCode());
		log.info("??????????????????????????????" + orderTemp.getOrderTempCode() +"????????? " + totalopr +" ?????????");
		if(totalopr <orderTemp.getGroupCount()){
			orderTemp.setOprStats(3);
		}else{
			orderTemp.setOprStats(2);
		}
	}

	/**
	 * ??????????????????
	 * 
	 * @param item
	 * @param orderTemp
	 * @param product
	 * @param memberCouponInfoDTO
	 * @return
	 */
	private OrderInfo4Mq mkOrderInfo(MemberAddressAndLevelDTO item, OrderTemp orderTemp, List<OrderTempProduct> product,
									 MemberAddressAndLevelDTO member, MemberCouponInfoDTO memberCouponInfoDTO) {

		// ?????????
		String orderNo = redisUtil.getSeqNo(RedisKeys.CREATE_ORDER_NO_PREFIX, RedisKeys.CREATE_ORDER_NO, 6);
		ReveiverAddressMsgDTO addressMsgDTO = checkMember(member);
		if (addressMsgDTO == null) {
			log.info("??????????????????????????????????????????????????????????????????????????????{}",member.getMemberCard());
			return null;
		}

		OrderInfo4Mq orderInfo = new OrderInfo4Mq();
		// ????????????
		OrderM orderM = mkOrderM(orderNo, orderTemp, product, member, addressMsgDTO,memberCouponInfoDTO);
		if(orderM != null){
			orderInfo.setOrderM(orderM);
			// ???????????????
			List<OrderDetail> orderDetails = this.selectAndMkDetail(orderNo, orderM, product, member);
			if(orderDetails == null){
				return null;
			}
			orderInfo.setDetailList(orderDetails);
		}


		return orderInfo;

	}

	/**
	 * ??????????????????????????? ???????????? ???????????? ????????? ?????????nul ????????????
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
	 * ????????????
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
		orderM.setWorkOrderNo(-1);// ???????????? ??????????????????????????????-1
		orderM.setWorkBatchNo("-1");// ??????????????? ??????-1
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

		orderM.setTotal(orderTemp.getTotalAllAmt());// ??????
		orderM.setTotalAll(orderTemp.getTotalAllAmt());// ????????????
		orderM.setPfee(0);
		orderM.setPfeeFlag(0);
		orderM.setCash(orderTemp.getTotalAllAmt());// ??????
		orderM.setCash1(0);// ??????
		orderM.setSpend(orderTemp.getTotalAmt());// ????????????

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
		orderM.setPayType(CommonConstant.PAY_TYPE_0);// ???????????? ????????????
		orderM.setPayMode(CommonConstant.PAY_MODE_K);// ????????????
		orderM.setPayStatus(null);// ?????????
		orderM.setDistrubutionMode(CommonConstant.DISTRUBUTION_MODE_KD);// ???????????? ??????
        if(!StringUtils.isNullOrEmpty(orderTemp.getExpressCode())){
			orderM.setExpressCompanyFlag(1);
			orderM.setExpressCompanyCode(orderTemp.getExpressCode());
			orderM.setExpressCompanyName(orderTemp.getExpressName());
		}

		orderM.setRelationOrder(orderTemp.getRelationOrder());
		//  ??????????????????
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
		orderM.setOrderAfterSpare(MathUtils.strPriceToLong(String.valueOf(member.getTotalMoney())));// ????????????

		orderM.setFinancialOwner(orderTemp.getFinancialOwner());
		orderM.setFinancialOwnerName(orderTemp.getFinancialOwnerName());
		orderM.setRelationReissueOrderNo(CommonConstant.RELATION_REISSUE_ORDER_NO);
		orderM.setLogisticsClaims(null);
		orderM.setRelationReissueOrderTotal(null);
        //??????????????????
		orderM.setOrderAfterIntegral(memberCouponInfoDTO.getMemberIntegral()==null?0:memberCouponInfoDTO.getMemberIntegral());//????????????
		orderM.setOrderAfterRebate(memberCouponInfoDTO.getMemberCouponSize()==null?0:memberCouponInfoDTO.getMemberRedBag());//???????????????
		orderM.setOrderAfterRed(memberCouponInfoDTO.getMemberRedBag()==null?0:memberCouponInfoDTO.getMemberRedBag());//????????????
		orderM.setOrderAfterSpare(MathUtils.strPriceToLong(String.valueOf(member.getTotalMoney())));// ????????????

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
	 * ??????????????????????????????
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

		//??????????????????????????????????????????
		ComResponse<String> storeRes = orderSearchClient.selectSplitStore(Integer.valueOf(order.getReveiverProvince()));
		if(storeRes == null ||Integer.compare(storeRes.getCode(),Integer.valueOf(200))!=0){
			log.error("??????????????????????????????????????????????????????");
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode()
					,"??????????????????????????????????????????????????????!");
		}
		if(StringUtils.isNullOrEmpty(storeRes.getData())){
			log.info("?????????????????????????????????{} ?????????????????????????????????????????????",member.getMemberCard());
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
			// todo ???

		}

	}

	/**
	 * ????????????????????????
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
	 * ??????http?????????????????????
	 */
	protected void sentHttpRequest(String url, Object param) {
		template.postForObject(url, param, ComResponse.class);
	}

	/**
	 * ??????????????????
	 *
	 * @param groupCodes
	 * @return
	 */
	protected List<CrowdGroup> searchGroups(List<String> groupCodes) {
		String groupsStr = groupCodes.stream().collect(Collectors.joining(","));
		// ????????????????????????
		ComResponse<List<CrowdGroup>> response = memberGroupFeign.getCrowdGroupList(groupsStr);
		if (response.getCode().compareTo(Integer.valueOf(200)) != 0) {

			throw new BizException(response.getCode(), "????????????????????????>>" + response.getMessage());
		}
		if (response.getData() != null && (groupCodes.size() != response.getData().size())) {
			throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "?????????????????????");
		}
		response.getData().forEach(map ->{
			if(map.getEnable() == 0){
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),
						"???????????????! ??????????????? " + map.getCrowd_name());
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
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "???????????????????????????" + map.get_id());
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
	 * ??????????????????
	 * 
	 * @param productCodes
	 * @param
	 * @return
	 */
	protected List<OrderTempProduct> searchProducts(String productCodes, int prodCnt, String tempCode) {
		ComResponse<List<ProductMainDTO>> prd = productClient.queryByProductCodes(productCodes);
		if (prd.getCode().compareTo(200) != 0) {
			throw new BizException(prd.getCode(), "????????????????????????>>" + prd.getMessage());
		}
		if (prd.getData() != null && (prodCnt != prd.getData().size())) {
			throw new BizException(ResponseCodeEnums.REPEAT_ERROR_CODE.getCode(), "?????????????????????");
		}

		List<OrderTempProduct> list = prd.getData().stream().map(m -> {
			if(CommonConstant.PRODUCT_AND_MEAL_STATUS_0 == m.getStatus()){
				throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),
						m.getName()+",???????????????");
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
