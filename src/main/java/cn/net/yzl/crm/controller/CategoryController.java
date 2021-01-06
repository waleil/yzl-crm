package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.service.CategoryService;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product/v1/category")
@Api(tags = "商品分类管理", description = "包含：增删改查，树化查询")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "通过id精确匹配分类")
    @GetMapping("getCategoryById")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query",required = true)
    public ComResponse<CategoryTO> getCategoryById(@RequestParam("id") Integer id) {
        ComResponse comResponse = categoryService.getCategoryById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }
    @ApiOperation(value = "添加分类")
    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody @Valid CategoryVO categoryVO) {
        return categoryService.insertCategory(categoryVO);
    }

    @ApiOperation(value = "修改分类信息")
    @PostMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@RequestBody @Valid CategoryVO categoryVO) {
        return categoryService.updateCategory(categoryVO);
    }

    @ApiOperation(value = "删除分类信息")
    @GetMapping("deleteCategory")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query",required = true)
    public ComResponse<CategoryBean> deleteCategory(@RequestParam("id") Integer id) {
        return categoryService.deleteCategory(id);
    }

    @ApiOperation(value = "修改分类展示状态")
    @GetMapping( "changeCategoryStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true),
            @ApiImplicitParam(name = "flag", value = "是否展示（1：展示，0：不展示）",required = true,paramType = "query")
    })
    public ComResponse<CategoryBean> changeCategoryStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id) {
        if (flag == 1||flag==0) {
            return categoryService.changeCategoryStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "修改分类移动端展示状态")
    @GetMapping("changeCategoryAppStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true),
            @ApiImplicitParam(name = "flag", value = "是否展示（1：展示，0：不展示）",required = true,paramType = "query")
    })
    public ComResponse<CategoryBean> changeCategoryAppStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return categoryService.changeCategoryAppStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "通过pid查询分类列表")
    @GetMapping("getCategoriesByPid")
    @ApiImplicitParam(name = "pid",value = "根据父类id查询该父类下所有子类，如果需要查询一级分类，则输入0",paramType = "query",required = true)
    public ComResponse<List<CategoryTO>> getCategoriesByPid( @RequestParam("pid") Integer pid) {
        ComResponse comResponse = categoryService.getCategoriesByPid(pid);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "将一个分类下的商品全部转移到另一个分类下(已废弃)")
    @GetMapping("transferCategories")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "源id", paramType = "query", required = true),
            @ApiImplicitParam(name = "targetId", value = "目标id",required = true,paramType = "query")
    })
    public ComResponse<CategoryBean> transferCategories(@RequestParam("sourceId")Integer sourceId, @RequestParam("targetId") Integer targetId) {
        return categoryService.transferCategories(sourceId,targetId);
    }

    @ApiOperation(value = "查询全部品牌")
    @GetMapping("selectAll")
    public ComResponse<List<Category>> selectAll() {
        return categoryService.selectAll();
    }

}
