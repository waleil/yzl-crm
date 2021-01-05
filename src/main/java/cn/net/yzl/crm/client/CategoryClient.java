package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categoryClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/category")
public interface CategoryClient {

    @GetMapping("/v1/category/selectById")
    ComResponse<CategoryTO> getCategoryById(@RequestParam("id") Integer id);

    @PostMapping("/v1/category/insert")
    ComResponse<CategoryBean> insertCategory(@RequestBody CategoryTO CategoryTO);


    @PutMapping("/v1/category/update")
    ComResponse<CategoryBean> updateCategory(@RequestBody CategoryTO CategoryTO);

    @DeleteMapping("/v1/category/delete")
    ComResponse<CategoryBean> deleteCategory(@RequestParam("id") Integer id);

    @PutMapping("/v1/category/changeStatus")
    ComResponse<CategoryBean> changeCategoryStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @PutMapping("/v1/category/changeAppStatus")
    ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @GetMapping("/v1/category/selectByPid")
    ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid);


    @PutMapping("/v1/category/transfer")
    ComResponse<CategoryBean> transferCategories(@RequestParam("sourceId") Integer sourceId,@RequestParam("targetId") Integer targetId);


    @GetMapping("/v1/category/getSimpleTree")
    ComResponse<List<CategoryTreeNode>> getCategorySimpleTree();

}
