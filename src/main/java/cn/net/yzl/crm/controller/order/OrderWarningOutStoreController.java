package cn.net.yzl.crm.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OutStoreWarningClient;
import cn.net.yzl.order.model.vo.order.OrderWarningOutStorePageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhouchangsong
 */
@RestController
@RequestMapping("orderWarningOutStore")
@Api(tags = "出库预警管理")
public class OrderWarningOutStoreController {
	@Autowired
	private OutStoreWarningClient outStoreWarningClient;

	@GetMapping("v1/getPageList")
	@ApiOperation("出库预警分页")
	public ComResponse<Page<OrderWarningOutStorePageDTO>> getPageList(
			@RequestParam @ApiParam(value = "当前页码", required = true) Integer pageNo,
			@RequestParam @ApiParam(value = "每页总数量", required = true) Integer pageSize,
			@RequestParam @ApiParam(value = "订单编号") String orderNo) {
		return this.outStoreWarningClient.getPageList(pageNo, pageSize, orderNo);
	}
}
