package cn.net.yzl.crm.controller.order;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.ComparisonMgtFeignClient;
import cn.net.yzl.order.model.vo.order.CompareOrderIn;
import cn.net.yzl.order.model.vo.order.CompareOrderOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 对账订单管理
 * 
 * @author zhangweiwei
 * @date 2021年2月8日,上午9:35:03
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/comparisonmgt")
public class ComparisonMgtController {
	@Resource
	private ComparisonMgtFeignClient comparisonMgtFeignClient;

	@PostMapping("/v1/querypagelist")
	@ApiOperation(value = "查询对账订单列表--支持分页", notes = "查询对账订单列表--支持分页")
	public ComResponse<Page<CompareOrderOut>> queryPageList(@RequestBody CompareOrderIn orderin) {
		return this.comparisonMgtFeignClient.queryPageList(orderin);
	}
}
