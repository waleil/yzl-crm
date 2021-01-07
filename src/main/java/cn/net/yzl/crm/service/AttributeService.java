package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.AttributeBean;
import org.springframework.stereotype.Service;

@Service
public interface AttributeService {
    ComResponse updateAttribute(AttributeBean attributeBean);

    ComResponse insertProductAttribute(AttributeBean attributeBean);

    ComResponse selectPageAttribute(int pageNo, int pageSize);

    ComResponse selectById(Integer id);

    ComResponse selectByclassifyIdAttribute(Integer id);
}
