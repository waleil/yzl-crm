package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.CategoryClient;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.service.CategoryService;
import cn.net.yzl.product.model.db.Category;
import cn.net.yzl.product.model.vo.category.CategoryChangeStatusVO;
import cn.net.yzl.product.model.vo.category.CategorySelectTO;
import cn.net.yzl.product.model.vo.category.CategoryTO;
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
    public ComResponse<CategoryBean> changeCategoryStatus(Integer flag, Integer id,String uid) {
        CategoryChangeStatusVO categoryChangeStatusVO = new CategoryChangeStatusVO();
        categoryChangeStatusVO.setId(id);
        categoryChangeStatusVO.setUpdateNo(uid);
        if (flag == null||flag>1||flag<0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE,"参数超出取值范围！");
        }
        categoryChangeStatusVO.setStatus(flag == 1);
        return categoryClient.changeCategoryStatus(categoryChangeStatusVO);
    }

    @Override
    public ComResponse<CategoryBean> changeCategoryAppStatus(Integer flag, Integer id,String uid) {
        CategoryChangeStatusVO categoryChangeStatusVO = new CategoryChangeStatusVO();
        categoryChangeStatusVO.setId(id);
        categoryChangeStatusVO.setUpdateNo(uid);
        if (flag == null||flag>1||flag<0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE,"参数超出取值范围！");
        }
        categoryChangeStatusVO.setStatus(flag == 1);
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
