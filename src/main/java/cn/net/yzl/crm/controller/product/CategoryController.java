package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.product.CategoryService;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryDelVO;
import cn.net.yzl.product.model.vo.category.CategorySelectTO;
import cn.net.yzl.product.model.vo.category.CategoryTO;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product/v1/category")
@Api(tags = "商品分类管理", description = "包含：增删改查，树化查询")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FastDFSConfig fastDFSConfig;


    @ApiOperation(value = "通过id精确匹配分类")
    @GetMapping("getCategoryById")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query",required = true)
    public ComResponse<Category> getCategoryById(@RequestParam("id") Integer id) {
        if(id == null || id < 1){
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"不合法的id类型！");
        }
        ComResponse<Category> comResponse = categoryService.getCategoryById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse.setMessage(fastDFSConfig.getUrl());
    }
    @ApiOperation(value = "添加分类")
    @PostMapping("insertCategory")
    public ComResponse<?> insertCategory(@RequestBody @Valid CategoryTO categoryTO,HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(userId)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户登录信息，请尝试重新登陆!");
        }
        categoryTO.setUpdateNo(userId);
        return categoryService.insertCategory(categoryTO);
    }

    @ApiOperation(value = "修改分类信息")
    @PostMapping("updateCategory")
    public ComResponse<?> updateCategory(@RequestBody @Valid CategoryTO categoryTO,HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(userId)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户登录信息，请尝试重新登陆!");
        }
        categoryTO.setUpdateNo(userId);
        return categoryService.updateCategory(categoryTO);
    }

    @ApiOperation(value = "删除分类信息")
    @GetMapping("deleteCategory")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query",required = true)
    public ComResponse<?> deleteCategory(@RequestParam("id") Integer id, HttpServletRequest request) {
        CategoryDelVO categoryDelVO = new CategoryDelVO();
        categoryDelVO.setUpdateNo(request.getHeader("userId"));
        categoryDelVO.setId(id);
        return categoryService.deleteCategory(categoryDelVO);
    }

    @ApiOperation(value = "修改分类展示状态")
    @GetMapping( "changeCategoryStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true),
            @ApiImplicitParam(name = "flag", value = "是否展示（true：展示，false：不展示）",required = true,paramType = "query")
    })
    public ComResponse<?> changeCategoryStatus(@RequestParam("flag") Boolean flag,
                                                          @RequestParam("id") Integer id,
                                                          HttpServletRequest request) {
            if (flag == null) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"请正确输入上下架信息！");
            }
            if(id == null || id < 1){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"非法的id类型！");
            }
            String userId = request.getHeader("userId");
            if(StringUtils.isBlank(userId)){
                return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"无法获取用户登录信息，请尝试重新登陆!");
            }
            return categoryService.changeCategoryStatus(flag,id,userId);
    }

    @ApiOperation(value = "修改分类移动端展示状态")
    @GetMapping("changeCategoryAppStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true),
            @ApiImplicitParam(name = "flag", value = "是否展示（true：展示，false：不展示）",required = true,paramType = "query")
    })
    public ComResponse<?> changeCategoryAppStatus(@RequestParam("flag") Boolean flag,
                                                             @RequestParam("id") Integer id,
                                                             HttpServletRequest request) {


            return categoryService.changeCategoryAppStatus(flag,id,request.getHeader("userId"));
    }

    @ApiOperation(value = "通过pid查询分类列表")
    @GetMapping("getCategoriesByPid")
    @ApiImplicitParam(name = "pid",value = "根据父类id查询该父类下所有子类，如果需要查询一级分类，则输入0",paramType = "query",required = true)
    public ComResponse<List<CategoryTO>> getCategoriesByPid( @RequestParam("pid") Integer pid) {
        ComResponse comResponse = categoryService.getCategoriesByPid(pid);
        List<?> list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List<?>) comResponse.getData()).size() == 0) {
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
    public ComResponse<?> transferCategories(@RequestParam("sourceId")Integer sourceId, @RequestParam("targetId") Integer targetId) {
        return null;
    }

    @ApiOperation(value = "根据pid分页查询分类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid",value = "父类id，如果需要查询一级分类，此处输入0",paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query", required = true)
    })
    @GetMapping("selectPage")
    public ComResponse<Page<CategoryTO>> selectAll(@RequestParam Integer pid, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        if (pid == null || pid < 0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"非法的id类型！");
        }
        if (pageNo == null || pageNo < 0) {
            pageNo = 1;
        }
        if (pageSize <= 0){
            pageSize = 10;
        }
        return categoryService.selectAll(pid,pageNo, pageSize);
    }

    @ApiOperation("提供给前端下拉列表的查询接口")
    @GetMapping("selectForOptions")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid",value = "父类id，如果需要查询一级分类，此处输入0",paramType = "query", required = true),
            @ApiImplicitParam(name = "type",value = "如不需要过滤空值，则在此输入'pid'",paramType = "query", required = false)
    })
    public ComResponse<List<CategorySelectTO>> selectForOptions(@RequestParam("pid")Integer pid,
                                                                @RequestParam(value = "type",required = false)String type){
        if (pid == null || pid < 0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"非法的id类型！");
        }
        return categoryService.selectForOptions(pid,type);
    }

 }
