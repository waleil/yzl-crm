package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.ProductClient;
import cn.net.yzl.crm.service.ProductService;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductStatusCountDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
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
}
