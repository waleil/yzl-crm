package cn.net.yzl.crm.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.member.AccountDetailIn;
import cn.net.yzl.order.model.vo.member.AccountDetailOut;
import cn.net.yzl.order.model.vo.member.AccountDetailOut.DetailSummary;

/**
 * 账户余额明细feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年2月6日,下午7:39:42
 */
@FeignClient(name = "accountdetail", url = "${api.gateway.url}/orderService")
public interface AccountDetailFeignClient {

	/**
	 * 查询账户余额明细列表--支持分页
	 * 
	 * @param accountDetailIn 查询条件
	 * @return 明细列表
	 * @author zhangweiwei
	 * @date 2021年2月6日,下午7:41:58
	 */
	@PostMapping("/accountdetail/v1/querypagelist")
	ComResponse<Page<AccountDetailOut>> queryPageList(@RequestBody AccountDetailIn accountDetailIn);

	/**
	 * 查询账户余额明细列表汇总
	 * 
	 * @param accountDetailIn 查询条件
	 * @return 明细列表汇总
	 * @author zhangweiwei
	 * @date 2021年2月8日,上午3:59:12
	 */
	@PostMapping("/v1/querysummary")
	ComResponse<DetailSummary> querySummary(@RequestBody AccountDetailIn accountDetailIn);
}
