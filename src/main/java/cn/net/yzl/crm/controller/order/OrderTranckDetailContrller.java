package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.order.OrderTranckDetailClient;
import cn.net.yzl.crm.service.order.IOrderTranckDetailService;
import cn.net.yzl.order.model.vo.order.OrderTrackDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderTranck")
@Api(tags = "订单管理")
public class OrderTranckDetailContrller {

	@Autowired
	private OrderTranckDetailClient service;

	@Autowired
	private IOrderTranckDetailService tranckDetailService;




	@ApiOperation(value = "新增跟踪记录")
	@PostMapping("/v1/saveOrderTranckDetail")
	public ComResponse<Boolean> saveOrderTranckDetail(@RequestBody @Validated OrderTrackDetailDTO dto) {


		return tranckDetailService.saveOrderTranckDetail(dto);
	}

	@ApiOperation(value = "根据订单号查询跟踪记录")
	@GetMapping("/v1/selectOrderTranckList")
	public ComResponse<List<OrderTrackDetailDTO>> selectOrderSaleList(
			@RequestParam(required = false) @ApiParam(value = "订单编号", name = "orderNo") String orderNo) {

		return service.selectOrderTranckList(orderNo);
	}


}
