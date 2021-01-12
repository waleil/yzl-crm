package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import cn.net.yzl.product.model.vo.disease.dto.DiseaseTreePageDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "diseaseClient",url = "${api.gateway.url}/productServer/disease")
public interface DiseaseClient {

    @GetMapping("v1/queryTreeNode")
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree();

    @PostMapping("v1/insert")
    ComResponse<Integer> insertDisease(@RequestBody DiseaseVo diseaseVo);

    @PostMapping("v1/deleteById")
    ComResponse<Void> deleteDisease(@RequestBody DiseaseDelVo diseaseDelVo);

    @GetMapping("v1/selectAll")
    ComResponse selectAllDiseases();

    @GetMapping("v1/queryByPID")
    ComResponse<List<DiseaseDTO>> queryByPID(@RequestParam("pid") Integer pid);

    @GetMapping("v1/changeName")
    ComResponse changeName(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("userId") String userId);

    @GetMapping("v1/queryDiseasePage")
    ComResponse<Page<DiseaseTreePageDTO>> queryDiseaseTreePage(@RequestParam(value = "pageNo") Integer pageNo,
                                                               @RequestParam(value = "pageSize") Integer pageSize);
}
