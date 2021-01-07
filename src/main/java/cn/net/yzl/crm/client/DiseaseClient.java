package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.DiseaseBean;
import cn.net.yzl.crm.model.DiseaseTreeNode;
import cn.net.yzl.crm.model.ProductDiseaseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "diseaseClient",url = "${api.gateway.url}/productServer/disease")
public interface DiseaseClient {

    @GetMapping("/v1/disease/selectSimpleTree")
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    @PostMapping("/v1/disease/insert")
    ComResponse<Void> insertDisease(@RequestBody DiseaseBean diseaseBean);

    @DeleteMapping("/v1/disease/deleteDiseaseProduct")
    ComResponse<Void> deleteRelationOfDiseaseAndProduct(@RequestParam("did") Integer did, @RequestParam("pCode") String pCode);

    @DeleteMapping("/v1/disease/deleteById")
    ComResponse<Void> deleteDisease(@RequestParam("id") Integer id);

    @PostMapping("/v1/disease/insertDiseaseProduct")
    ComResponse<Void> insertRelationOfDiseaseAndProduct(@RequestBody ProductDiseaseBean productDiseaseBean);

    @PostMapping("/v1/disease/insertProductImgId")
    ComResponse<Void> insertRelationOfProductAndImgUrl(@RequestParam(value = "id",required = false) String id,
                                                       @RequestParam(value = "imgId",required = false)Integer imgId,
                                                       @RequestParam(value = "type",required = false)Integer type);

}
