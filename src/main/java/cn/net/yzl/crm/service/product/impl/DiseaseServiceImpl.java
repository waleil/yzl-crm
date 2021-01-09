package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.product.DiseaseClient;
import cn.net.yzl.crm.service.product.DiseaseService;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ComResponse<Integer> insertDisease(DiseaseVo diseaseBean) {
        return client.insertDisease(diseaseBean);
    }

    @Override
    public ComResponse<Void> deleteDisease(DiseaseDelVo id) {
        return client.deleteDisease(id);
    }

    @Override
    public ComResponse<List<DiseaseDTO>> queryByPid(Integer pid) {
        return client.queryByPID(pid);
    }

    @Override
    public ComResponse selectAllDiseases() {
        return client.selectAllDiseases();
    }

    @Override
    public ComResponse changeName(Integer id, String name, String userId) {
        return client.changeName(id, name, userId);
    }
}
