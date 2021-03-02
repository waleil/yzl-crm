package cn.net.yzl.crm.service.product.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.DiseaseClient;
import cn.net.yzl.crm.service.product.DiseaseService;
import cn.net.yzl.product.model.db.DiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import cn.net.yzl.product.model.vo.disease.dto.DiseaseTreePageDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDiseaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    @Autowired
    private DiseaseClient client;

    @Override
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree(Boolean allowEmpty) {
        return client.getDiseaseSimpleTree(allowEmpty);
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
        //查询，不允许空一级病症
        return client.queryByPID(pid,false);
    }

    @Override
    public ComResponse<List<DiseaseBean>> selectAllDiseases() {
        return client.selectAllDiseases();
    }

    @Override
    public ComResponse<?> changeName(Integer id, String name, String userId) {
        return client.changeName(id, name, userId);
    }

    @Override
    public ComResponse<Page<DiseaseTreePageDTO>> queryDiseaseTreePage(int pageNo, int pageSize) {
        return client.queryDiseaseTreePage(pageNo,pageSize);
    }

    @Override
    public ComResponse<List<ProductDiseaseInfo>> queryProductByDiseaseId(String name) {
        return client.queryProductByDiseaseId(name);
    }
    @Override
    public ComResponse<List<ProductDiseaseInfo>> queryProductByDiseaseNameAndMemberCard(String name, String memberCard) {
        return client.queryProductByDiseaseNameAndMemberCard(name,memberCard);
    }

    @Override
    public ComResponse queryTreeNodeWithTemp(String memberCard, String userId) {
        return client.queryTreeNodeWithTemp(memberCard, userId);
    }
}
