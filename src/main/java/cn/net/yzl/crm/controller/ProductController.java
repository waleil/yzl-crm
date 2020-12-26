package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.micservice.ProductMapperFeign;
import cn.net.yzl.crm.model.AttributeBean;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController implements ProductMapperFeign {

    @Autowired
    private ProductMapperFeign productMapperFeign;

    /**
     * 添加商品属性信息
     * @param attributeBean
     * @return
     */
    @PostMapping("insertAttribute")
    public ComResponse insertProductAttribute(@RequestBody AttributeBean attributeBean) {
        return productMapperFeign.insertProductAttribute(attributeBean);
    }

    /**
     * 分页查询商品属性列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("selectPageAttribute")
    public ComResponse selectPageAttribute(int pageNo, int pageSize) {
        return productMapperFeign.selectPageAttribute(pageNo,pageSize);
    }

    /**
     * 根据商品属性id查询
     * @param id
     * @return
     */
    @GetMapping("selectById")
    public ComResponse selectById(@RequestParam Integer id) {
        return productMapperFeign.selectById(id);
    }

    /**
     * 根据二级分类id进行查询
     * @param id
     * @return
     */
    @GetMapping("selectByclassifyIdAttribute")
    public ComResponse selectByclassifyIdAttribute(@RequestParam Integer id) {
        return productMapperFeign.selectByclassifyIdAttribute(id);
    }

    @PostMapping("updateAttribute")
    public ComResponse updateAttribute(@RequestBody AttributeBean attributeBean) {
        return productMapperFeign.updateAttribute(attributeBean);
    }

    @GetMapping("getCategoryByid")
    public ComResponse<CategoryTO> getCategoryByid(@RequestParam Integer id) {
        return productMapperFeign.getCategoryByid(id);
    }

    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody CategoryTO CategoryTO) {
        return productMapperFeign.insertCategory(CategoryTO);
    }

    @PutMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@RequestBody CategoryTO CategoryTO) {
        return productMapperFeign.updateCategory(CategoryTO);
    }

    @DeleteMapping("deleteCategory")
    public ComResponse<CategoryBean> deleteCategory(@RequestParam Integer id) {
        return productMapperFeign.deleteCategory(id);
    }

    @PutMapping("changeCategoryStatus")
    public ComResponse<CategoryBean> changeCategoryStatus(@RequestParam Integer flag,@RequestParam Integer id) {
        return productMapperFeign.changeCategoryStatus(flag,id);
    }

    @PutMapping("changeCategoryAppStatus")
    public ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam Integer flag,@RequestParam Integer id) {
        return productMapperFeign.changeCategoryAppStatus(flag,id);
    }

    @GetMapping("getCategoriesByPid")
    public ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam Integer pid) {
        return productMapperFeign.getCategoriesByPid(pid);
    }

    @PutMapping("transferCategories")
    public ComResponse<CategoryBean> transferCategories(@RequestParam Integer sourceId,@RequestParam Integer targetId) {
        return productMapperFeign.transferCategories(sourceId,targetId);
    }

    @GetMapping("getCategorySimpleTree")
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree() {
        return productMapperFeign.getCategorySimpleTree();
    }
}
