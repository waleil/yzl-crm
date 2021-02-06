package cn.net.yzl.crm.controller.order;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.AccountDetailFeignClient;
import cn.net.yzl.order.model.vo.member.AccountDetailIn;
import cn.net.yzl.order.model.vo.member.AccountDetailOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 账户余额明细控制器
 * 
 * @author zhangweiwei
 * @date 2021年2月6日,下午7:07:21
 */
@Api(tags = "结算中心")
@RestController
@RequestMapping("/accountdetail")
public class AccountDetailController {
	@Resource
	private AccountDetailFeignClient accountDetailFeignClient;

	@PostMapping("/v1/querypagelist")
	@ApiOperation(value = "查询账户余额明细列表--支持分页", notes = "查询账户余额明细列表--支持分页")
	public ComResponse<Page<AccountDetailOut>> queryPageList(@RequestBody AccountDetailIn accountDetailIn) {
		return this.accountDetailFeignClient.queryPageList(accountDetailIn);
	}
}
