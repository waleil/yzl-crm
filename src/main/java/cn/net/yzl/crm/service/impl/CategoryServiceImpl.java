package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.CategoryClient;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.service.CategoryService;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryClient categoryClient;

    @Override
    public ComResponse getCategoryById(Integer id) {
        return categoryClient.getCategoryById(id);
    }

    @Override
    public ComResponse<CategoryBean> insertCategory(CategoryVO categoryVO) {
        return categoryClient.insertCategory(categoryVO);
    }

    @Override
    public ComResponse<CategoryBean> updateCategory(CategoryVO categoryVO) {
        return categoryClient.updateCategory(categoryVO);
    }

    @Override
    public ComResponse<CategoryBean> deleteCategory(Integer id) {
        return categoryClient.deleteCategory(id);
    }

    @Override
    public ComResponse<CategoryBean> changeCategoryStatus(Integer flag, Integer id) {
        return categoryClient.changeCategoryStatus(flag, id);
    }

    @Override
    public ComResponse<CategoryBean> changeCategoryAppStatus(Integer flag, Integer id) {
        return categoryClient.changeCategoryAppStatus(flag, id);
    }

    @Override
    public ComResponse getCategoriesByPid(Integer pid) {
        return categoryClient.getCategoriesByPid(pid);
    }

    @Override
    public ComResponse<CategoryBean> transferCategories(Integer sourceId, Integer targetId) {
        return categoryClient.transferCategories(sourceId, targetId);
    }

    @Override
    public ComResponse<List<Category>> selectAll() {
        return categoryClient.selectAll();
    }
}
