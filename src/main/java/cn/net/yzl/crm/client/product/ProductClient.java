package cn.net.yzl.crm.client.product;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductAtlasDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDetailVO;
import cn.net.yzl.product.model.vo.product.dto.ProductDiseaseDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMainInfoDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductPortraitDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.BatchProductVO;
import cn.net.yzl.product.model.vo.product.vo.OrderProductVO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateStatusVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateTimeVO;
import cn.net.yzl.product.model.vo.product.vo.ProductVO;
import io.swagger.annotations.ApiOperation;

@FeignClient(name = "productClient", url = "${api.gateway.url}" + ProductClient.SUFFIX_URL)
public interface ProductClient {

	Logger logger = LoggerFactory.getLogger(ProductClient.class);

	String SUFFIX_URL = "/productServer/product";
	String INCREASE_STOCK_URL = "/v1/increaseStock";
	String DECREASE_STOCK_URL = "/v1/productReduce";

	@GetMapping(value = "v1/queryCountByStatus")
	ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

	@GetMapping(value = "v1/queryPageProduct")
	ComResponse<Page<ProductListDTO>> queryListProduct(@SpringQueryMap ProductSelectVO vo);

	@PostMapping(value = "v1/edit")
	ComResponse<Void> editProduct(@RequestBody ProductVO vo);

	@PostMapping(value = "v1/updateStatus")
	ComResponse<?> updateStatusByProductCode(@RequestBody ProductUpdateStatusVO vo);

	@GetMapping("v1/queryProductListAtlas")
	ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(@RequestParam("productName") String productName,
			@RequestParam("id") Integer id, @RequestParam("pid") Integer pid);

	@PostMapping(value = "v1/updateTime")
	ComResponse<?> updateTimeByProductCode(@RequestBody ProductUpdateTimeVO vo);

	@GetMapping(value = "v1/queryProductDetail")
	ComResponse<ProductDetailVO> queryProductDetail(@RequestParam("productCode") String productCode);

	@GetMapping(value = "v1/queryProductPortrait")
	ComResponse<ProductPortraitDTO> queryProductPortrait(@RequestParam("productCode") String productCode);

	@GetMapping(value = "v1/queryDiseaseByProductCode")
	ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(@RequestParam("productCode") String productCode);

	@RequestMapping(value = "v1/queryByCodes", method = RequestMethod.GET)
	ComResponse<List<ProductDTO>> queryByCodes(@RequestParam("codes") String productCode);

	/**
	 * 扣减库存
	 *
	 * @param orderProductVO
	 * @return
	 */
	@PostMapping(value = DECREASE_STOCK_URL)
	ComResponse<?> productReduce(@RequestBody @Valid OrderProductVO orderProductVO);

	/**
	 * 按批次扣减商品库存 wangzhe 2021-01-19
	 * 
	 * @param productVO
	 * @return
	 */
	@ApiOperation("批量下单扣减库存")
	@PostMapping(value = "v1/batchReduce")
	public ComResponse<?> productReduce(@RequestBody @Valid BatchProductVO productVO);

	@GetMapping(value = "v1/queryProducts")
	ComResponse<List<ProductMainInfoDTO>> queryProducts(@RequestParam(value = "ids", required = false) String ids);

	@ApiOperation("取消单增加库存")
	@PostMapping(INCREASE_STOCK_URL)
	public ComResponse<?> increaseStock(@RequestBody @Valid OrderProductVO orderProductVO);

	/**
	 * 按一组商品编码查询商品列表 to王潇
	 * 
	 * @param codes 商品编码，多个以英文逗号分隔
	 * @return 商品列表
	 * @author zhangweiwei
	 * @date 2021年1月25日,下午10:14:01
	 */
	@GetMapping("/v1/queryByProductCodes")
	ComResponse<List<ProductMainDTO>> queryByProductCodes(@RequestParam @NotBlank String codes);

	default List<ProductMainDTO> queryByProductCodesDefault(String productCodes) {
		try {
			ComResponse<List<ProductMainDTO>> comResponse = queryByProductCodes(productCodes);
			if (null == comResponse || !ResponseCodeEnums.SUCCESS_CODE.getCode().equals(comResponse.getCode())) {
				logger.error("{message:根据多个商品编码查询商品信息失败！}");
				return Collections.emptyList();
			}
			return comResponse.getData();
		} catch (Exception e) {
			logger.error("message:根据多个商品编码查询商品信息失败！", e);
		}
		return Collections.emptyList();
	}

	@GetMapping("v1/queryProductListAtlasByDiseaseName")
	ComResponse<List<ProductAtlasDTO>> queryProductListAtlasByDiseaseName(@RequestParam(value = "diseaseName") String diseaseName);

	@GetMapping(value = "v1/queryCategoryCountByCodes")
	ComResponse<Integer> queryCategoryCountByCodes(@RequestParam("productCodes") String[] productCodes);
}