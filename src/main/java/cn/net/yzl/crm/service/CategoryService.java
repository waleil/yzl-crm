package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryDelVO;
import cn.net.yzl.product.model.vo.category.CategorySelectTO;
import cn.net.yzl.product.model.vo.category.CategoryTO;
import cn.net.yzl.product.model.vo.category.CategoryVO;

import java.util.List;


public interface CategoryService {
    ComResponse getCategoryById(Integer id);

    ComResponse<CategoryBean> insertCategory(CategoryTO categoryTO);

    ComResponse<CategoryBean> updateCategory(CategoryTO categoryTO);

    ComResponse<CategoryBean> deleteCategory(CategoryDelVO categoryDelVO);

    ComResponse<CategoryBean> changeCategoryStatus(Boolean flag, Integer id,String uid);

    ComResponse<CategoryBean> changeCategoryAppStatus(Boolean flag, Integer id,String uid);

    ComResponse getCategoriesByPid(Integer pid);

    ComResponse<Page<CategoryTO>> selectAll(Integer pid, Integer pageNo, Integer pageSize);

    ComResponse<List<CategorySelectTO>> selectForOptions(Integer pid);
}
