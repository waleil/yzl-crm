package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.ReturnReceiptFeginService;
import cn.net.yzl.model.dto.RefuseReturnDto;
import cn.net.yzl.model.dto.ReturnReceiptDetailDto;
import cn.net.yzl.model.dto.ReturnReceiptPageDto;
import cn.net.yzl.model.dto.ReturnToStoreAddDto;
import cn.net.yzl.model.vo.ReturnReceiptCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "仓储中心心心心心-退货入库管理", tags = {"仓储中心心心心心-退货入库管理"})
@RequestMapping("returnReceipt")
@RestController
public class ReturnReceiptController {

    @Autowired
    private ReturnReceiptFeginService returnReceiptFeginService;

    /**
     * 确认收货
     * @param addList
     * @return
     */
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @PostMapping("v1/confirm/receipt")
    public ComResponse confirmReceipt(@RequestBody List<ReturnToStoreAddDto> addList){
        return returnReceiptFeginService.confirmReceipt(addList);
    }

    /**
     * 拒绝收货
     * @param refuseReturnDto
     * @return
     */
    @ApiOperation(value = "拒绝收货", notes = "拒绝收货")
    @PostMapping("v1/refuse/return")
    public ComResponse refuseReturn(@RequestBody RefuseReturnDto refuseReturnDto){
        return returnReceiptFeginService.refuseReturn(refuseReturnDto);
    }


    /**
     * 退货入库单详情
     * @param id
     * @return
     */
    @ApiOperation(value = "退货入库单详情", notes = "退货入库单详情")
    @ApiImplicitParam(name = "id", value = "采购订单id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/detail")
    public ComResponse<ReturnReceiptDetailDto> detail(@RequestParam("id") Integer id){
        return returnReceiptFeginService.detail(id);
    }



    /**
     * 退货入库单详情
     * @param returnReceiptCondition
     * @return
     */
    @ApiOperation(value = "退货入库单列表", notes = "退货入库单列表")
    @PostMapping("v1/page")
    public ComResponse<Page<ReturnReceiptPageDto>> page(@RequestBody ReturnReceiptCondition returnReceiptCondition){
        return returnReceiptFeginService.page(returnReceiptCondition);
    }

    /**
     * 退货入库单类型列表
     * @param
     * @return
     */
    @ApiOperation(value = "退货入库单类型列表")
    @GetMapping("v1/type/list")
    public ComResponse<List<Map<String,Object>>> returnTypeList() {
        return returnReceiptFeginService.returnTypeList();
    }
}
