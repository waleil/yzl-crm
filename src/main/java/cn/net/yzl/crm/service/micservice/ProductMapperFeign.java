package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "productMapperFeign",url = "http://api.staff.yuzhilin.net.cn")
//@FeignClient("yzl-product-mapper")
public interface ProductMapperFeign {

    /**
     * 添加商品属性信息
     * @param attributeBean
     * @return
     */
    @PostMapping("productApi/insertAttribute")
    ComResponse insertProductAttribute(@RequestBody AttributeBean attributeBean);


    /**
     * 分页查询商品属性列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("productApi/selectPageAttribute")
    ComResponse selectPageAttribute(@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize);


    /**
     * 根据商品属性id查询
     * @param id
     * @return
     */
    @GetMapping("productApi/selectById")
    ComResponse selectById(@RequestParam("id") Integer id);

    /**
     * 根据二级分类id进行查询
     * @param id
     * @return
     */
    @GetMapping("productApi/selectByclassifyIdAttribute")
    ComResponse selectByclassifyIdAttribute(@RequestParam("id") Integer id);


    @PutMapping("productApi/updateAttribute")
    ComResponse updateAttribute(@RequestBody AttributeBean attributeBean);

    @GetMapping("productApi/getCategoryById")
    ComResponse<CategoryTO> getCategoryById(@RequestParam("id") Integer id);

    @PostMapping("productApi/insertCategory")
    ComResponse<CategoryBean> insertCategory(@RequestBody CategoryTO CategoryTO);


    @PutMapping("productApi/updateCategory")
    ComResponse<CategoryBean> updateCategory(@RequestBody CategoryTO CategoryTO);

    @DeleteMapping("productApi/deleteCategory")
    ComResponse<CategoryBean> deleteCategory(@RequestParam("id") Integer id);

    @PutMapping("productApi/changeCategoryStatus")
    ComResponse<CategoryBean> changeCategoryStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @PutMapping("productApi/changeCategoryAppStatus")
    ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @GetMapping("productApi/getCategoriesByPid")
    ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid);


    @PutMapping("productApi/transferCategories")
    ComResponse<CategoryBean> transferCategories(@RequestParam("sourceId") Integer sourceId,@RequestParam("targetId") Integer targetId);


    @GetMapping("productApi/getCategorySimpleTree")
    ComResponse<List<CategoryTreeNode>> getCategorySimpleTree();

    @ApiOperation(value = "获取所有品牌信息")
    @GetMapping("productApi/getAllBrands")
    ComResponse<PageInfo<BrandBeanTO>> getAllBrands(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("productApi/getBrandById")
    ComResponse<BrandBean> getBrandById(@RequestParam("id") Integer id);

    @GetMapping("productApi/getProductByBid")
    ComResponse<List<BrandBean>> getProductByBid(@RequestParam("bid") Integer bid);

    @PutMapping("productApi/changeBrandStatus")
    ComResponse<Void> changeBrandStatus(@RequestParam("flag") Integer flag, @RequestParam("id") Integer id);

    @PostMapping("productApi/insertBrand")
    ComResponse<Void> insertBrand(@RequestBody BrandBean brand);

    @PutMapping("productApi/updateBrand")
    ComResponse<Void> updateBrand(@RequestBody BrandBean brandBean);

    @GetMapping("productApi/getDiseaseSimpleTree")
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    @PostMapping("productApi/insertDisease")
    ComResponse<Void> insertDisease(@RequestBody DiseaseBean diseaseBean);

    @DeleteMapping("productApi/deleteRelationOfDiseaseAndProduct")
    ComResponse<Void> deleteRelationOfDiseaseAndProduct(@RequestParam("did") Integer did, @RequestParam("pCode") String pCode);

    @DeleteMapping("productApi/deleteDisease")
    ComResponse<Void> deleteDisease(@RequestParam("id") Integer id);

    @PostMapping("productApi/insertRelationOfDiseaseAndProduct")
    ComResponse<Void> insertRelationOfDiseaseAndProduct(@RequestBody ProductDiseaseBean productDiseaseBean);

    @PostMapping("productApi/insertRelationOfProductAndImgId")
    ComResponse<Void> insertRelationOfProductAndImgUrl(@RequestParam(value = "id",required = false) String id,
                                                              @RequestParam(value = "imgId",required = false)Integer imgId,
                                                              @RequestParam(value = "type",required = false)Integer type);

    @PostMapping("productApi/insertImage")
    ComResponse<Integer> insertImage(@RequestParam("url")String url,@RequestParam("type") Integer type);

    @DeleteMapping("productApi/deleteRelationOfProductAndImgId")
    ComResponse<Void> deleteRelationOfProductAndImgId(@RequestParam("id")Integer id,
                                                             @RequestParam("type")Integer type);

}
