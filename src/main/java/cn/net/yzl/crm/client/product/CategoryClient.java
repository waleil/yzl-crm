package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categoryClient",url = "${api.gateway.url}/productServer/category/v1")
public interface CategoryClient {

    @GetMapping("getById")
    ComResponse<Category> getCategoryById(@RequestParam("id") Integer id);

    @PostMapping("insert")
    ComResponse<Category> insertCategory(@RequestBody CategoryVO categoryVO);


    @PostMapping("update")
    ComResponse<Category> updateCategory(@RequestBody CategoryVO categoryVO);

    @PostMapping("delete")
    ComResponse<Category> deleteCategory(@RequestBody CategoryDelVO categoryDelVO);

    @PostMapping("changeStatus")
    ComResponse<Category> changeCategoryStatus(@RequestBody CategoryChangeStatusVO categoryChangeStatusVO);


    @PostMapping("changeAppStatus")
    ComResponse<Category> changeCategoryAppStatus(@RequestBody CategoryChangeStatusVO categoryChangeStatusVO);


    @GetMapping("getByPid")
    ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid);

    @GetMapping("queryPageByPid")
    ComResponse<Page<CategoryTO>> queryPageByPid(@RequestParam("pid") int pid,
                                                 @RequestParam("pageNo") Integer pageNo,
                                                 @RequestParam("pageSize") Integer pageSize);
    @GetMapping("query4SelectOption")
    ComResponse<List<CategorySelectTO>> query4SelectOption(@RequestParam(value ="pid") Integer pid);
}
