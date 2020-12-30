package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.model.*;
import cn.net.yzl.crm.service.micservice.ProductMapperFeign;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

@Api(value = "商品controller",tags = {"商品访问接口"})
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductMapperFeign productMapperFeign;
    @Autowired
    private FastdfsUtils fastdfsUtils;
    /**
     * 添加商品属性信息
     * @param attributeBean
     * @return
     */
    @ApiOperation(value = "添加商品属性")
    @PostMapping("insertAttribute")
    public ComResponse insertProductAttribute(@NotNull(message = "数据不能为空！")@RequestBody AttributeBean attributeBean) {
        if (attributeBean.getId() == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.SYSTEM_ERROR_CODE.getMessage());
        }else {
            return productMapperFeign.insertProductAttribute(attributeBean);
        }
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
        if (pageSize>50) {
            pageSize=50;
        }
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
        ComResponse comResponse = productMapperFeign.selectById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    /**
     * 根据二级分类id进行查询
     * @param id
     * @return
     */
    @ApiOperation(value = "通过分类查询商品属性")
    @GetMapping("selectByclassifyIdAttribute")
    public ComResponse selectByclassifyIdAttribute(@RequestParam("id") @ApiParam("分类id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = productMapperFeign.selectByclassifyIdAttribute(id);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "修改商品属性")
    @PostMapping("updateAttribute")
    public ComResponse updateAttribute(@NotNull(message = "数据不能为空！")@RequestBody AttributeBean attributeBean) {
        return productMapperFeign.updateAttribute(attributeBean);
    }

    @ApiOperation(value = "通过id精确匹配分类")
    @GetMapping("getCategoryById")
    public ComResponse<CategoryTO> getCategoryById(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = productMapperFeign.getCategoryById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }
    @ApiOperation(value = "添加分类")
    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody @NotNull(message = "数据不能为空！") CategoryTO CategoryTO) {
        return productMapperFeign.insertCategory(CategoryTO);
    }

    @ApiOperation(value = "修改分类信息")
    @PostMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@NotNull(message = "数据不能为空！") @RequestBody CategoryTO CategoryTO) {
        return productMapperFeign.updateCategory(CategoryTO);
    }

    @ApiOperation(value = "删除分类信息")
    @GetMapping("deleteCategory")
    public ComResponse<CategoryBean> deleteCategory(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.deleteCategory(id);
    }

    @ApiOperation(value = "修改分类展示状态")
    @GetMapping( "changeCategoryStatus")
    public ComResponse<CategoryBean> changeCategoryStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return productMapperFeign.changeCategoryStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "修改分类移动端展示状态")
    @GetMapping("changeCategoryAppStatus")
    public ComResponse<CategoryBean> changeCategoryAppStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return productMapperFeign.changeCategoryAppStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "通过pid查询分类列表")
    @GetMapping("getCategoriesByPid")
    public ComResponse<List<CategoryTO>> getCategoriesByPid(@NotNull(message = "数据不能为空！") @RequestParam("pid") Integer pid) {
        ComResponse comResponse = productMapperFeign.getCategoriesByPid(pid);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "将一个分类下的商品全部转移到另一个分类下")
    @GetMapping("transferCategories")
    public ComResponse<CategoryBean> transferCategories(@NotNull(message = "数据不能为空！") @RequestParam("sourceId") @ApiParam("源id") Integer sourceId,@NotNull(message = "数据不能为空！") @RequestParam("targetId") @ApiParam("目标id") Integer targetId) {
        return productMapperFeign.transferCategories(sourceId,targetId);
    }

    @ApiOperation(value = "添加分类属性的简单树结构")
    @GetMapping("getCategorySimpleTree")
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree() {
        return productMapperFeign.getCategorySimpleTree();
    }

    @ApiOperation(value = "查询全部品牌")
    @GetMapping("getAllBrands")
    public ComResponse<PageInfo<BrandBeanTO>> getAllBrands(@NotNull(message = "数据不能为空！") @RequestParam("pageNo") Integer pageNo,@NotNull(message = "数据不能为空！") @RequestParam("pageSize") Integer pageSize) {
        if (pageSize>50) {
            pageSize=50;
        }
        return productMapperFeign.getAllBrands(pageNo, pageSize);
    }

    @ApiOperation(value = "通过id精确查询品牌")
    @GetMapping("getBrandById")
    public ComResponse<BrandBean> getBrandById(@NotNull(message = "数据不能为空！") @RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = productMapperFeign.getBrandById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "根据id查询该品牌下的所有商品")
    @GetMapping("getProductByBid")
    public ComResponse<List<ProductBean>> getProductByBid(@NotNull(message = "数据不能为空！") @RequestParam("bid") Integer bid) {
        ComResponse comResponse = productMapperFeign.getProductByBid(bid);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "修改品牌状态")
    @GetMapping("changeBrandStatus")
    public ComResponse<Void> changeBrandStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否启用（0禁用，1启用）") Integer flag, @RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return productMapperFeign.changeBrandStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "新增品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer") })
    @PostMapping("insertBrand")
    public ComResponse insertBrand(MultipartFile file, HttpServletRequest request,
                                  String name, String descri, int sort) {
        try {
            BrandBean brand = new BrandBean();
            if(file!=null){
                long size = file.getSize() / 1024; //kb
                if (size > 50) { //判断图片大小 单位Kb
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片过大,保证在50Kb下");
                }
                String fileName = file.getOriginalFilename();
                if(!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png格式文件");
                }
                StorePath storePath = fastdfsUtils.upload(file);
                String filePath = storePath.getFullPath();
                brand.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brand.setCreateNo(userId);
            brand.setName(name);
            brand.setDescri(descri);
            brand.setSort(sort);
            return productMapperFeign.insertBrand(brand);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }

    @ApiOperation(value = "修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "品牌LOGO", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "name", value = "品牌名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "descri", value = "品牌故事", required = false, dataType = "String"),
            @ApiImplicitParam(name = "brandId", value = "品牌编号", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "Integer") })
    @PostMapping("updateBrand")
    public ComResponse<Void> updateBrand(MultipartFile file, HttpServletRequest request,
                                         String name, String descri, int sort,int brandId) {
        try {
            if(brandId<=0){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数错误!");
            }
            BrandBean brand = productMapperFeign.getBrandById(brandId).getData();
            if(brand==null){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"品牌不存在!");
            }
            if(file!=null){
                long size = file.getSize() / 1024; //kb
                if (size > 50) { //判断图片大小 单位Kb
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"图片过大,保证在50Kb下");
                }
                String fileName = file.getOriginalFilename();
                if(!fileName.endsWith(".jpg")&&!fileName.endsWith(".png")){
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"只能上传jpg/png格式文件");
                }
                if(StringUtils.isNotEmpty(brand.getBrandUrl())){
                    fastdfsUtils.delete(brand.getBrandUrl());
                }
                StorePath storePath = fastdfsUtils.upload(file);
                String filePath = storePath.getFullPath();
                brand.setBrandUrl(filePath);
            }
            String userId = request.getHeader("userId");
            brand.setCreateNo(userId);
            brand.setName(name);
            brand.setDescri(descri);
            brand.setSort(sort);
            return productMapperFeign.updateBrand(brand);
        } catch (IOException e) {
            e.printStackTrace();
            return ComResponse.fail(0,"添加失败");
        }
    }

    @ApiOperation(value = "获取病症信息的简单树结构，用于病症管理页面初始化")
    @GetMapping("getDiseaseSimpleTree")
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree() {
        return productMapperFeign.getDiseaseSimpleTree();
    }

    @ApiOperation(value = "新增病症")
    @PostMapping("insertDisease")
    public ComResponse<Void> insertDisease(@NotNull(message = "数据不能为空！") @RequestBody DiseaseBean diseaseBean) {
        return productMapperFeign.insertDisease(diseaseBean);
    }

    @ApiOperation(value = "删除产品和病症的关系")
    @GetMapping("deleteRelationOfDiseaseAndProduct")
    public ComResponse<Void> deleteRelationOfDiseaseAndProduct(@NotNull(message = "数据不能为空！") @RequestParam("did") @ApiParam("病症id") Integer did,@NotNull(message = "数据不能为空！") @RequestParam("pCode") @ApiParam("产品编号") String pCode) {
        return productMapperFeign.deleteRelationOfDiseaseAndProduct(did, pCode);
    }

    @ApiOperation(value = "删除病症")
    @GetMapping("deleteDisease")
    public ComResponse<Void> deleteDisease(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return productMapperFeign.deleteDisease(id);
    }

    @ApiOperation(value = "新增病症与产品的关系")
    @PostMapping("insertRelationOfDiseaseAndProduct")
    public ComResponse<Void> insertRelationOfDiseaseAndProduct(@RequestBody @NotNull(message = "数据不能为空！") @ApiParam("病症与产品之间的关系映射") ProductDiseaseBean productDiseaseBean) {
        return productMapperFeign.insertRelationOfDiseaseAndProduct(productDiseaseBean);
    }

    @ApiOperation(value = "新增产品和图片的关系")
    @PostMapping("insertRelationOfProductAndImgUrl")
    public ComResponse<Void> insertRelationOfProductAndImgUrl(@NotNull(message = "数据不能为空！") @RequestParam(value = "id") @ApiParam("当type=0时此处为产品编码，type=1时此处为套餐id") String id, @NotNull(message = "数据不能为空！") @RequestParam(value = "imgId") Integer imgId, @NotNull(message = "数据不能为空！") @RequestParam(value = "type") @ApiParam("0为产品，1为套餐") Integer type) {
        if(type==0||type==1){
            productMapperFeign.insertRelationOfProductAndImgUrl(id, imgId, type);
            return ComResponse.success();
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }

    }

    @ApiOperation(value = "向逻辑图片库插入一条图片")
    @PostMapping("insertImage")
    public ComResponse<Integer> insertImage(@NotNull(message = "数据不能为空！") @RequestParam("url") String url,@NotNull(message = "数据不能为空！") @RequestParam("type") @ApiParam("1为图片，0为视频") Integer type) {
        if(type==0||type==1){
            return productMapperFeign.insertImage(url,type);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "删除产品和图片的关系")
    @GetMapping("deleteRelationOfProductAndImgId")
    public ComResponse<Void> deleteRelationOfProductAndImgId(@NotNull(message = "数据不能为空！") @RequestParam("id") @NotNull(message = "id不能为空！") Integer id,@NotNull(message = "数据不能为空！") @RequestParam("type") Integer type) {
        if (type == 1||type == 0) {
            return productMapperFeign.deleteRelationOfProductAndImgId(id, type);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

}
