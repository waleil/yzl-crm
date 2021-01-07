package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.DiseaseService;
import cn.net.yzl.product.model.db.DiseaseBean;
import cn.net.yzl.product.model.db.ProductDiseaseBean;
import cn.net.yzl.product.model.vo.disease.DiseaseDTO;
import cn.net.yzl.product.model.vo.disease.DiseaseDelVo;
import cn.net.yzl.product.model.vo.disease.DiseaseTreeNode;
import cn.net.yzl.product.model.vo.disease.DiseaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product/disease")
@Api(tags = "病症管理", description = "包含：增删改查")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    @ApiOperation("【未完成商品关联】查询树形结构，包含商品信息")
    @GetMapping("v1/queryTreeNode")
    public ComResponse queryTreeNode() {
        return diseaseService.getDiseaseSimpleTree();
    }

    @ApiOperation("新增病症")
    @PostMapping("v1/insert")
    public ComResponse insertDisease(@RequestBody @Valid DiseaseVo diseaseVo,HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"无法获取用户id，请检查您的登录状态");
        }
        diseaseVo.setUpdateNo(userId);
        return diseaseService.insertDisease(diseaseVo);
    }

    @ApiOperation(value = "删除病症")
    @GetMapping("delete")
    @ApiImplicitParam(name = "id",value = "id",required = true,paramType = "query")
    public ComResponse deleteDisease(@RequestParam("id") Integer id, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(userId)){
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE,"无法获取用户id，请检查您的登录状态");
        }
        //新建删除实体
        DiseaseDelVo delVo = new DiseaseDelVo();
        delVo.setUpdateNo(request.getHeader("userId"));
        delVo.setId(id);
        return diseaseService.deleteDisease(delVo);
    }

    @ApiOperation("根据父级编号查询病症")
    @GetMapping("v1/queryByPid")
    @ApiImplicitParam(name = "pid",value = "父类id,默认为0（查询一级病症）",paramType = "query")
    public ComResponse<List<DiseaseDTO>> queryByPID(@RequestParam(value = "pid",defaultValue = "0",required = false) Integer pid){
        ComResponse<List<DiseaseDTO>> listComResponse = diseaseService.queryByPid(pid);
        if (listComResponse.getData().size() == 0){
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE,"当前父类下无子类病症");
        }
        return listComResponse;
    }

    @ApiOperation("查询所有病症")
    @GetMapping("v1/selectAll")
    public ComResponse selectAllDiseases(){
        return diseaseService.selectAllDiseases();
    }

}
