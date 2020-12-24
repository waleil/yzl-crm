package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.dto.express.ExpressCompanyResDTO;
import cn.net.yzl.crm.dto.express.ExpressCompanySaveDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = ExpressCompanyController.PATH)
public class ExpressCompanyController {
    public static final String PATH = "express/expresscompany";

    @ApiOperation(value="查询快递公司列表")
    @PostMapping("list")
    public GeneralResult<List<ExpressCompanyResDTO>> list() {
        ExpressCompanyResDTO d =  new ExpressCompanyResDTO();
        return GeneralResult.success(Collections.singletonList(d));
    }

    @ApiOperation(value="删除快递公司")
    @PostMapping("delete")
    public GeneralResult<Boolean> delete(@RequestParam("id")
                                                       @NotBlank(message="快递公司id不能为空")
                                                       @ApiParam(name="id",value="快递公司id",required=true)  String id) {
        return GeneralResult.success(Boolean.TRUE);
    }

    @ApiOperation(value="查询快递公司详情")
    @PostMapping("getById")
    public GeneralResult<ExpressCompanyResDTO> getById( @RequestParam("id")
                                                        @NotBlank(message="快递公司id不能为空")
                                                        @ApiParam(name="id",value="快递公司id",required=true)  String id) {
        ExpressCompanyResDTO d =  new ExpressCompanyResDTO();
        return GeneralResult.success(d);
    }


    @ApiOperation(value="保存")
    @PostMapping("save")
    public GeneralResult<Boolean> save(@RequestBody ExpressCompanySaveDTO dto) {
        return GeneralResult.success(Boolean.TRUE);
    }
    @ApiOperation(value="更新")
    @PostMapping("update")
    public GeneralResult<Boolean> update(@RequestBody ExpressCompanySaveDTO dto) {
        return GeneralResult.success(Boolean.TRUE);
    }

    @ApiOperation(value="账户信息")
    @PostMapping("getAccountInfo")
    public GeneralResult<Boolean> getAccountInfo( @RequestParam("id")
                                                      @NotBlank(message="快递公司id不能为空")
                                                      @ApiParam(name="id",value="快递公司id",required=true)  String id) {
        return GeneralResult.success(Boolean.TRUE);
    }

    @ApiOperation(value="接口信息")
    @PostMapping("getInterfaceInfo")
    public GeneralResult<Boolean> getInterfaceInfo(@RequestParam("id")
                                                       @NotBlank(message="快递公司id不能为空")
                                                       @ApiParam(name="id",value="快递公司id",required=true)  String id) {
        return GeneralResult.success(Boolean.TRUE);
    }

}
