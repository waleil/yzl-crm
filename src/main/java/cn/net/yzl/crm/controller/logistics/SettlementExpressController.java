package cn.net.yzl.crm.controller.logistics;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.settlement.SettlementExpressService;
import cn.net.yzl.logistics.model.ExpressSettleDetailAddVO;
import cn.net.yzl.logistics.model.vo.ExpressSettlementPageVo;
import cn.net.yzl.logistics.model.vo.ImportResult;
import cn.net.yzl.logistics.settleexpresscharge.SettleBillSearchVo;
import cn.net.yzl.order.model.vo.order.ImportParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("settlement")
@Api(tags = "结算API")
public class SettlementExpressController {

    @Autowired
    LogisticsFien logisticsFien;
    @Autowired
    private SettlementExpressService settlementExpressService;
    @Autowired
    private EhrStaffClient ehrStaffClient;

    @PostMapping("import/settlement/freight")
    @ApiOperation("快递费用导入")
    public ComResponse<ImportResult> importSettlement(@RequestBody ImportParam param){

        if (!StringUtils.hasText(param.getExpressCompanyCode())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "快递公司编码不能为空");
        }
        if (!StringUtils.hasText(param.getFileUrl())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "上传文件URL不能为空");
        }
//        return this.settlement.importFromExcel(param);
        return ComResponse.success();
    }

    @PostMapping("/add/settle/detail")
    @ApiOperation("添加结算")
    public ComResponse addSettleDetail(@RequestBody @Valid ExpressSettleDetailAddVO addVO, HttpServletRequest request){
        String userNo = request.getHeader("userNo");
        ComResponse<StaffImageBaseInfoDto> user = ehrStaffClient.getDetailsByNo(userNo);
        StaffImageBaseInfoDto data = user.getData();
        if(data != null){
            addVO.setOperator(userNo);
            addVO.setOperatorName(data.getName());
        }
        return logisticsFien.addSettleDetail(addVO);
    }

    @PostMapping("/search/settle")
    @ApiOperation("结算查询")
    public ComResponse<Page<ExpressSettlementPageVo>> searchSettBill(@RequestBody SettleBillSearchVo searchVo){
        return  logisticsFien.searchSettBill(searchVo);
    }


    @ApiOperation(value = "导出结算",notes = "导出结算")
    @PostMapping("v1/export/settle")
    public void exportSettleExcel(@RequestBody SettleBillSearchVo searchVo,HttpServletResponse response) throws IOException {
        settlementExpressService.exportSettleExcel(searchVo,response);
    }


}
