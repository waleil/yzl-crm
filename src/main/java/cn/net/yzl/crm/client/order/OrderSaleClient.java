package cn.net.yzl.crm.client.order;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderSale;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleCheckListVO;
import cn.net.yzl.order.model.vo.order.OrderSaleDetailVO;
import cn.net.yzl.order.model.vo.order.OrderSaleListVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@FeignClient(name = "orderSale", url = "${api.gateway.url}/orderService/orderSale")
public interface OrderSaleClient {
	// 新建售后订单
	@PostMapping("/v1/saveOrderSale")
	public ComResponse<Boolean> saveOrderSale(@RequestBody @Validated OrderSale orderSalem);

	@PostMapping("/v1/updateOrderSale")
	public ComResponse<Boolean> updateOrderSale(@RequestBody @Validated OrderSale orderSalem);

	@ApiOperation(value = "查询售后单列表")
	@GetMapping("/v1/selectOrderSaleList")
	public ComResponse<List<OrderSaleListVO>> selectOrderSaleList(
			@RequestParam(required = false, name = "orderNo") @ApiParam(value = "订单编号", name = "orderNo") String orderNo,
			@RequestParam(required = false, name = "saleOrderType") @ApiParam(value = "售后类型", name = "saleOrderType") Integer saleOrderType,
			@RequestParam(required = false, name = "refundType") @ApiParam(value = "返货类型", name = "refundType") Integer refundType,
			@RequestParam(required = false, name = "memberName") @ApiParam(value = "顾客名称", name = "memberName") String memberName,
			@RequestParam(required = false, name = "createStartTime") @ApiParam(value = "开始时间", name = "createStartTime") String createStartTime,
			@RequestParam(required = false, name = "createEndTime") @ApiParam(value = "结束时间", name = "createEndTime") String createEndTime,
			@RequestParam(required = false, name = "pageSize") @ApiParam(value = "页数", name = "pageSize") Integer pageSize,
			@RequestParam(required = false, name = "pageNum") @ApiParam(value = "条数", name = "pageNum") Integer pageNum);

	@ApiOperation(value = "查询售后单详情")
	@GetMapping("/v1/selectOrderSaleInfo")
	public ComResponse<OrderSaleDetailVO> selectOrderSaleInfo(
			@RequestParam(required = false, name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
			@RequestParam(name = "saleOrderNo") @NotBlank(message = "售后单号不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号", required = true) String saleOrderNo);

	@ApiOperation(value = "查询售后顶单审核详情")
	@GetMapping("/v1/selectOrderSaleCheckInfo")
	public ComResponse<OrderSaleCheckDetailVO> selectOrderSaleCheckInfo(
			@RequestParam(name = "saleOrderNo") @NotBlank(message = "售后订单不能为空") @ApiParam(name = "saleOrderNo", value = "售后单号") String saleOrderNo);

	@ApiOperation(value = "根据订单号查询订单信息")
	@GetMapping("/v1/selectOrderSaleProductInfoByOrderNo")
	public ComResponse<OrderSaleDetailVO> selectOrderSaleProductInfoByOrderNo(
			@RequestParam(name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo);

	@ApiOperation(value = "查询售后订单审批列表")
	@GetMapping("/v1/selectOrderSaleCheckList")
	public ComResponse<List<OrderSaleCheckListVO>> selectOrderSaleCheckList(
			@RequestParam(required = false, name = "orderNo") @ApiParam(name = "orderNo", value = "订单编号") String orderNo,
			@RequestParam(required = false, name = "memberName") @ApiParam(name = "memberName", value = "顾客姓名") String memberName,
			@RequestParam(required = false, name = "createStartTime") @ApiParam(name = "createStartTime", value = "开始时间") String createStartTime,
			@RequestParam(required = false, name = "createEndTime") @ApiParam(name = "createEndTime", value = "结束时间") String createEndTime,
			@RequestParam(required = false, name = "pageSize") @ApiParam(value = "页数", name = "pageSize") Integer pageSize,
			@RequestParam(required = false, name = "pageNum") @ApiParam(value = "条数", name = "pageNum") Integer pageNum,
			@RequestParam(name = "state") @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "state", value = "售后单状态 1:未审核,其他:已审核", required = true) Integer state);

	@ApiOperation(value = "售后订单审批")
	@GetMapping("/v1/updateOrderSaleState")
	public ComResponse<Boolean> updateOrderSaleState(
			@RequestParam(name = "orderSaleNo") @NotBlank(message = "售后单状态不能为空") @ApiParam(name = "orderSaleNo", value = "售后单号", required = true) String orderSaleNo,
			@RequestParam(name = "checkStatus") @NotBlank(message = "审批状态不能为空") @ApiParam(name = "state", value = "审批状态 0:不通过,其他:通过", required = true) Integer checkStatus,
			@RequestParam(required = false, name = "userNo") @ApiParam(name = "userNo", value = "用户ID") String userNo,
			@RequestParam(required = false, name = "userWorkInfo") @ApiParam(name = "userWorkInfo", value = "用户岗位") Integer userWorkInfo,
			@RequestParam(required = false, name = "remark") @ApiParam(name = "remark", value = "备注") String remark,
			@RequestParam(required = false, name = "checkType") @ApiParam(name = "checkType", value = "售后类型") String checkType);

}
