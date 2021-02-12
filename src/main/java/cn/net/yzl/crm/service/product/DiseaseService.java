package cn.net.yzl.crm.service.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.db.DiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import cn.net.yzl.product.model.vo.disease.dto.DiseaseTreePageDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDiseaseInfo;

import java.util.List;

public interface DiseaseService {

    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree(Boolean allowEmpty);

    ComResponse<Integer> insertDisease(DiseaseVo diseaseBean);

    ComResponse<Void> deleteDisease(DiseaseDelVo id);

    ComResponse<List<DiseaseDTO>> queryByPid(Integer pid);

    ComResponse<List<DiseaseBean>> selectAllDiseases();

    ComResponse<?> changeName(Integer id, String name, String userId);

    ComResponse<Page<DiseaseTreePageDTO>> queryDiseaseTreePage(int pageNo, int pageSize);

    ComResponse<List<ProductDiseaseInfo>> queryProductByDiseaseId(String name);
}
