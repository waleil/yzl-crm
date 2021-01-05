package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;
import cn.net.yzl.crm.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public ComResponse getCategoryById(Integer id) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> insertCategory(CategoryTO categoryTO) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> updateCategory(CategoryTO categoryTO) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> deleteCategory(Integer id) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> changeCategoryStatus(Integer flag, Integer id) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> changeCategoryAppStatus(Integer flag, Integer id) {
        return null;
    }

    @Override
    public ComResponse getCategoriesByPid(Integer pid) {
        return null;
    }

    @Override
    public ComResponse<CategoryBean> transferCategories(Integer sourceId, Integer targetId) {
        return null;
    }

    @Override
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree() {
        return null;
    }
}
