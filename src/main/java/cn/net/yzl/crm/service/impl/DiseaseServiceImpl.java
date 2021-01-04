package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.DiseaseBean;
import cn.net.yzl.crm.model.DiseaseTreeNode;
import cn.net.yzl.crm.model.ProductDiseaseBean;
import cn.net.yzl.crm.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {
    @Override
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree() {
        return null;
    }

    @Override
    public ComResponse<Void> insertDisease(DiseaseBean diseaseBean) {
        return null;
    }

    @Override
    public ComResponse<Void> deleteRelationOfDiseaseAndProduct(Integer did, String pCode) {
        return null;
    }

    @Override
    public ComResponse<Void> deleteDisease(Integer id) {
        return null;
    }

    @Override
    public ComResponse<Void> insertRelationOfDiseaseAndProduct(ProductDiseaseBean productDiseaseBean) {
        return null;
    }
}
