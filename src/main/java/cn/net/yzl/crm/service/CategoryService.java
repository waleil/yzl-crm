package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;

import java.util.List;

public interface CategoryService {
    ComResponse getCategoryById(Integer id);

    ComResponse<CategoryBean> insertCategory(CategoryTO categoryTO);

    ComResponse<CategoryBean> updateCategory(CategoryTO categoryTO);

    ComResponse<CategoryBean> deleteCategory(Integer id);

    ComResponse<CategoryBean> changeCategoryStatus(Integer flag, Integer id);

    ComResponse<CategoryBean> changeCategoryAppStatus(Integer flag, Integer id);

    ComResponse getCategoriesByPid(Integer pid);

    ComResponse<CategoryBean> transferCategories(Integer sourceId, Integer targetId);

    ComResponse<List<CategoryTreeNode>> getCategorySimpleTree();
}
