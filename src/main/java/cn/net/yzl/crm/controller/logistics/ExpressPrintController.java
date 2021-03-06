package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.logistics.model.vo.print.SFPrintResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("logistics")
@Api(tags = "电子面单")
public class ExpressPrintController {

    @Autowired
    LogisticsFien logisticsFien;

    @ApiOperation(value = "打印电子面单", notes = "")
    @GetMapping("print/elec/bill")
    @ResponseBody
    public ComResponse<SFPrintResult> printElecBill(@RequestParam("orderNo") @Valid String orderNo){
        return logisticsFien.printElecBill(orderNo);
    }
}
