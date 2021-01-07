package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.CategoryClient;
import cn.net.yzl.crm.service.CategoryService;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.*;
import org.springframework.beans.BeanUtils;
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
    public ComResponse<Category> insertCategory(CategoryTO categoryTO) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(categoryTO, categoryVO);
        return categoryClient.insertCategory(categoryVO);
    }

    @Override
    public ComResponse<Category> updateCategory(CategoryTO categoryTO) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(categoryTO, categoryVO);
        return categoryClient.updateCategory(categoryVO);
    }

    @Override
    public ComResponse<Category> deleteCategory(CategoryDelVO categoryDelVO) {
        return categoryClient.deleteCategory(categoryDelVO);
    }

    @Override
    public ComResponse<Category> changeCategoryStatus(Boolean flag, Integer id,String uid) {
        CategoryChangeStatusVO categoryChangeStatusVO = new CategoryChangeStatusVO();
        categoryChangeStatusVO.setId(id);
        categoryChangeStatusVO.setUpdateNo(uid);
        categoryChangeStatusVO.setStatus(flag);
        return categoryClient.changeCategoryStatus(categoryChangeStatusVO);
    }

    @Override
    public ComResponse<Category> changeCategoryAppStatus(Boolean flag, Integer id,String uid) {
        CategoryChangeStatusVO categoryChangeStatusVO = new CategoryChangeStatusVO();
        categoryChangeStatusVO.setId(id);
        categoryChangeStatusVO.setUpdateNo(uid);
        categoryChangeStatusVO.setStatus(flag);
        return categoryClient.changeCategoryAppStatus(categoryChangeStatusVO);
    }

    @Override
    public ComResponse getCategoriesByPid(Integer pid) {
        return categoryClient.getCategoriesByPid(pid);
    }

    @Override
    public ComResponse<Page<CategoryTO>> selectAll(Integer pid, Integer pageNo, Integer pageSize) {
        return categoryClient.queryPageByPid(pid, pageNo, pageSize);
    }

    @Override
    public ComResponse<List<CategorySelectTO>> selectForOptions(Integer pid) {
        return categoryClient.query4SelectOption(pid);
    }


}
