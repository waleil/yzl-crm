package cn.net.yzl.crm.controller.logistics;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.service.settlement.SettlementExpressService;
import cn.net.yzl.logistics.model.ExpressSettleDetailAddVO;
import cn.net.yzl.logistics.model.vo.ExpressImportParam;
import cn.net.yzl.logistics.model.vo.ExpressSettlementPageVo;
import cn.net.yzl.logistics.model.vo.ImportResult;
import cn.net.yzl.logistics.settleexpresscharge.*;
import cn.net.yzl.logistics.settleexpresscharge.excel.ResultExcelBDVo;
import cn.net.yzl.logistics.settleexpresscharge.excel.ResultExcelVo;
import cn.net.yzl.logistics.settleexpresscharge.excel.ResultRecionExcelVo;

import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("settlement")
@Api(tags = "??????API")
public class SettlementExpressController {
    private Resource templateResource = new ClassPathResource("excel/comparisonmgt/exptemplate.xlsx");
    @Autowired
    private LogisticsFien settlement;

    @Autowired
    private SettlementExpressService settlementExpressService;
    @Autowired
    private EhrStaffClient ehrStaffClient;




    @GetMapping("/excel/download")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<byte[]> download() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s.xlsx",
                URLEncoder.encode("???????????????", StandardCharsets.UTF_8.name())));
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
                .body(StreamUtils.copyToByteArray(this.templateResource.getInputStream()));
    }

    /*
     * ??????
     *
     * */

    @ApiOperation(value = "?????????????????????????????????",notes = "???????????????????????????")
    @PostMapping("v1/exportInventoryExcel")
    public ComResponse<List<ResultExportVo>> exportExpressChargeExcel(@RequestBody SearchVo searchVo, HttpServletResponse httpServletResponse) throws IOException {
        ComResponse<List<ResultExportVo>> listComResponse = settlement.exportExpressChargeExcel(searchVo);
        log.info("????????????????????????:{}", JsonUtil.toJsonStr(listComResponse));
        if (listComResponse==null || listComResponse.getCode() != 200)
            return listComResponse;
        List<ResultExportVo> listComResponseData = listComResponse.getData();
        Date inventoryDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(inventoryDate);
//        String storeName = inventoryExcelVo.getStoreName();
        httpServletResponse.setCharacterEncoding("UTF-8");
        //??????????????????
        httpServletResponse.setContentType("application/vnd.ms-excel");


        if (searchVo.getSearchStatus() == 1) {
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+
                    URLEncoder.encode("????????????"+date+".xlsx","utf-8"));
        }
//        URLEncoder.encode("??????????????????"+sysDate+".xlsx", "utf-8"));
        if (searchVo.getSearchStatus() == 0) {
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+
                    URLEncoder.encode("???????????????"+date+".xlsx","utf-8"));
        }

//        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+"??????????????????"+date+".xlsx");


        // ???????????????list
        List<ResultRecionExcelVo> inventoryProductResultExcelVoList = new ArrayList<>();


        List<ResultExcelBDVo> inventoryProductResultExcelVoList1 = new ArrayList<>();
        for (ResultExportVo listComResponseDatum : listComResponseData) {
            if(searchVo.getSearchStatus()==1){
//                httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+"????????????"+date+".xlsx");
                ResultRecionExcelVo inventoryProductResultExcelVo = new ResultRecionExcelVo();
                BeanUtils.copyProperties(listComResponseDatum,inventoryProductResultExcelVo);
                inventoryProductResultExcelVoList.add(inventoryProductResultExcelVo);
            }
            if(searchVo.getSearchStatus()==0){
//                httpServletResponse.setHeader("Content-Disposition", "attachment;fileName="+"???????????????"+date+".xlsx");
                ResultExcelBDVo inventoryProductResultExcelVo = new ResultExcelBDVo();
                BeanUtils.copyProperties(listComResponseDatum,inventoryProductResultExcelVo);
                inventoryProductResultExcelVoList1.add(inventoryProductResultExcelVo);
            }
        }
        //???????????????????????????
        if (searchVo.getSearchStatus() == 1) {
            EasyExcel.write(httpServletResponse.getOutputStream(), ResultRecionExcelVo.class)
                    .sheet("???????????????").doWrite(inventoryProductResultExcelVoList);
        }
        if (searchVo.getSearchStatus() == 0) {
            EasyExcel.write(httpServletResponse.getOutputStream(), ResultExcelVo.class)
                    .sheet("??????????????????").doWrite(inventoryProductResultExcelVoList1);
        }




        return ComResponse.success();
    }



    /*
     * ????????????????????????
     * */
    @PostMapping("/express/charge/detail")
    @ApiOperation("??????????????????")
    public   ComResponse<Page<SettlementDetailResult>>  expressChargeSettlementDetailSearch(@RequestBody ExpressChargeSettlementDetail
                                                                                                    expressChargeSettlementDetail){
        return  settlement.expressChargeSettlementDetailSearch(expressChargeSettlementDetail);



    }



    @PostMapping("/seach/reconciliation")
    @ApiOperation("??????")
    public  ComResponse<Boolean>  settlementInterface(@RequestBody @Valid List<ReconExpressNum> searchVo ){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> user = ehrStaffClient.getDetailsByNo(userNo);
        StaffImageBaseInfoDto data = user.getData();

        UpdateSearchVo updateSearchVo = new UpdateSearchVo();
        updateSearchVo.setExpressList(searchVo);
        updateSearchVo.setUserName(data.getName());
        updateSearchVo.setUserNo(userNo);

        return settlement.settlementInterface(updateSearchVo);
    }


    @PostMapping("/close/account")
    @ApiOperation("???????????????")
    public  ComResponse<Boolean>  closeAccount(@RequestBody @Valid GeneratorSettVo searchVo){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> user = ehrStaffClient.getDetailsByNo(userNo);
        StaffImageBaseInfoDto data = user.getData();
        if(data != null){
            searchVo.setCreateUser(userNo);
            searchVo.setCreateUserName(data.getName());
        }
        return settlement.closeAccount(searchVo);
    }

    @PostMapping("/search/settle/detail")
    @ApiOperation("??????????????????")
    public ComResponse<List<SettDetailVo>> searchSettDertail(@RequestBody @Valid SetNum setNum){
        return settlement.searchSettDertail(setNum);
    }







    @PostMapping("seach/nosett")
    @ApiOperation("?????????????????????")
    public  ComResponse<Page<ResultDecimalVo>>  searchSettlementData(@RequestBody @Valid SearchVo searchVo){
        log.info("??????API=="+"searchSettlementData");
        try {
            return  settlement.searchSettlementDataDecimal(searchVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE, "?????????????????????");
    }


    @PostMapping("settlement/logistics/import/freight")
    @ApiOperation("??????????????????")
    public ComResponse<ImportResult> importSettlement(@RequestBody ExpressImportParam param){

        if (!StringUtils.hasText(param.getExpressCompanyCode())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "??????????????????????????????");
        }
        if (!StringUtils.hasText(param.getFileUrl())) {
            return ComResponse.fail(ResponseCodeEnums.ERROR, "????????????URL????????????");
        }
        return this.settlement.importFromExcel(param);
//        return ComResponse.success();
    }

    @PostMapping("/add/settle/detail")
    @ApiOperation("????????????")
    public ComResponse addSettleDetail(@RequestBody @Valid ExpressSettleDetailAddVO addVO){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> user = ehrStaffClient.getDetailsByNo(userNo);
        StaffImageBaseInfoDto data = user.getData();
        if(data != null){
            addVO.setOperator(userNo);
            addVO.setOperatorName(data.getName());
        }
        return settlement.addSettleDetail(addVO);
    }

    @PostMapping("/search/settle")
    @ApiOperation("????????????")
    public ComResponse<Page<ExpressSettlementPageVo>> searchSettBill(@RequestBody SettleBillSearchVo searchVo){
        return  settlement.searchSettBill(searchVo);
    }


    @ApiOperation(value = "????????????",notes = "????????????")
    @PostMapping("v1/export/settle")
    public void exportSettleExcel(@RequestBody SettleBillSearchVo searchVo,HttpServletResponse response) throws IOException {
        settlementExpressService.exportSettleExcel(searchVo,response);
    }


    @ApiOperation(value = "??????????????????",notes = "??????????????????")
    @PostMapping("v1/export/over/detail")
    public void exportSettleOverDetailExcel(@RequestBody ExpressChargeSettlementDetail detail, HttpServletResponse response) throws IOException {
        settlementExpressService.exportSettleOverDetailExcel(detail,response);
    }

    @PostMapping("v1/allCreateSettle")
    @ApiOperation("???????????????????????????")
    public ComResponse allCreateSettle(@RequestBody CreateSettleByCondition createSettleByCondition){
        return settlement.allCreateSettle(createSettleByCondition);
    }

    @PostMapping("search/charge/sum")
    @ApiOperation("??????????????????")
    public ComResponse<ExpressSettlementPageVo> searchChargeSum(@RequestBody SettleBillSearchVo searchVo){
        return settlement.searchChargeSum(searchVo);
    }

    @PostMapping("search/detail/charge/sum")
    @ApiOperation("????????????????????????????????????")
    public ComResponse<CountFreightVo> searchDetailSum(@RequestBody ExpressChargeSettlementDetail detail) {
        return settlement.searchDetailSum(detail);
    }
}

