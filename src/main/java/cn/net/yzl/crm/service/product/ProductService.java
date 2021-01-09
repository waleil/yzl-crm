package cn.net.yzl.crm.service.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.ProductAtlasDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateStatusVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateTimeRequestVO;
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
    /**
     * @Author: lichanghong
     * @Description: 修改商品上下架状态
     * @Date: 2021/1/8 9:25 下午
     * @param vo
     * @Return:
     */
    ComResponse updateStatusByProductCode(ProductUpdateStatusVO vo);

    /**
     * 查询商品图谱
     * 商品图谱
     * @param productName 商品名称(模糊查询)
     * @param id 病症id
     * @return
     */
    ComResponse<ProductAtlasDTO> queryProductListAtlas(String productName, Integer id);

    ComResponse updateTimeByProductCode(ProductUpdateTimeRequestVO vo);
}
