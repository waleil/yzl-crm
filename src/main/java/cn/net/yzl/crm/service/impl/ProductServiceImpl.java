package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public void insertRelationOfProductAndImgUrl(String id, Integer imgId, Integer type) {
        
    }

    @Override
    public ComResponse<Integer> insertImage(String url, Integer type) {
        return null;
    }

    @Override
    public ComResponse<Void> deleteRelationOfProductAndImgId(Integer id, Integer type) {
        return null;
    }
}
