package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.product.AttributeService;
import cn.net.yzl.product.model.db.AttributeBean;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {
    @Override
    public ComResponse updateAttribute(AttributeBean attributeBean) {
        return null;
    }

    @Override
    public ComResponse insertProductAttribute(AttributeBean attributeBean) {
        return null;
    }

    @Override
    public ComResponse selectPageAttribute(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public ComResponse selectById(Integer id) {
        return null;
    }

    @Override
    public ComResponse selectByclassifyIdAttribute(Integer id) {
        return null;
    }
}
