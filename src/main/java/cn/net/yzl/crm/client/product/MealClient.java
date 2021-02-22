package cn.net.yzl.crm.client.product;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.MealDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMealListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.MealVO;
import cn.net.yzl.product.model.vo.product.vo.ProductMealDetailVO;
import cn.net.yzl.product.model.vo.product.vo.ProductMealSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductMealUpdateStatusVO;
import cn.net.yzl.product.model.vo.product.vo.ProductMealUpdateTimeVO;
import io.swagger.annotations.ApiOperation;

@FeignClient(name = "mealClient", url = "${api.gateway.url}/productServer/productMeal/v1")
//@FeignClient("yzl-product-server")
public interface MealClient {

	Logger logger = LoggerFactory.getLogger(ActivityClient.class);


	/**
	 * @param
	 * @Author: wanghuasheng
	 * @Description:
	 * @Date: 2021/1/9 09:20 上午
	 * @Return: cn.net.yzl.common.entity.ComResponse<java.util.List <
	 *          cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO>>
	 */
	@GetMapping(value = "queryCountByStatus")
	@ApiOperation("按照上下架状态查询商品套餐数量")
	ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

	@GetMapping(value = "queryPageProductMeal")
	@ApiOperation("分页查询商品套餐列表")
	ComResponse<Page<ProductMealListDTO>> queryListProductMeal(@SpringQueryMap ProductMealSelectVO vo);

	/**
	 * @Description:
	 * @Author: dongjunmei
	 * @Date: 2021-01-09 11:12 上午 @param：
	 * @return: cn.net.yzl.common.entity.ComResponse
	 **/
	@PostMapping(value = "updateStatus")
	@ApiOperation("修改套餐上下架状态")

	ComResponse updateStatusByMealCode(@RequestBody @Valid ProductMealUpdateStatusVO vo);

	/**
	 * @Description:
	 * @Author: dongjunmei
	 * @Date: 2021-01-09 11:30
	 * @param :
	 * @param vo
	 * @return: cn.net.yzl.common.entity.ComResponse<java.lang.Void>
	 **/
	@PostMapping(value = "edit")
	@ApiOperation("编辑套餐")
	ComResponse<Void> editProductMeal(@RequestBody MealVO vo);

	/**
	 * @Description:
	 * @Author: dongjunmei
	 * @Date: 2021-01-09 13:00
	 * @param:
	 * @return: cn.net.yzl.common.entity.ComResponse
	 **/
	@GetMapping(value = "queryMealDetail")
	@ApiOperation("查询商品详情")
	ComResponse<ProductMealDetailVO> queryMealDetail(@RequestParam("mealNo") String mealNo);

	/**
	 * @Description:
	 * @Author: dongjunmei
	 * @Date: 2021-01-09 13:26
	 * @param vo:
	 * @return: cn.net.yzl.common.entity.ComResponse
	 **/
	@PostMapping(value = "updateTime")
	@ApiOperation("修改套餐售卖时间")
	ComResponse updateTimeByMealCode(@RequestBody @Valid ProductMealUpdateTimeVO vo);

	@GetMapping(value = "queryProductMealPortray")
	@ApiOperation("查询商品套餐画像")
	ComResponse<MealDTO> queryProductMealPortray(@RequestParam("mealNo") String mealNo);

	default MealDTO queryProductMealPortrayDefault(String productCodes) {
		try {
			ComResponse<MealDTO> listComResponse = queryProductMealPortray(productCodes);
			if (null == listComResponse || !ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
				logger.error("{message:查询商品套餐画像失败！}");
				return null;
			}
			return listComResponse.getData();
		} catch (Exception e) {
			logger.error("message:查询商品套餐画像失败！", e);
		}
		return null;
	}

	/**
	 * 按套餐编码查询套餐里的商品列表
	 * 
	 * @param ids 套餐编码，多个以半角逗号分隔
	 * @return 套餐里的商品列表
	 * @author zhangweiwei
	 * @date 2021年1月25日,下午10:34:21
	 */
	@GetMapping("/queryByIds")
	ComResponse<List<ProductMealListDTO>> queryByIds(@RequestParam @NotBlank String ids);

	default List<ProductMealListDTO> queryByIdsDefault(String productCodes) {
		try {
			ComResponse<List<ProductMealListDTO>> listComResponse = queryByIds(productCodes);
			if (null == listComResponse || !ResponseCodeEnums.SUCCESS_CODE.getCode().equals(listComResponse.getCode())) {
				logger.error("{message:根据多个商品编码查询商品信息失败！}");
				return Collections.emptyList();
			}
			return listComResponse.getData();
		} catch (Exception e) {
			logger.error("message:根据多个商品编码查询商品信息失败！", e);
		}
		return Collections.emptyList();
	}
}
