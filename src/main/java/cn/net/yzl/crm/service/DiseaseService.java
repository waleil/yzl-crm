package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.ProductDiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;

import javax.validation.Valid;
import java.util.List;

public interface DiseaseService {

    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    ComResponse<Void> insertDisease(DiseaseVo diseaseBean);

    ComResponse<Void> deleteDisease(DiseaseDelVo id);

    List<DiseaseDTO> queryByPid(Integer pid);
}
