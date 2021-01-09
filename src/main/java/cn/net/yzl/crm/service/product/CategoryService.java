package cn.net.yzl.crm.service.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryDelVO;
import cn.net.yzl.product.model.vo.category.CategorySelectTO;
import cn.net.yzl.product.model.vo.category.CategoryTO;

import java.util.List;


public interface CategoryService {
    ComResponse getCategoryById(Integer id);

    ComResponse<Category> insertCategory(CategoryTO categoryTO);

    ComResponse<Category> updateCategory(CategoryTO categoryTO);

    ComResponse<Category> deleteCategory(CategoryDelVO categoryDelVO);

    ComResponse<Category> changeCategoryStatus(Boolean flag, Integer id,String uid);

    ComResponse<Category> changeCategoryAppStatus(Boolean flag, Integer id,String uid);

    ComResponse getCategoriesByPid(Integer pid);

    ComResponse<Page<CategoryTO>> selectAll(Integer pid, Integer pageNo, Integer pageSize);

    ComResponse<List<CategorySelectTO>> selectForOptions(Integer pid);
}
