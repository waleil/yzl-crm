package cn.net.yzl.crm.client.member;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;

/**
 * 顾客收货地址feign客户端
 * 
 * @author zhangweiwei
 * @date 2021年1月27日,下午2:02:31
 */
@FeignClient(name = "memberAddressClient", url = "${api.gateway.url}/crmCustomer")
public interface MemberAddressClient {
	/**
	 * 顾客收货地址-获取顾客收货地址
	 * 
	 * @param memberCard 会员卡号
	 * @return 顾客收货地址列表
	 * @author zhangweiwei
	 * @date 2021年1月27日,下午2:00:28
	 */
	@GetMapping("/memberAddress/v1/getReveiverAddress")
	ComResponse<List<ReveiverAddressDto>> getReveiverAddress(@RequestParam @NotBlank String memberCard);
}
