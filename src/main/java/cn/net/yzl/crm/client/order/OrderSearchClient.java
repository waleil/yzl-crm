package cn.net.yzl.crm.client.order;

import java.util.List;

import javax.validation.constraints.NotNull;

import cn.net.yzl.order.model.vo.order.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;

@FeignClient(name = "orderSerch", url = "${api.gateway.url}/orderService/orderSearch")
public interface OrderSearchClient {

	/**
	 * @param dto
	 * @return 查询订单列表
	 * @date 2021年1月25日,下午1:28:43
	 */
	@RequestMapping(path = "v1/selectOrderList", method = RequestMethod.POST)
	public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto);

	/**
	 * @param orderNo
	 * @return 查询订单基本信息
	 * @date 2021年1月25日,下午1:28:55
	 */
	@GetMapping("v1/selectOrderInfo")
	public ComResponse<List<OrderInfoResDTO>> selectOrderInfo(@RequestParam String orderNo);

	/**
	 * @param orderNo
	 * @return 查询订单商品列表
	 * @date 2021年1月25日,下午1:29:07
	 */
	@GetMapping("v1/selectOrderProductDetail")
	public ComResponse<OrderProductListVo> selectOrderProductDetail(@RequestParam String orderNo);

	/**
	 * @param orderNo
	 * @return 查询订单操作日志
	 * @date 2021年1月25日,下午1:29:15
	 */
	@GetMapping("v1/selectOrderLogList")
	public ComResponse<List<OrderUpdateLogDTO>> selectOrderLogList(
			@RequestParam @NotNull(message = "订单编号不能为空") String orderNo);

	/**
	 * @param dto
	 * @return 查询订单审核列表
	 * @date 2021年1月25日,下午1:29:29
	 */
	@RequestMapping(path = "v1/selectOrderList4Check", method = RequestMethod.POST)
	public ComResponse<Page<OderListResDTO>> selectOrderList4Check(@RequestBody OrderList4CheckReqDTO dto);

    @ApiOperation(value = "查询订单信息服务层,为取消订单售后单提供的接口")
    @PostMapping("v1/selectOrderInfo4Opr")
    public  ComResponse<OrderInfoVo> selectOrderInfo4Opr(@RequestParam("orderNo") String orderNo);

}
