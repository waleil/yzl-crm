package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categoryClient",url = "http://api.staff.yuzhilin.net.cn/productServer/productServer/category")
public interface CategoryClient {

    @GetMapping("/v1/getById")
    ComResponse<CategoryTO> getCategoryById(@RequestParam("id") Integer id);

    @PostMapping("/v1/insert")
    ComResponse<CategoryBean> insertCategory(@RequestBody CategoryVO categoryVO);


    @PutMapping("/v1/update")
    ComResponse<CategoryBean> updateCategory(@RequestBody CategoryVO categoryVO);

    @DeleteMapping("/v1/delete")
    ComResponse<CategoryBean> deleteCategory(@RequestParam("id") Integer id);

    @PutMapping("/v1/changeStatus")
    ComResponse<CategoryBean> changeCategoryStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @PutMapping("/v1/changeAppStatus")
    ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam("flag") Integer flag,@RequestParam("id") Integer id);


    @GetMapping("/v1/getByPid")
    ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam("pid") Integer pid);


    @PutMapping("/v1/transfer")
    ComResponse<CategoryBean> transferCategories(@RequestParam("sourceId") Integer sourceId,@RequestParam("targetId") Integer targetId);

    @GetMapping("/v1/selectAll")
    ComResponse<List<Category>> selectAll();
}
