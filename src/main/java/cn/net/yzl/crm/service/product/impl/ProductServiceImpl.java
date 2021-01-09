package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.service.product.ProductService;
import cn.net.yzl.product.model.vo.product.dto.ProductAtlasDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateStatusVO;
import cn.net.yzl.product.model.vo.product.vo.ProductUpdateTimeRequestVO;
import cn.net.yzl.product.model.vo.product.vo.ProductVO;
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
    public ComResponse<List<ProductAtlasDTO>> queryProductListAtlas(String productName, Integer id) {
        return productClient.queryProductListAtlas(productName,id);
    }

    @Override
    public ComResponse updateTimeByProductCode(ProductUpdateTimeRequestVO vo) {
        return productClient.updateTimeByProductCode(vo);
    }

}
