package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.logistics.model.vo.print.SFPrintResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@RequestMapping("logistics/")
public class ExpressPrintController {

    @Autowired
    LogisticsFien logisticsFien;

    @ApiOperation(value = "打印电子面单", notes = "")
    @GetMapping("print/Bill")
    @ResponseBody
    public ComResponse<SFPrintResult> printElecBill(@RequestParam("orderNo") @Valid String orderNo){
        return logisticsFien.printElecBill(orderNo);
    }
}
