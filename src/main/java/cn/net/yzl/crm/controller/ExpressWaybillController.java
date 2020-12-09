package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.dto.express.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = ExpressWaybillController.PATH)
public class ExpressWaybillController {
    public static final String PATH = "express/waybill";

    @ApiOperation(value="查询快递单列表")
    @PostMapping("list")
    public GeneralResult<List<ExpressWaybillResDTO>> list() {
        ExpressWaybillResDTO d =  new ExpressWaybillResDTO();
        return GeneralResult.success(Collections.singletonList(d));
    }

    @ApiOperation(value="查询快递单轨迹信息")
    @PostMapping("getWaybillTraceList")
    public GeneralResult<DemoExpressWaybillTraceResDTO> delete(@RequestParam("id")
                                                       @NotBlank(message="快递单id不能为空")
                                                       @ApiParam(name="id",value="快递公司id",required=true)  String id) {
        return GeneralResult.success(new DemoExpressWaybillTraceResDTO());
    }


    @ApiOperation(value="查询快递运单详情")
    @PostMapping("getById")
    public GeneralResult<ExpressWaybillResDTO> getById( @RequestParam("id")
                                                        @NotBlank(message="快递单id不能为空")
                                                        @ApiParam(name="id",value="快递单id",required=true)  String id) {
        ExpressWaybillResDTO d =  new ExpressWaybillResDTO();
        return GeneralResult.success(d);
    }


    @ApiOperation(value="签收补登")
    @PostMapping("save")
    public GeneralResult<Boolean> save(@RequestBody ExpressWaybillSaveDTO dto) {
        return GeneralResult.success(Boolean.TRUE);
    }


}
