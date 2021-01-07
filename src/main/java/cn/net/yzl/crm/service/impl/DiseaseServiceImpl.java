package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.DiseaseClient;
import cn.net.yzl.crm.service.DiseaseService;
import cn.net.yzl.product.model.db.ProductDiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    @Autowired
    private DiseaseClient client;

    @Override
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree() {
        return client.getDiseaseSimpleTree();
    }

    @Override
    public ComResponse<Void> insertDisease(DiseaseVo diseaseBean) {
        return client.insertDisease(diseaseBean);
    }

    @Override
    public ComResponse<Void> deleteDisease(DiseaseDelVo id) {
        return client.deleteDisease(id);
    }

    @Override
    public ComResponse<Void> insertRelationOfDiseaseAndProduct(ProductDiseaseBean productDiseaseBean) {
        return null;
    }

    @Override
    public List<DiseaseDTO> queryByPid(Integer pid) {
        return null;
    }
}
