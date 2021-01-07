package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "diseaseClient",url = "${api.gateway.url}/productServer/disease")
public interface DiseaseClient {

    @GetMapping("/v1/queryTreeNode")
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    @PostMapping("/v1/insert")
    ComResponse<Void> insertDisease(@RequestBody DiseaseVo diseaseVo);

    @PostMapping("/v1/deleteById")
    ComResponse<Void> deleteDisease(@RequestBody DiseaseDelVo delVo);
    @GetMapping("v1/queryByPID")
    ComResponse<List<DiseaseDTO>> queryByPID(@RequestParam(value = "pid") Integer pid);
}
