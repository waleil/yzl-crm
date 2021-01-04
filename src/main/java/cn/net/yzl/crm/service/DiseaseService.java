package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.DiseaseBean;
import cn.net.yzl.crm.model.DiseaseTreeNode;
import cn.net.yzl.crm.model.ProductDiseaseBean;

import java.util.List;

public interface DiseaseService {
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    ComResponse<Void> insertDisease(DiseaseBean diseaseBean);

    ComResponse<Void> deleteRelationOfDiseaseAndProduct(Integer did, String pCode);

    ComResponse<Void> deleteDisease(Integer id);

    ComResponse<Void> insertRelationOfDiseaseAndProduct(ProductDiseaseBean productDiseaseBean);
}
