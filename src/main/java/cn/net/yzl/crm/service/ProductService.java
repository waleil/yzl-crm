package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.DiseaseBean;

import java.util.List;

public interface ProductService {
    void insertRelationOfProductAndImgUrl(String id, Integer imgId, Integer type);

    ComResponse<Integer> insertImage(String url, Integer type);

    ComResponse<Void> deleteRelationOfProductAndImgId(Integer id, Integer type);
}
