package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductVO;

import java.util.List;

public interface ProductService {
    /**
     * @Author: lichanghong
     * @Description: 查询上下架商品总数
     * @Date: 2021/1/7 9:59 下午
     * @Return:
     */
    ComResponse<List<ProductStatusCountDTO>> queryCountByStatus();

    ComResponse<Page<ProductListDTO>> queryListProduct(ProductSelectVO vo);
    /**
     * @Author: lichanghong
     * @Description: 编辑商品
     * @Date: 2021/1/7 9:59 下午
     * @Return:
     */
    ComResponse editProduct(ProductVO vo);
}
