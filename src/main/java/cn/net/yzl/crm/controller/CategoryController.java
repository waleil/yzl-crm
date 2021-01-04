package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;
import cn.net.yzl.crm.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product/category")
@Api(tags = "商品分类管理", description = "包含：增删改查，树化查询")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "通过id精确匹配分类")
    @GetMapping("getCategoryById")
    public ComResponse<CategoryTO> getCategoryById(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = categoryService.getCategoryById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }
    @ApiOperation(value = "添加分类")
    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody @NotNull(message = "数据不能为空！") CategoryTO CategoryTO) {
        return categoryService.insertCategory(CategoryTO);
    }

    @ApiOperation(value = "修改分类信息")
    @PostMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@NotNull(message = "数据不能为空！") @RequestBody CategoryTO CategoryTO) {
        return categoryService.updateCategory(CategoryTO);
    }

    @ApiOperation(value = "删除分类信息")
    @GetMapping("deleteCategory")
    public ComResponse<CategoryBean> deleteCategory(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return categoryService.deleteCategory(id);
    }

    @ApiOperation(value = "修改分类展示状态")
    @GetMapping( "changeCategoryStatus")
    public ComResponse<CategoryBean> changeCategoryStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return categoryService.changeCategoryStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "修改分类移动端展示状态")
    @GetMapping("changeCategoryAppStatus")
    public ComResponse<CategoryBean> changeCategoryAppStatus(@NotNull(message = "数据不能为空！") @RequestParam("flag") @ApiParam("是否展示，0为不展示，1为展示") Integer flag,@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        if (flag == 1||flag==0) {
            return categoryService.changeCategoryAppStatus(flag,id);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "通过pid查询分类列表")
    @GetMapping("getCategoriesByPid")
    public ComResponse<List<CategoryTO>> getCategoriesByPid(@NotNull(message = "数据不能为空！") @RequestParam("pid") Integer pid) {
        ComResponse comResponse = categoryService.getCategoriesByPid(pid);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "将一个分类下的商品全部转移到另一个分类下")
    @GetMapping("transferCategories")
    public ComResponse<CategoryBean> transferCategories(@NotNull(message = "数据不能为空！") @RequestParam("sourceId") @ApiParam("源id") Integer sourceId, @NotNull(message = "数据不能为空！") @RequestParam("targetId") @ApiParam("目标id") Integer targetId) {
        return categoryService.transferCategories(sourceId,targetId);
    }

    @ApiOperation(value = "添加分类属性的简单树结构")
    @GetMapping("getCategorySimpleTree")
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree() {
        return categoryService.getCategorySimpleTree();
    }

}
