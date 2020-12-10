package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;

import cn.net.yzl.common.entity.GeneralResult;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.PageDTO;
import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.dto.region.ProvinceTSaveDTO;
import cn.net.yzl.crm.model.ProvinceT;
import cn.net.yzl.crm.service.ProvinceTService;
import cn.net.yzl.crm.sys.BizException;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;


@RestController
@RequestMapping(value = ProvinceController.PATH)
public class ProvinceController {
    public static final String PATH = "region/province";

    @Autowired
    private ProvinceTService service;

    @ApiOperation(value="分页查询省份列表")
    @PostMapping("listPage")
    public GeneralResult<PageInfo<ProvinceTResDTO>> listPage(@RequestBody PageDTO page) {
        Map<String, Object> params = new HashMap<>();
        BeanUtil.copyProperties(page, params);
        PageInfo<ProvinceTResDTO> result = service.findPage(params);
        return GeneralResult.success(result);
    }

    @ApiOperation(value="删除")
    @PostMapping("deleteById")
    public GeneralResult<Boolean> deleteById(@RequestParam("id")
                                                 @NotBlank(message="省id不能为空")
                                                 @ApiParam(name="id",value="省id",required=true)  Integer id) {
       //这个应该是假删除，需要加个是否删除的状态
        return GeneralResult.success(Boolean.TRUE);
    }


    @ApiOperation(value="查询省份详情")
    @PostMapping("getById")
    public GeneralResult<ProvinceTResDTO> getById(@RequestParam("id")
                                                           @NotBlank(message="省id不能为空")
                                                           @ApiParam(name="id",value="省id",required=true)  Integer id) {
        Optional<ProvinceTResDTO> byId = service.getById(id);
        ProvinceTResDTO resDTO = byId.orElseThrow(() -> new BizException(ResponseCodeEnums.NO_DATA_CODE));
        return GeneralResult.success(resDTO);
    }


    @ApiOperation(value="新增-保存")
    @PostMapping("save")
    public GeneralResult<Boolean> save(@RequestBody ProvinceTSaveDTO dto) {
        ProvinceT p = new ProvinceT();
        BeanUtil.copyProperties(dto,p,true);
        service.insert(p);
        return GeneralResult.success(Boolean.TRUE);
    }

}
