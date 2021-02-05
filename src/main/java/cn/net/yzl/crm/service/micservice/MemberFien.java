package cn.net.yzl.crm.service.micservice;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.dto.member.MemberAddressAndLevelDTO;
import cn.net.yzl.crm.customer.dto.member.MemberDiseaseCustomerDto;
import cn.net.yzl.crm.customer.dto.member.MemberGradeRecordDto;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberBaseAttr;
import cn.net.yzl.crm.customer.model.MemberGrad;
import cn.net.yzl.crm.customer.model.MemberOrderStat;
import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.crm.customer.model.MemberProductEffect;
import cn.net.yzl.crm.customer.model.ProductConsultation;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import cn.net.yzl.crm.dto.member.MemberDiseaseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 顾客服务接口
 */
@FeignClient(name = "crmCustomer", url = "${api.gateway.url}" + MemberFien.SUFFIX_URL)
public interface MemberFien {
	String SUFFIX_URL = "/crmCustomer/member";
	String CUSTOMER_AMOUNT_OPERATION_URL = "/customerAmount/operation";
	String CUSTOMER_AMOUNT_OPERATION_CONFIRM_URL = "/customerAmount/operationConfirm";

	@RequestMapping(method = RequestMethod.POST, value = "/v1/getMemberListByPage")
	ComResponse<Page<Member>> listPage(@RequestBody MemberSerchConditionDTO dto);

	@ApiOperation("保存会员信息")
	@PostMapping("/v1/save")
	GeneralResult<Boolean> save(@RequestBody Member dto);

	@ApiOperation("修改会员信息")
	@PostMapping("/v1/updateByMemberCard")
	GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto);

	@ApiOperation("根据卡号获取会员信息")
	@GetMapping("/v1/getMember")
	GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard);

	@ApiOperation("获取会员等级")
	@GetMapping("/v1/getMemberGrad")
	GeneralResult<List<MemberGrad>> getMemberGrad();

	@ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
	@GetMapping("/v1/getMemberPhoneList")
	GeneralResult<List<MemberPhone>> getMemberPhoneList(@RequestParam("member_card") String member_card);

	@ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
	@GetMapping("/v1/getMemberByPhone")
	GeneralResult<Member> getMemberByPhone(@RequestParam("phone") String phone);

	@ApiOperation("设置顾客为会员")
	@GetMapping("/v1/setMemberToVip")
	void setMemberToVip(@RequestParam("member_card") String member_card);

	@ApiOperation("获取顾客购买商品")
	@GetMapping("/v1/getMemberProductEffectList")
	GeneralResult<List<MemberProductEffect>> getMemberProductEffectList(
			@RequestParam("member_card") String member_card);

	@ApiOperation("获取顾客咨询商品")
	@GetMapping("/v1/getProductConsultationList")
	GeneralResult<List<ProductConsultation>> getProductConsultationList(
			@RequestParam("member_card") String member_card);

	@ApiOperation("获取顾客病症")
	@GetMapping("/v1/getMemberDisease")
	ComResponse<List<MemberDiseaseCustomerDto>> getMemberDisease(@RequestParam("memberCard") String memberCard);

	@ApiOperation("新增顾客病症")
	@PostMapping("/v1/insertMemberDisease")
	ComResponse<Integer> insertMemberDisease(@RequestBody MemberDiseaseDto memberDiseaseDto);

	@ApiOperation("获取购买能力")
	@GetMapping("/v1/getMemberOrderStat")
	GeneralResult<MemberOrderStat> getMemberOrderStat(@RequestParam("member_card") String member_card);

	@ApiOperation("新增购买能力")
	@GetMapping("/v1/addMemberOrderStat")
	GeneralResult<?> addMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

	@ApiOperation("修改购买能力")
	@GetMapping("/v1/updateMemberOrderStat")
	GeneralResult<?> updateMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat);

	@ApiOperation("获取顾客行为偏好字典数据")
	@GetMapping("/v1/getMemberActions")
	ComResponse<List<MemberBaseAttr>> getMemberActions();

	@GetMapping("/customerAmount/getMemberAmount")
	ComResponse<MemberAmountDto> getMemberAmount(@RequestParam("memberCard") String memberCard);

	@GetMapping("/customerAmount/getMemberAmountDetailList")
	ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(@RequestParam("memberCard") String memberCard,
			@RequestParam("timeFlag") Integer timeFlag);

	// 顾客收货地址
	@PostMapping("/memberAddress/v1/addReveiverAddress")
	ComResponse<String> addReveiverAddress(@RequestBody ReveiverAddressInsertVO reveiverAddressInsertVO);

	@PostMapping("/memberAddress/v1/updateReveiverAddress")
	ComResponse<String> updateReveiverAddress(@RequestBody ReveiverAddressUpdateVO reveiverAddressUpdateVO);

	@GetMapping("/memberAddress/v1/getReveiverAddress")
	ComResponse<List<ReveiverAddressDto>> getReveiverAddress(@RequestParam("memberCard") String memberCard);

	/**
	 * 顾客账户-账户冻结操作(充值,消费,退回)
	 *
	 * @param memberAmountDetail 顾客账户信息记录实体
	 * @return
	 * @author zhangweiwei
	 * @date 2021年1月26日,下午9:03:51
	 */
	@PostMapping(CUSTOMER_AMOUNT_OPERATION_URL)
	ComResponse<String> customerAmountOperation(@RequestBody MemberAmountDetailVO memberAmountDetail);

	/**
	 * 顾客账户-冻结金额确认(消费/退回)
	 * 
	 * @param orderNo    订单号
	 * @param obtainType 操作类型(1 退回 2 消费,3:充值(目前先不考虑)
	 * @return
	 * @author zhangweiwei
	 * @date 2021年2月1日,下午11:19:27
	 */
	@PostMapping(CUSTOMER_AMOUNT_OPERATION_CONFIRM_URL)
	ComResponse<String> customerAmountOperationConfirm(@RequestParam String orderNo, @RequestParam String obtainType);

	@ApiOperation("顾客一批顾客卡号获取顾客收货地址、余额、会员等级")
	@GetMapping("/v1/getMembereAddressAndLevelByMemberCards")
	ComResponse<List<MemberAddressAndLevelDTO>> getMembereAddressAndLevelByMemberCards(
			@RequestParam("member_cards") String memberCards);

	// 添加顾客咨询商品
	@PostMapping("v1/addProductConsultation")
	ComResponse<String> addProductConsultation(
			@RequestBody @Validated List<ProductConsultationInsertVO> productConsultationInsertVOList);

	@ApiOperation("获取会员级别记录")
	@GetMapping("v1/getMemberGradeRecordList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query") })
	public ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordList(@NotBlank String memberCard);

	@ApiOperation("顾客病症-根据病症id更新顾客病症")
	@GetMapping("/v1/updateMemberOrderStat")
	public GeneralResult<Integer> updateMemberDiseaseByDiseaseId(@RequestBody MemberDiseaseIdUpdateVO memberDiseaseIdUpdateVO);



}
