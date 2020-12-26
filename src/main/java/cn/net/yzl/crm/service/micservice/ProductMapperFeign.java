package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.AttributeBean;
import cn.net.yzl.crm.model.CategoryBean;
import cn.net.yzl.crm.model.CategoryTO;
import cn.net.yzl.crm.model.CategoryTreeNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "productMapperFeign",url = "http://api.staff.yuzhilin.net.cn/productDB")
public interface ProductMapperFeign {

    /**
     * 添加商品属性信息
     * @param attributeBean
     * @return
     */
    @PostMapping("insertAttribute")
    public ComResponse insertProductAttribute(@RequestBody AttributeBean attributeBean);


    /**
     * 分页查询商品属性列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("selectPageAttribute")
    public ComResponse selectPageAttribute(@RequestParam int pageNo,@RequestParam int pageSize);


    /**
     * 根据商品属性id查询
     * @param id
     * @return
     */
    @GetMapping("selectById")
    public ComResponse selectById(@RequestParam Integer id);

    /**
     * 根据二级分类id进行查询
     * @param id
     * @return
     */
    @GetMapping("selectByclassifyIdAttribute")
    public ComResponse selectByclassifyIdAttribute(@RequestParam Integer id);


    @PostMapping("updateAttribute")
    public ComResponse updateAttribute(@RequestBody AttributeBean attributeBean);




    /*
     * @description 通过id字段对category_dict表进行条件检索
     * @author Majinbao
     * @date 2020/12/24 16:33
     * @param [pid]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @GetMapping("getCategoryByid")
    public ComResponse<CategoryTO> getCategoryByid(@RequestParam Integer id);

    /*
     * @description 通过接收请求的json参数对category_dict表进行插入操作
     * @author Majinbao
     * @date 2020/12/24 16:53
     * @param [categoryBean]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @PostMapping("insertCategory")
    public ComResponse<CategoryBean> insertCategory(@RequestBody CategoryTO CategoryTO);

    /*
     * @description 通过接受请求的json参数对category_dict表进行修改操作
     * @author Majinbao
     * @date 2020/12/24 17:03
     * @param [categoryBean]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @PutMapping("updateCategory")
    public ComResponse<CategoryBean> updateCategory(@RequestBody CategoryTO CategoryTO);

    /*
     * @description 通过主键对category_dict表进行逻辑删除
     * @author Majinbao
     * @date 2020/12/24 17:12
     * @param [id]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @DeleteMapping("deleteCategory")
    public ComResponse<CategoryBean> deleteCategory(@RequestParam Integer id);

    /*
     * @description 启用/禁用显示
     * @author Majinbao
     * @date 2020/12/24 17:26
     * @param [flag, id]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @PutMapping("changeCategoryStatus")
    public ComResponse<CategoryBean> changeCategoryStatus(@RequestParam Integer flag,@RequestParam Integer id);

    /*
     * @description 启用/禁用移动端显示
     * @author Majinbao
     * @date 2020/12/24 17:29
     * @param [flag, id]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @PutMapping("changeCategoryAppStatus")
    public ComResponse<CategoryBean> changeCategoryAppStatus(@RequestParam Integer flag,@RequestParam Integer id);

    /*
     * @description 根据父类id获取当前父级分类下所有子类
     * @author Majinbao
     * @date 2020/12/25 9:45
     * @param [pid]
     * @return cn.net.yzl.common.entity.ComResponse<java.util.List<cn.net.yzl.product.model.CategoryTO>>
     */
    @GetMapping("getCategoriesByPid")
    public ComResponse<List<CategoryTO>> getCategoriesByPid(@RequestParam Integer pid);

    /*
     * @description 根据源id和目标id实现分类下商品的全部转移功能
     * @author Majinbao
     * @date 2020/12/25 9:45
     * @param [sourceId, targetId]
     * @return cn.net.yzl.common.entity.ComResponse<cn.net.yzl.product.model.CategoryBean>
     */
    @PutMapping("transferCategories")
    public ComResponse<CategoryBean> transferCategories(@RequestParam Integer sourceId,@RequestParam Integer targetId);

    /*
     * @description 查询一个树状的结果集展示所有分类信息
     * @author Majinbao
     * @date 2020/12/25 15:47
     * @param []
     * @return cn.net.yzl.common.entity.ComResponse<java.util.List<cn.net.yzl.product.model.CategoryTreeNode>>
     */
    @GetMapping("getCategorySimpleTree")
    public ComResponse<List<CategoryTreeNode>> getCategorySimpleTree();

}
