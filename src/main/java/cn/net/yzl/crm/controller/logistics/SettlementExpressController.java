package cn.net.yzl.crm.controller.logistics;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.settlement.SettlementExpressService;
import cn.net.yzl.logistics.model.ExpressSettleDetailAddVO;
import cn.net.yzl.logistics.model.vo.ExpressSettlementPageVo;
import cn.net.yzl.logistics.model.vo.ImportResult;
import cn.net.yzl.logistics.settleexpresscharge.*;
import cn.net.yzl.logistics.settleexpresscharge.excel.ResultExcelVo;
import cn.net.yzl.logistics.settleexpresscharge.excel.ResultRecionExcelVo;
import cn.net.yzl.model.vo.InventoryExcelVo;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import cn.net.yzl.model.vo.InventoryProductResultExcelVo;
import cn.net.yzl.order.model.vo.order.ImportParam;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("settlement")
@Api(tags = "结算API")
public class SettlementExpressController {

    @Autowired
    private LogisticsFien settlement;

    @Autowired
    private SettlementExpressService settlementExpressService;
    @Autowired
    private EhrStaffClient ehrStaffClient;


    /*
     * 导出
     *
     * */

    @ApiOperation(value = "导出对账或者未对账数据",notes = "运费对账订单的导出")
    @PostMapping("v1/exportInventoryExcel")
    public ComResponse<List<ResultVo>> exportExpressChargeExcel(@RequestBody SearchVo searchVo, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ResultVo>> listComResponse = settlement.exportExpressChargeExcel(searchVo);
        if (listComResponse==null || listComResponse.getCode() != 200)
            return listComResponse;
        List<ResultVo> listComResponseData = listComResponse.getData();
        //盘点日期
        Date inventoryDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(inventoryDate);
        //仓库名称
//        String storeName = inventoryExcelVo.getStoreName();
        httpServletResponse.setCharacterEncoding("UTF-8");
        //响应内容格式
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+"PD"+date+".xlsx");


        // 已经结账的list
        List<ResultRecionExcelVo> inventoryProductResultExcelVoList = new ArrayList<>();


        List<ResultExcelVo> inventoryProductResultExcelVoList1 = new ArrayList<>();
        for (ResultVo listComResponseDatum : listComResponseData) {
            if(searchVo.getSearchStatus()==1){
                ResultRecionExcelVo inventoryProductResultExcelVo = new ResultRecionExcelVo();
                BeanUtils.copyProperties(listComResponseDatum,inventoryProductResultExcelVo);
                inventoryProductResultExcelVoList.add(inventoryProductResultExcelVo);
            }
            if(searchVo.getSearchStatus()==0){
                ResultExcelVo inventoryProductResultExcelVo = new ResultExcelVo();
                BeanUtils.copyProperties(listComResponseDatum,inventoryProductResultExcelVo);
                inventoryProductResultExcelVoList1.add(inventoryProductResultExcelVo);
            }



        }
        //向前端写入文件流流
        EasyExcel.write(httpServletResponse.getOutputStream(), InventoryProductResultExcelVo.class)
                .sheet("盘点商品库存表").doWrite(inventoryProductResultExcelVoList);



        return ComResponse.success();
    }



    /*
     * 运费结算明细查询
     * */
    @PostMapping("/express/charge/detail")
    @ApiOperation("运费结算明细")
    public   ComResponse<Page<SettlementDetailResult>>  expressChargeSettlementDetailSearch(@RequestBody @Valid ExpressChargeSettlementDetail
                                                                                                    expressChargeSettlementDetail){
        return  settlement.expressChargeSettlementDetailSearch(expressChargeSettlementDetail);



    }



    @PostMapping("/seach/reconciliation")
    @ApiOperation("对账")
    public  ComResponse<Boolean>  settlementInterface(@RequestBody @Valid List<Express> searchVo){
        return settlement.settlementInterface(searchVo);
    }


    @PostMapping("/close/account")
    @ApiOperation("生成结算单")
    public  ComResponse<Boolean>  closeAccount(@RequestBody @Valid GeneratorSettVo searchVo){
        return settlement.closeAccount(searchVo);
    }

    @PostMapping("/search/settle/detail")
    @ApiOperation("结算单号查询")
    public ComResponse<List<SettDetailVo>> searchSettDertail(@RequestBody @Valid String setNum){
        return settlement.searchSettDertail(setNum);
    }







    @PostMapping("seach/nosett")
    @ApiOperation("未对账数据查询")
    public  ComResponse<Page<ResultVo>>  searchSettlementData(@RequestBody @Valid SearchVo searchVo){
        return  settlement.searchSettlementData(searchVo);
    }


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
        return settlement.addSettleDetail(addVO);
    }

    @PostMapping("/search/settle")
    @ApiOperation("结算查询")
    public ComResponse<Page<ExpressSettlementPageVo>> searchSettBill(@RequestBody SettleBillSearchVo searchVo){
        return  settlement.searchSettBill(searchVo);
    }


    @ApiOperation(value = "导出结算",notes = "导出结算")
    @PostMapping("v1/export/settle")
    public void exportSettleExcel(@RequestBody SettleBillSearchVo searchVo,HttpServletResponse response) throws IOException {
        settlementExpressService.exportSettleExcel(searchVo,response);
    }
}

