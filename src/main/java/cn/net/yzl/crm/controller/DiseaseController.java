package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.DiseaseService;
import cn.net.yzl.product.model.db.DiseaseBean;
import cn.net.yzl.product.model.db.ProductDiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product/disease")
@Api(tags = "病症管理", description = "包含：增删改查")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    @ApiOperation(value = "获取病症信息的简单树结构，用于病症管理页面初始化")
    @GetMapping("getDiseaseSimpleTree")
    public ComResponse<List<DiseaseTreeNode>> getDiseaseSimpleTree() {
        return diseaseService.getDiseaseSimpleTree();
    }

    @ApiOperation(value = "新增病症")
    @PostMapping("insertDisease")
    public ComResponse<Void> insertDisease(@NotNull(message = "数据不能为空！") @RequestBody DiseaseBean diseaseBean) {
        return diseaseService.insertDisease(diseaseBean);
    }

    @ApiOperation(value = "删除产品和病症的关系")
    @GetMapping("deleteRelationOfDiseaseAndProduct")
    public ComResponse<Void> deleteRelationOfDiseaseAndProduct(@NotNull(message = "数据不能为空！") @RequestParam("did") @ApiParam("病症id") Integer did, @NotNull(message = "数据不能为空！") @RequestParam("pCode") @ApiParam("产品编号") String pCode) {
        return diseaseService.deleteRelationOfDiseaseAndProduct(did, pCode);
    }

    @ApiOperation(value = "删除病症")
    @GetMapping("deleteDisease")
    public ComResponse<Void> deleteDisease(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        return diseaseService.deleteDisease(id);
    }

    @ApiOperation(value = "新增病症与产品的关系")
    @PostMapping("insertRelationOfDiseaseAndProduct")
    public ComResponse<Void> insertRelationOfDiseaseAndProduct(@RequestBody @NotNull(message = "数据不能为空！") @ApiParam("病症与产品之间的关系映射") ProductDiseaseBean productDiseaseBean) {
        return diseaseService.insertRelationOfDiseaseAndProduct(productDiseaseBean);
    }

}
