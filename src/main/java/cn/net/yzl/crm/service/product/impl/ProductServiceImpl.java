package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.service.product.ProductService;
import cn.net.yzl.product.model.vo.product.dto.*;
import cn.net.yzl.product.model.vo.product.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductClient productClient;
    @Override
    public ComResponse<List<ProductStatusCountDTO>> queryCountByStatus() {
        return productClient.queryCountByStatus();
    }

    @Override
    public ComResponse<Page<ProductListDTO>> queryListProduct(ProductSelectVO vo) {
        return productClient.queryListProduct(vo);
    }

    @Override
    public ComResponse editProduct(ProductVO vo) {
        return productClient.editProduct(vo);
    }

    @Override
    public ComResponse updateStatusByProductCode(ProductUpdateStatusVO vo) {
        return productClient.updateStatusByProductCode(vo);
    }

    @Override
    public ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(String productName, Integer id,Integer pid) {
        return productClient.queryProductListAtlas(productName,id,pid);
    }

    @Override
    public ComResponse updateTimeByProductCode(ProductUpdateTimeVO vo) {
        return productClient.updateTimeByProductCode(vo);
    }

    @Override
    public ComResponse<ProductDetailVO> queryProductDetail(String productCode) {
        return productClient.queryProductDetail(productCode);
    }

    @Override
    public ComResponse<ProductPortraitDTO> queryProductPortrait(String productCode) {
        return productClient.queryProductPortrait(productCode);
    }

    @Override
    public ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(String productCode) {
        return productClient.queryDiseaseByProductCode(productCode);
    }

}
