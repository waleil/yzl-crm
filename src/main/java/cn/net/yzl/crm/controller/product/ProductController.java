package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.product.ProductService;
import cn.net.yzl.product.model.vo.product.dto.*;
import cn.net.yzl.product.model.vo.product.vo.*;
import com.alibaba.nacos.common.utils.CollectionUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Api(tags = "商品服务")
@RestController
@RequestMapping("product")
public class
ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FastDFSConfig fastDFSConfig;
    /**
     * @param
     * @Author: lichanghong
     * @Description:
     * @Date: 2021/1/7 10:39 下午
     * @Return: cn.net.yzl.common.entity.ComResponse<java.util.List < cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO>>
     */
    @GetMapping(value = "v1/queryCountByStatus")
    @ApiOperation("按照上下架状态查询商品数量")
    public ComResponse<List<ProductStatusCountDTO>> queryCountByStatus() {

        return productService.queryCountByStatus();
    }

    @GetMapping(value = "v1/queryPageProduct")
    @ApiOperation("分页查询商品列表")
    public ComResponse<Page<ProductListDTO>> queryListProduct(ProductSelectVO vo) {
        //价格必须成对出现
        if ((vo.getPriceUp() != null && vo.getPriceDown() == null)
                || (vo.getPriceUp() == null && vo.getPriceDown() != null)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
        if (vo.getPriceUp() != null) {
            vo.setUpPrice((int) (vo.getPriceUp() * 100));
        }
        if (vo.getPriceDown() != null) {
            vo.setDownPrice((int) (vo.getPriceDown() * 100));
        }
        if (vo.getPageNo() == null) {
            vo.setPageNo(1);
        }
        if (vo.getPageSize() == null) {
            vo.setPageSize(15);
        }
        if (vo.getPageSize() > 50) {
            vo.setPageSize(50);
        }
        if (StringUtils.isNotBlank(vo.getKeyword())) {
            String str = vo.getKeyword();
            vo.setKeyword(str.replace("%", "\\%"));
        }
        return productService.queryListProduct(vo);
    }

    /**
     * @param vo
     * @Author: lichanghong
     * @Description: 编辑商品
     * @Date: 2021/1/8 10:45 上午
     * @Return: cn.net.yzl.common.entity.ComResponse
     */
    @PostMapping(value = "v1/edit")
    @ApiOperation("编辑商品")
    public ComResponse<Void> editProduct(@RequestBody @Valid ProductVO vo,HttpServletRequest request) {
        if(StringUtils.isBlank(vo.getProductCode())){
            vo.setProductCode(null);
        }
        //参数校验
        String str=checkParams(vo);
        if(StringUtils.isNotBlank(str)){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), str);
        }
        //获取操作人id
        String userId;
        if(StringUtils.isBlank(userId = request.getHeader("userId"))){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"校验操作员身份失败，尝试重新登陆！");
        }
        //获取修改时间和操作人
        vo.setUpdateTime(new Date());
        vo.setUpdateNo(userId);
        return productService.editProduct(vo);
    }

    /**
     * @param vo
     * @Author: lichanghong
     * @Description:
     * @Date: 2021/1/8 9:46 下午
     * @Return: cn.net.yzl.common.entity.ComResponse
     */
    @PostMapping(value = "v1/updateStatus")
    @ApiOperation("修改商品上下架状态")
    ComResponse updateStatusByProductCode(@RequestBody @Valid ProductUpdateStatusRequestVO vo,HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(userId)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户信息，请检查您的登录状态！");
        }
        ProductUpdateStatusVO update = new ProductUpdateStatusVO();
        update.setUpdateNo(userId);
        update.setStatus(vo.getStatus());
        update.setProductCodeList(vo.getProductCodeList());
        return productService.updateStatusByProductCode(update);
    }

    /**
     * @param vo
     * @Author: lichanghong
     * @Description: 参数校验
     * @Date: 2021/1/8 11:15 上午
     * @Return: java.lang.String
     */
    public String checkParams(ProductVO vo) {
        List<ProductImageVO> images = vo.getImages();

        if (images.size() != 0) {

            int[] count = {0};

            images.forEach(image ->{
                if (image.getMainFlag()==1){
                    count[0]++;
                }
            });
            if (count[0] > 1){
                return "只能存在一张主图，当前设置了"+count[0]+"张！";
            }
        }
        if (vo.getCostPriceD() == null) {
            return "市场价价格不能为空";
        }
        if (vo.getSalePriceD() == null) {
            return "市场价价格不能为空";
        }
        //更改为上架时需要上传图片
//        if (CollectionUtils.isEmpty(vo.getImages())||vo.getImages().size()>5){
//            return "商品图片数量不正确，应为1-5张，当前为"+vo.getImages()==null?"0":vo.getImages().size()+"张";
//        }
        return null;
    }


    @ApiOperation("查询商品图谱,两个参数至少填入一个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName", value = "商品名称",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "病症id", dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "病症pid", dataType = "Int", paramType = "query"),
    })
    @GetMapping("v1/queryProductListAtlas")
    public ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(@RequestParam(value = "productName",required = false) String productName, @RequestParam(value = "id",required = false) Integer id, @RequestParam(value = "pid",required = false) Integer pid){
        if (StringUtils.isBlank(productName)&&(pid == null || id == null)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(),"商品编号和商品id+pid组必须添加至少一项！");
        }
        if (null != id&& null != pid){
            if (id < 1 || pid < 0){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(),"非法的id/pid参数！");
            }
        }
        return productService.queryProductListAtlas(productName,id,pid).setMessage(fastDFSConfig.getUrl());
    }

    @PostMapping(value = "v1/updateTime")
    @ApiOperation("修改商品售卖时间")
    ComResponse updateTimeByProductCode(@RequestBody @Valid ProductUpdateTimeRequestVO vo, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : result.getAllErrors()) {
                sb.append(error.getDefaultMessage() + ",");
            }
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), sb.toString());
        }
        ProductUpdateTimeVO params = new ProductUpdateTimeVO();
        if (CollectionUtils.isEmpty(vo.getProductCodeList())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE.getCode(), "商品code不能为空");
        }
        BeanUtils.copyProperties(vo, params);
        String userId = request.getHeader("userId");
        if(StringUtils.isBlank(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"无法获取操作员工编号，请检查您的登录状态！");
        }
        params.setUpdateNo(userId);
        return productService.updateTimeByProductCode(params);

    }

    @GetMapping(value = "v1/queryDetail")
    @ApiOperation("查询商品详情")
    public ComResponse<ProductDetailVO> queryProductDetail(@RequestParam("productCode") String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"目标商品编号不能为空！");
        }
        ComResponse<ProductDetailVO> productDetailVOComResponse = productService.queryProductDetail(productCode);

        if (productDetailVOComResponse.getData()==null){
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);
        }
        return productDetailVOComResponse;
    }
    @GetMapping(value = "v1/queryProductPortrait")
    @ApiOperation("查询商品画像")
    public ComResponse<ProductPortraitDTO> queryProductPortrait(@RequestParam("productCode") String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"目标商品编号不能为空！");
        }
        return productService.queryProductPortrait(productCode).setMessage(fastDFSConfig.getUrl());
    }

    /**
     * @param productCode
     * @Author: lichanghong
     * @Description: 根据商品编号查询病症
     * @Date: 2021/1/10 4:03 下午
     * @Return: java.util.List<cn.net.yzl.product.model.vo.product.dto.ProductDiseaseDTO>
     */
    @GetMapping(value = "v1/queryDiseaseByProductCode")
    @ApiOperation("根据商品编号查询病症")
    public ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(@RequestParam("productCode") String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"目标商品编号不能为空！");
        }
        return productService.queryDiseaseByProductCode(productCode);
    }

    @GetMapping(value = "v1/queryProducts")
    @ApiOperation("根据商品编号串查询商品列表,需要在编号中间用半角,间隔（如需查询全部则键入空值）")
    ComResponse<List<ProductMainInfoDTO>> queryProducts(@RequestParam(value = "ids",required = false) String ids){
        return productService.queryProducts(ids);
    }

    @ApiOperation("根据病症查询商品图谱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "diseaseName", value = "商品名称", dataType = "String", paramType = "query")
    })
    @GetMapping("v1/queryProductListAtlasByDiseaseName")
    public ComResponse<List<ProductAtlasDTO>> queryProductListAtlasByDiseaseName(@RequestParam(value = "diseaseName") String diseaseName) {
        return productService.queryProductListAtlasByDiseaseName(diseaseName);
    }

    @ApiOperation("根据一组商品code查询商品信息")
    @GetMapping("/v1/queryByProductCodes")
    @ApiImplicitParam(name = "codes",value = "codes:使用,分隔",dataType = "String", paramType = "query")
    public ComResponse<List<ProductMainDTO>> queryByProductCodes(@RequestParam @NotBlank String codes){
        return productService.queryByProductCodes(codes).setMessage(fastDFSConfig.getUrl());
    }

    @ApiOperation("根据商品编码获取库存")
    @GetMapping("/v1/getProductStock")
    @ApiImplicitParam(name = "productCode",value = "商品编码",dataType = "String", paramType = "query")
    public ComResponse<Integer> getProductStock(@RequestParam @NotBlank String productCode){
        return productService.getProductStock(productCode);
    }

}
