package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.*;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categoryClient",url = "${api.gateway.url}/productServer/category")
public interface CategoryClient {

    @GetMapping("/v1/getById")
    ComResponse<CategoryTO> getCategoryById(@RequestParam("id") Integer id);

    @PostMapping("/v1/insert")
    ComResponse<CategoryBean> insertCategory(@RequestBody CategoryVO categoryVO);


    @PostMapping("/v1/update")
    ComResponse<CategoryBean> updateCategory(@RequestBody CategoryVO categoryVO);

    @PostMapping("/v1/delete")
    ComResponse<CategoryBean> deleteCategory(@RequestBody CategoryDelVO categoryDelVO);

    @PostMapping("/v1/changeStatus")
    ComResponse<CategoryBean> changeCategoryStatus(@RequestBody CategoryChangeStatusVO categoryChangeStatusVO);


    @PostMapping("/v1/changeAppStatus")
    ComResponse<CategoryBean> changeCategoryAppStatus(@RequestBody CategoryChangeStatusVO categoryChangeStatusVO);


    @GetMapping("/v1/getByPid")
    ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid);

    @GetMapping("/v1/queryPageByPid")
    ComResponse<Page<CategoryTO>> queryPageByPid(@RequestParam("pid") int pid,
                                                 @RequestParam("pageNo") Integer pageNo,
                                                 @RequestParam("pageSize") Integer pageSize);
    @GetMapping("/v1/query4SelectOption")
    ComResponse<List<CategorySelectTO>> query4SelectOption(@RequestParam(value ="pid") Integer pid);
}
