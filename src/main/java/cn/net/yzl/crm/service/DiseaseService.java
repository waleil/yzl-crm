package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.DiseaseBean;
import cn.net.yzl.product.model.db.ProductDiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;

import java.util.List;

public interface DiseaseService {
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    ComResponse<Void> insertDisease(DiseaseBean diseaseBean);

    ComResponse<Void> deleteRelationOfDiseaseAndProduct(Integer did, String pCode);

    ComResponse<Void> deleteDisease(Integer id);

    ComResponse<Void> insertRelationOfDiseaseAndProduct(ProductDiseaseBean productDiseaseBean);
}
