package cn.net.yzl.crm.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import cn.net.yzl.product.model.vo.disease.dto.DiseaseTreePageDTO;
import cn.net.yzl.product.model.vo.product.dto.DiseaseMainInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "diseaseClient",url = "${api.gateway.url}/productServer/disease/v1")
public interface DiseaseClient {

    @GetMapping("queryTreeNode")
    ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree(@RequestParam("allowEmpty") Boolean allowEmpty);

    @PostMapping("insert")
    ComResponse<Integer> insertDisease(@RequestBody DiseaseVo diseaseVo);

    @PostMapping("deleteById")
    ComResponse<Void> deleteDisease(@RequestBody DiseaseDelVo diseaseDelVo);

    @GetMapping("selectAll")
    ComResponse selectAllDiseases();

    @GetMapping("queryByPID")
    ComResponse<List<DiseaseDTO>> queryByPID(@RequestParam("pid") Integer pid);

    @GetMapping("changeName")
    ComResponse<?> changeName(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("userId") String userId);

    @GetMapping("queryDiseasePage")
    ComResponse<Page<DiseaseTreePageDTO>> queryDiseaseTreePage(@RequestParam(value = "pageNo") Integer pageNo,
                                                               @RequestParam(value = "pageSize") Integer pageSize);



    @GetMapping("queryHierarchy")
    ComResponse<List<DiseaseMainInfo>> queryHierarchy(@RequestParam("ids") String ids);

    @GetMapping("artificialSeatInput")
    ComResponse<DiseaseMainInfo> artificialSeatInput(@RequestParam("pid") Integer pid ,@RequestParam("name")String name,@RequestParam("userId")String userId);
}
