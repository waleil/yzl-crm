package cn.net.yzl.crm.controller.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.client.order.OrderSaleClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckListVO;
import cn.net.yzl.order.model.vo.order.OrderSaleDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleListVO;
import cn.net.yzl.order.model.vo.order.OrderSaleMemberInfo;
import cn.net.yzl.order.model.vo.order.OrderSaleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/orderSale")
@Api(tags = "售后订单管理")
public class OrderSaleController {
	@Autowired
	private OrderSaleClient orderSaleClient;

	@Autowired
	private EhrStaffClient ehrStaffClient;
	@Autowired
	private MemberFien memberFien;

	@ApiOperation(value = "新建或修改售后订单")
	@PostMapping("/v1/saveOrderSale")
	public ComResponse<Boolean> saveOrUpdateOrderSale(@RequestBody @Validated OrderSaleVO orderSalem,
			HttpServletRequest request) {
		ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
		if (userNo.getData() != null) {
			String workCodeStr = userNo.getData().getWorkCodeStr();
			orderSalem.setSaleOrderNo(workCodeStr);
		}
		ComResponse<Boolean> comResponse = null;
		if (orderSalem.getSaleOrderNo() == null) {
			orderSalem.setCreateCode(request.getHeader("userNo"));
			comResponse = orderSaleClient.saveOrderSale(orderSalem);
		} else {
			orderSalem.setUpdateCode(request.getHeader("userNo"));
			comResponse = orderSaleClient.updateOrderSale(orderSalem);
		}
		return comResponse;
	}

	@ApiOperation(value = "查询售后单列表")
	@GetMapping("/v1/selectOrderSaleList")
	public ComResponse<List<OrderSaleListVO>> selectOrderSaleList(
			@RequestParam(required = false, name = "orderNo") @ApiParam(value = "订单编号", name = "orderNo") String orderNo,
			@RequestParam(required = false, name = "saleOrderType") @ApiParam(value = "售后方式：0=退货，1=换货 2=拒收", name = "saleOrderType") Integer saleOrderType,
			@RequestParam(required = false, name = "refundType") @ApiParam(value = "退款方式：0=快递代办，1=微信转账，2=支付宝转账，3=银行卡转账，4=退回账户余款", name = "refundType") Integer refundType,
			@RequestParam(required = false, name = "memberName") @ApiParam(value = "顾客名称", name = "memberName") String memberName,
			@RequestParam(required = false, name = "createStartTime") @ApiParam(value = "开始时间", name = "createStartTime") String createStartTime,
			@RequestParam(required = false, name = "createEndTime") @ApiParam(value = "结束时间", name = "createEndTime") String createEndTime,
			@RequestParam(required = false, name = "pageSize", defaultValue = "18") @ApiParam(value = "返货类型", name = "pageSize") Integer pageSize,
			@RequestParam(required = false, name = "pageNum", defaultValue = "1") @ApiParam(value = "返货类型", name = "pageNum") Integer pageNum) {

		return orderSaleClient.selectOrderSaleList(orderNo, saleOrderType, refundType, memberName, createStartTime,
				createEndTime, pageSize, pageNum);
	}

	@ApiOperation(value = "查询售后订单审核页详情")
	@GetMapping("/v1/selectOrderSaleCheckInfo")
	public ComResponse<OrderSaleCheckDetailVO> selectOrderSaleCheckInfo(
			@RequestParam(name = "saleOrderNo") @NotBlank(message = "售后订单不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号") String saleOrderNo) {
		ComResponse<OrderSaleCheckDetailVO> comResponse = orderSaleClient.selectOrderSaleCheckInfo(saleOrderNo);
		if (comResponse.getData() != null) {
			OrderSaleCheckDetailVO orderSaleCheckDetailVO = comResponse.getData();
			GeneralResult<Member> memberResult = memberFien.getMember(orderSaleCheckDetailVO.getMemberCardNo());
			if (memberResult.getData() != null) {
				Member member = memberResult.getData();
				OrderSaleMemberInfo orderSaleMemberInfo = orderSaleCheckDetailVO.getOrderSaleMemberInfo();
				orderSaleMemberInfo.setMemberProvinceCode(member.getProvince_code());
				orderSaleMemberInfo.setMemberCityCode(member.getCity_code());
				orderSaleMemberInfo.setMemberRegionCode(member.getRegion_code());
				orderSaleMemberInfo.setMemberEmail(member.getEmail());
				orderSaleMemberInfo.setMemberBalance(member.getMember_amount().getTotal_money());
				orderSaleMemberInfo.setMemberRedPacket(member.getMember_amount().getLast_red_bag());
				orderSaleMemberInfo.setMemberCoupon(member.getMember_amount().getLast_coupon());
				orderSaleCheckDetailVO.setOrderSaleMemberInfo(orderSaleMemberInfo);
				comResponse.setData(orderSaleCheckDetailVO);
			}
		}
		return comResponse;
	}

	@ApiOperation(value = "修改页查询订单信息")
	@GetMapping("/v1/selectOrderSaleInfo")
	public ComResponse<OrderSaleDetailVO> selectOrderSaleInfo(
			@RequestParam(required = false, name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
			@RequestParam(name = "saleOrderNo") @NotBlank(message = "售后单号不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号", required = true) String saleOrderNo) {
		ComResponse<OrderSaleDetailVO> comResponse = orderSaleClient.selectOrderSaleInfo(orderNo, saleOrderNo);
		if (comResponse.getData() != null) {
			OrderSaleDetailVO orderSaleCheckDetailVO = comResponse.getData();
			GeneralResult<Member> memberResult = memberFien.getMember(orderSaleCheckDetailVO.getMemberCardNo());
			if (memberResult.getData() != null) {
				Member member = memberResult.getData();
				OrderSaleMemberInfo orderSaleMemberInfo = orderSaleCheckDetailVO.getOrderSaleMemberInfo();
				orderSaleMemberInfo.setMemberProvinceCode(member.getProvince_code());
				orderSaleMemberInfo.setMemberCityCode(member.getCity_code());
				orderSaleMemberInfo.setMemberRegionCode(member.getRegion_code());
				orderSaleMemberInfo.setMemberEmail(member.getEmail());
				orderSaleCheckDetailVO.setOrderSaleMemberInfo(orderSaleMemberInfo);
				comResponse.setData(orderSaleCheckDetailVO);
			}
		}
		return comResponse;
	}

	@ApiOperation(value = "售后单审批")
	@GetMapping("/v1/updateOrderSaleState")
	public ComResponse<Boolean> updateOrderSaleState(
			@RequestParam(name = "orderSaleNo") @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "orderSaleNo", value = "售后单号", required = true) String orderSaleNo,
			@RequestParam(name = "state") @NotBlank(message = "审批状态不能为空") @ApiParam(name = "state", value = "审批状态 0:不通过,其他:通过", required = true) Integer checkStatus,
			@RequestParam(required = false, name = "userNo") @ApiParam(name = "userNo", value = "用户ID") String userNo,
			@RequestParam(required = false, name = "userWorkInfo") @ApiParam(name = "userWorkInfo", value = "用户岗位") Integer userWorkInfo,
			@RequestParam(required = false, name = "remark") @ApiParam(name = "remark", value = "备注") String remark,
			@RequestParam(required = false, name = "checkType") @ApiParam(name = "checkType", value = "售后单方式：0=退货，1=换货 2=拒收") String checkType) {
		return orderSaleClient.updateOrderSaleState(orderSaleNo, checkStatus, userNo, userWorkInfo, remark, checkType);
	}

	@ApiOperation(value = "查询售后订单审批列表")
	@GetMapping("/v1/selectOrderSaleCheckList")
	public ComResponse<List<OrderSaleCheckListVO>> selectOrderSaleCheckList(
			@RequestParam(required = false, name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
			@RequestParam(required = false, name = "memberName") @ApiParam(name = "memberName", value = "顾客姓名") String memberName,
			@RequestParam(required = false, name = "createStartTime") @ApiParam(name = "createStartTime", value = "开始时间") String createStartTime,
			@RequestParam(required = false, name = "createEndTime") @ApiParam(name = "createEndTime", value = "结束时间") String createEndTime,
			@RequestParam(required = false, name = "pageSize", defaultValue = "18") @ApiParam(value = "页数", name = "pageSize") Integer pageSize,
			@RequestParam(required = false, name = "pageNum", defaultValue = "1") @ApiParam(value = "条数", name = "pageNum") Integer pageNum,
			@RequestParam(name = "state") @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "state", value = "售后单状态 1:未审核,其他:已审核", required = true) Integer state) {
		return orderSaleClient.selectOrderSaleCheckList(orderNo, memberName, createStartTime, createEndTime, pageSize,
				pageNum, state);
	}

	@ApiOperation(value = "根据订单号查询订单信息")
	@GetMapping("/v1/selectOrderSaleProductInfoByOrderNo")
	public ComResponse<OrderSaleDetailVO> selectOrderSaleProductInfoByOrderNo(
			@RequestParam(name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo) {
		return orderSaleClient.selectOrderSaleProductInfoByOrderNo(orderNo);
	}
}