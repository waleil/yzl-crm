package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.util.BeanUtil;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.dto.product.ProduceDto;
import cn.net.yzl.crm.service.CommonService;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.product.model.vo.product.dto.ProductDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDetailVO;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/16 23:12
 * @desc: //TODO  请说明该类的用途
 **/
@Service
public class CommonServiceImpl implements CommonService {


    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private ProductClient productClient;


    @Override
    public ComResponse<List<ProduceDto>> selectProduct() {
        // 培训过的商品
        ComResponse<List<String>> listComResponse = ehrStaffClient.selectProduct();
        if (null == listComResponse) {
            return null;
        }
        List<String> data = listComResponse.getData();
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        List<ProduceDto> produceDtoList = new ArrayList<>();
        data.forEach(productCode -> {
            ComResponse<ProductDetailVO> productDetail = productClient.queryProductDetail(productCode);
            if (null != productDetail) {
                ProduceDto produceDto = new ProduceDto();
                BeanUtil.copyProperties(productDetail, produceDto, true);
                produceDtoList.add(produceDto);
            }
        });
        return ComResponse.success(produceDtoList);
    }
}
