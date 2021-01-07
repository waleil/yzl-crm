package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.DiseaseClient;
import cn.net.yzl.crm.model.DiseaseBean;
import cn.net.yzl.crm.model.DiseaseTreeNode;
import cn.net.yzl.crm.model.ProductDiseaseBean;
import cn.net.yzl.crm.service.DiseaseService;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product/disease")
@Api(tags = "病症管理", description = "包含：增删改查")
public class DiseaseController {

    @Autowired
    private DiseaseClient diseaseService;
    @ApiOperation("新增病症")
    @PostMapping("v1/insert")
    public ComResponse insertDisease(@RequestBody @Valid DiseaseVo diseaseVo) {
        return diseaseService.insertDisease(diseaseVo);
    }
    @ApiOperation("删除病症")
    @PostMapping("v1/deleteById")
    public ComResponse deleteDisease(@RequestBody @Valid DiseaseDelVo delVo) {
        return diseaseService.deleteDisease(delVo);
    }

    @ApiOperation("查询树形结构，包含商品信息")
    @GetMapping("v1/queryTreeNode")
    public ComResponse queryTreeNode(){
        return diseaseService.getDiseaseSimpleTree();
    }
    /**
     * @author lichanghong
     * @description 查询病症，主要针对下拉列表
     * @date: 2021/1/6 2:08 下午
     * @param pid:
     * @return: ComResponse<List<DiseaseDTO>>
     */
    @ApiOperation("下拉列表根据父级编号查询")
    @GetMapping("v1/queryByPID")
    public ComResponse<List<DiseaseDTO>> queryByPID(@RequestParam(value = "pid",defaultValue = "0",required = false) Integer pid){
        return diseaseService.queryByPID(pid);
    }


}
