package cn.net.yzl.crm.client.order;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingDTO;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingProduct;
import cn.net.yzl.order.model.vo.order.UpdateOrderCheckSettingDTO;

@FeignClient(name = "orderSettingClient", url = "${api.gateway.url}/orderService/orderCheckSetting")
public interface OrderSettingClient {

	/**
	 * 查询所有免审配置
	 * 
	 * @param enableFlag 0通用 1启用
	 * @return
	 */
	@RequestMapping(path = "v1/findAllNonCheckSettings", method = RequestMethod.GET)
	public ComResponse<List<OrderCheckSettingDTO>> findAllNonCheckSettings(@RequestParam Integer enableFlag);

	/**
	 * 删除免审配置关联产品
	 * 
	 * @return
	 */
	@RequestMapping(path = "v1/deleteNonCheckSettings", method = RequestMethod.POST)
	public ComResponse<Boolean> deleteNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto);

	/**
	 * 新增规则关联商品
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(path = "v1/createNonCheckSettings", method = RequestMethod.POST)
	public ComResponse<Boolean> createNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto);

	/**
	 * 更新免审配置
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(path = "v1/updateNonCheckSettings", method = RequestMethod.POST)
	public ComResponse<Boolean> updateNonCheckSettings(@RequestBody @Valid OrderCheckSettingDTO dto);

	/**
	 * 查询免审规则已选商品
	 * 
	 * @param settingType 配置类型
	 * @return
	 */
	@RequestMapping(path = "v1/selectSettingedProducts", method = RequestMethod.GET)
	public ComResponse<Page<OrderCheckSettingProduct>> selectSettingedProducts(
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize,
			@RequestParam(required = false) Integer settingType);

}
