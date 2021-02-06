package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderTrackDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Api(tags = "订单管理")
@FeignClient(name = "OrderTranckDetailClient", url = "${api.gateway.url}/orderService/orderTranck")
//@FeignClient(name = "OrderTranckDetailClient", url = "localhost:4455/orderTranck")
public interface OrderTranckDetailClient {



	@ApiOperation(value = "新增跟踪记录")
	@PostMapping("/v1/saveOrderTranckDetail")
	public ComResponse<Boolean> saveOrderTranckDetail(@RequestBody @Validated OrderTrackDetailDTO dto) ;

	@ApiOperation(value = "根据订单号查询跟踪记录")
	@GetMapping("/v1/selectOrderTranckList")
	public ComResponse<List<OrderTrackDetailDTO>> selectOrderTranckList(
			@RequestParam(required = false) @ApiParam(value = "订单编号", name = "orderNo") String orderNo);



}




