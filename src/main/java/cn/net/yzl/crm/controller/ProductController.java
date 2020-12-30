package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.*;
import cn.net.yzl.crm.service.micservice.ProductMapperFeign;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value = "商品controller",tags = {"商品访问接口"})
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductMapperFeign productMapperFeign;

    /**
     * 添加商品属性信息
     * @param attributeBean
     * @return
     */
    @ApiOperation(value = "添加商品属性")
    @PostMapping("insertAttribute")
    public ComResponse insertProductAttribute(@RequestBody AttributeBean attributeBean) {
        return productMapperFeign.insertProductAttribute(attributeBean);
    }

    /**
     * 分页查询商品属性列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "通过页码和每页条数查询商品属性")
    @GetMapping("selectPageAttribute")
    public ComResponse selectPageAttribute(@RequestParam("pageNo") @NotNull(message = "页码信息不能为空！") int pageNo, @RequestParam("pageSize") @NotNull(message = "每页条数不能为空！") int pageSize) {
        return productMapperFeign.selectPageAttribute(pageNo,pageSize);
    }

    /**
     * 根据商品属性id查询
     * @param id
     * @return
     */
    @ApiOperation(value = "通过id查询商品属性")
    @GetMapping("selectById")
    public ComResponse selectById(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.selectById(id);
    }

    /**
     * 根据二级分类id进行查询
     * @param id
     * @return
     */
    @ApiOperation(value = "通过分类查询商品属性")
    @GetMapping("selectByclassifyIdAttribute")
    public ComResponse selectByclassifyIdAttribute(@RequestParam("id") @ApiParam("分类id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.selectByclassifyIdAttribute(id);
    }

    @ApiOperation(value = "修改商品属性")
    @PostMapping("updateAttribute")
    public ComResponse updateAttribute(@RequestBody AttributeBean attributeBean) {
        return productMapperFeign.updateAttribute(attributeBean);
    }

    @ApiOperation(value = "通过id精确匹配分类")
    @GetMapping("getCategoryById")
    public ComResponse<CategoryTO> getCategoryByid(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.getCategoryById(id);
    }
    @ApiOperation(value = "添加分类")
    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody CategoryTO CategoryTO) {
        return productMapperFeign.insertCategory(CategoryTO);
    }

    @ApiOperation(value = "修改分类信息")
    @PostMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@RequestBody CategoryTO CategoryTO) {
        return productMapperFeign.updateCategory(CategoryTO);
    }

    @ApiOperation(value = "删除分类信息")
    @GetMapping("deleteCategory")
    public ComResponse<CategoryBean> deleteCategory(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.deleteCategory(id);
    }

    @ApiOperation(value = "修改分类展示状态")
    @GetMapping( "changeCategoryStatus")
    public ComResponse<CategoryBean> changeCategoryStatus(@RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.changeCategoryStatus(flag,id);
    }

    @ApiOperation(value = "修改分类移动端展示状态")
    @GetMapping("changeCategoryAppStatus")
    public ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.changeCategoryAppStatus(flag,id);
    }

    @ApiOperation(value = "通过pid查询分类列表")
    @GetMapping("getCategoriesByPid")
    public ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid) {
        return productMapperFeign.getCategoriesByPid(pid);
    }

    @ApiOperation(value = "将一个分类下的商品全部转移到另一个分类下")
    @GetMapping("transferCategories")
    public ComResponse<CategoryBean> transferCategories(@RequestParam("sourceId") @ApiParam("源id") Integer sourceId,@RequestParam("targetId") @ApiParam("目标id") Integer targetId) {
        return productMapperFeign.transferCategories(sourceId,targetId);
    }

    @ApiOperation(value = "添加分类属性的简单树结构")
    @GetMapping("getCategorySimpleTree")
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree() {
        return productMapperFeign.getCategorySimpleTree();
    }

    @ApiOperation(value = "查询全部品牌")
    @GetMapping("getAllBrands")
    public ComResponse<PageInfo<BrandBeanTO>> getAllBrands(@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize) {
        return productMapperFeign.getAllBrands(pageNo, pageSize);
    }

    @ApiOperation(value = "通过id精确查询品牌")
    @GetMapping("getBrandById")
    public ComResponse<BrandBean> getBrandById(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.getBrandById(id);
    }

    @ApiOperation(value = "根据id查询该品牌下的所有商品")
    @GetMapping("getProductByBid")
    public ComResponse<List<BrandBean>> getProductByBid(@RequestParam("bid") Integer bid) {
        return productMapperFeign.getProductByBid(bid);
    }

    @ApiOperation(value = "修改品牌状态")
    @GetMapping("changeBrandStatus")
    public ComResponse<Void> changeBrandStatus(@RequestParam("flag") @ApiParam("是否启用（0禁用，1启用）") Integer flag, @RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.changeBrandStatus(flag, id);
    }

    @ApiOperation(value = "新增品牌")
    @PostMapping("insertBrand")
    public ComResponse<Void> insertBrand(@RequestBody BrandBean brand) {
        return productMapperFeign.insertBrand(brand);
    }

    @ApiOperation(value = "修改品牌")
    @PostMapping("updateBrand")
    public ComResponse<Void> updateBrand(@RequestBody BrandBean brandBean) {
        return productMapperFeign.updateBrand(brandBean);
    }

    @ApiOperation(value = "获取病症信息的简单树结构，用于病症管理页面初始化")
    @GetMapping("getDiseaseSimpleTree")
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree() {
        return productMapperFeign.getDiseaseSimpleTree();
    }

    @ApiOperation(value = "新增病症")
    @PostMapping("insertDisease")
    public ComResponse<Void> insertDisease(@RequestBody DiseaseBean diseaseBean) {
        return productMapperFeign.insertDisease(diseaseBean);
    }

    @ApiOperation(value = "删除产品和病症的关系")
    @GetMapping("deleteRelationOfDiseaseAndProduct")
    public ComResponse<Void> deleteRelationOfDiseaseAndProduct(@RequestParam("did") @ApiParam("病症id") Integer did,@RequestParam("pCode") @ApiParam("产品列表") String pCode) {
        return productMapperFeign.deleteRelationOfDiseaseAndProduct(did, pCode);
    }

    @ApiOperation(value = "删除病症")
    @GetMapping("deleteDisease")
    public ComResponse<Void> deleteDisease(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.deleteDisease(id);
    }

    @ApiOperation(value = "新增病症与产品的关系")
    @PostMapping("insertRelationOfDiseaseAndProduct")
    public ComResponse<Void> insertRelationOfDiseaseAndProduct(@RequestBody ProductDiseaseBean productDiseaseBean) {
        return productMapperFeign.insertRelationOfDiseaseAndProduct(productDiseaseBean);
    }

    @ApiOperation(value = "新增产品和图片的关系")
    @PostMapping("insertRelationOfProductAndImgUrl")
    public ComResponse<Void> insertRelationOfProductAndImgUrl(@RequestParam(value = "id") @ApiParam("当type=0时此处为产品编码，type=1时此处为套餐id") String id, @RequestParam(value = "imgId",required = false) Integer imgId, @RequestParam(value = "type",required = false) @ApiParam("0为产品，1为套餐") Integer type) {
        return productMapperFeign.insertRelationOfProductAndImgUrl(id, imgId, type);
    }

    @ApiOperation(value = "向逻辑图片库插入一条图片")
    @PostMapping("insertImage")
    public ComResponse<Integer> insertImage(@RequestParam("url") String url,@RequestParam("type") @ApiParam("1为图片，0为视频") Integer type) {
        return productMapperFeign.insertImage(url, type);
    }

    @ApiOperation(value = "删除产品和图片的关系")
    @GetMapping("deleteRelationOfProductAndImgId")
    public ComResponse<Void> deleteRelationOfProductAndImgId(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id,@RequestParam("type") Integer type) {
        return productMapperFeign.deleteRelationOfProductAndImgId(id, type);
    }
}
