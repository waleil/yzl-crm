package cn.net.yzl.crm.client.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.excel.ExcelResult;
import cn.net.yzl.order.model.vo.order.CompareOrderIn;
import cn.net.yzl.order.model.vo.order.CompareOrderParam;
import cn.net.yzl.order.model.vo.order.CompareOrderType1Out;
import cn.net.yzl.order.model.vo.order.CompareOrderType2Out;
import cn.net.yzl.order.model.vo.order.ImportParam;

/**
 * 对账订单管理feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年2月9日,下午4:39:49
 */
@FeignClient(name = "comparisonmgt", url = "${api.gateway.url}/orderService")
public interface ComparisonMgtFeignClient {
	/**
	 * 查询待对账订单列表--支持分页
	 * 
	 * @param orderin 查询条件
	 * @return 待对账订单列表
	 * @author zhangweiwei
	 * @date 2021年2月9日,下午4:43:15
	 */
	@PostMapping("/comparisonmgt/v1/querytype1pagelist")
	ComResponse<Page<CompareOrderType1Out>> queryType1PageList(@RequestBody CompareOrderIn orderin);

	/**
	 * 查询已对账订单列表--支持分页
	 * 
	 * @param orderin 查询条件
	 * @return 已对账订单列表
	 * @author zhangweiwei
	 * @date 2021年2月9日,下午4:43:15
	 */
	@PostMapping("/comparisonmgt/v1/querytype2pagelist")
	ComResponse<Page<CompareOrderType2Out>> queryType2PageList(@RequestBody CompareOrderIn orderin);

	/**
	 * 快递对账单导入
	 * 
	 * @param param {@link ImportParam}
	 * @return {@link ExcelResult}
	 * @author zhangweiwei
	 * @date 2021年2月17日,下午11:09:56
	 */
	@PostMapping("/comparisonmgt/v1/import")
	ComResponse<ExcelResult> importFromExcel(@RequestBody ImportParam param);

	/**
	 * 对账
	 * 
	 * @param param {@link CompareOrderParam}
	 * @return
	 * @author zhangweiwei
	 * @date 2021年2月18日,下午3:04:43
	 */
	@PostMapping("/comparisonmgt/v1/compare")
	ComResponse<Object> compareOrder(@RequestBody CompareOrderParam param);

	/**
	 * 订单对账时间集合
	 * 
	 * @param orderNoList 订单号集合
	 * @return
	 * @author zhangweiwei
	 * @date 2021年3月9日,上午3:03:55
	 */
	@PostMapping("/v1/settlementtimes")
	ComResponse<Map<String, Date>> querySettlementtimes(@RequestParam List<String> orderNoList);
}
