package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.product.ProductService;
import cn.net.yzl.product.model.vo.product.dto.*;
import cn.net.yzl.product.model.vo.product.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private FastDFSConfig fastDFSConfig;
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
        return productClient.queryProductDetail(productCode).setMessage(fastDFSConfig.getUrl());
    }

    @Override
    public ComResponse<ProductPortraitDTO> queryProductPortrait(String productCode) {
        return productClient.queryProductPortrait(productCode);
    }

    @Override
    public ComResponse<List<ProductDiseaseDTO>> queryDiseaseByProductCode(String productCode) {
        return productClient.queryDiseaseByProductCode(productCode);
    }

    @Override
    public ComResponse<List<ProductMainInfoDTO>> queryProducts(String ids) {
        return productClient.queryProducts(ids);
    }

}
