package cn.net.yzl.crm.service.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.product.dto.*;
import cn.net.yzl.product.model.vo.product.vo.*;
import org.springframework.web.bind.annotation.RequestParam;

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
    ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(String productName, Integer id);

    ComResponse updateTimeByProductCode(ProductUpdateTimeVO vo);
    /**
     * @Author: lichanghong
     * @Description: 查询商品详情信息
     * @Date: 2021/1/9 13:00 下午
     * @param productCode
     * @Return:
     */
    ComResponse<ProductDetailVO> queryProductDetail(String productCode);
    /**
     * @Author: lichanghong
     * @Description: 查询商品画像
     * @Date: 2021/1/10 12:29 下午
     * @param productCode 商品编号
     * @Return:
     */
    ComResponse<ProductPortraitDTO> queryProductPortrait( String productCode);
    /**
     * @Author: lichanghong
     * @Description: 根据商品编号查询病症
     * @Date: 2021/1/10 4:03 下午
     * @param productCode
     * @Return:
     */
    ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(String productCode);
}
