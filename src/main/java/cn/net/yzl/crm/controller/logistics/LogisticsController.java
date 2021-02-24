package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.logistics.model.ExpressCompany;
import cn.net.yzl.logistics.model.ExpressFindTraceDTO;
import cn.net.yzl.logistics.model.ExpressTraceResDTO;
import cn.net.yzl.logistics.model.TransPortExceptionRegistry;
import cn.net.yzl.logistics.model.pojo.*;
import cn.net.yzl.logistics.model.vo.*;
import cn.net.yzl.logistics.settleexpresscharge.*;
import cn.net.yzl.model.dto.StoreToLogisticsDto;
import cn.net.yzl.model.pojo.StorePo;
import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("logistics")
@Api(tags = "物流信息")
public class LogisticsController {

    @Autowired
    LogisticsFien logisticsFien;

    public LogisticsController(LogisticsFien logisticsFien) {
        this.logisticsFien = logisticsFien;
    }

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private FastdfsUtils fastdfsUtils;
    @Autowired
    private FastDFSConfig fastDFSConfig;

    @Autowired
    private StoreFeginService storeFeginService;


    @PostMapping("/seach/reconciliation")
    @ApiOperation("对账")
    public  ComResponse<Boolean>  settlementInterface(@RequestBody @Valid List<Express> searchVo){
        return logisticsFien.settlementInterface(searchVo);
    }


    @PostMapping("/close/account")
    @ApiOperation("结算")
    public  ComResponse<Boolean>  closeAccount(@RequestBody @Valid GeneratorSettVo searchVo){
        return logisticsFien.closeAccount(searchVo);
    }

    @PostMapping("/search/settle/detail")
    @ApiOperation("结算单号查询")
    public ComResponse<List<SettDetailVo>> searchSettDertail(@RequestBody @Valid String setNum){
        return logisticsFien.searchSettDertail(setNum);
    }

    @PostMapping("/search/settle")
    @ApiOperation("结算查询")
    public ComResponse<Page<SettleBillSearchResultVo>> searchSettBill(@RequestBody @Valid SettleBillSearchVo searchVo){
        return  logisticsFien.searchSettBill(searchVo);
    }





    @PostMapping("seach/nosett")
    @ApiOperation("未对账数据查询")
    public  ComResponse<Page<ResultVo>>  searchSettlementData(@RequestBody @Valid SearchVo searchVo){
        return  logisticsFien.searchSettlementData(searchVo);
    }

    @ApiOperation(value = "快递运单查询")
    @PostMapping("v1/search/orderexpress")
    public ComResponse<Page<LogisticsOrderInofoReturn>> searchOrderExpress(@RequestBody @Valid ExpressTraceNumSearchVo searchVo) {

        return logisticsFien.searchOrderExpress(searchVo);
    }


    @ApiOperation(value = "补登签单")
    @PostMapping("v1/signed/order")
    public ComResponse<Boolean> signedOrder(@RequestBody @Valid List<StoreToLogisticsDtoTrace> storeToLogisticsDtoTrace,HttpServletRequest request)
    {

        log.info("=============================");
        log.info("Logistics info ===> userNo:===>" + request.getHeader("userNo"));
        log.info("before");
            ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
        log.info("after");
        log.info(String.valueOf(null==(userNo.getData())));
        log.info("user info " +userNo.getData().getName());


            if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
            }
            if(null==userNo.getData())
            {
                return  ComResponse.fail(ComResponse.ERROR_STATUS,"用户数据错误"+userNo.getCode());
            }


            StaffImageBaseInfoDto data = userNo.getData();



            if(StringUtils.isEmpty(data.getName())){
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户名不存在");
            }




//        boolean isSucess = false;

        for (int i = 0; i < storeToLogisticsDtoTrace.size(); i++) {
            //参数错误
//            if(null==(storeToLogisticsDtoTrace))
//                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE);
            //更新订单状态的信息为空
            if(null==(storeToLogisticsDtoTrace.get(i).getSupplementRegistry())
                    || StringUtils.isEmpty(storeToLogisticsDtoTrace.get(i).getSupplementRegistry().getExpressNum())
//                    ||StringUtils.isEmpty(storeToLogisticsDtoTrace.get(i).getSupplementRegistry().getSupplementor())
            )
                return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息

//            //登记人空
//            if(StringUtils.isEmpty(storeToLogisticsDtoTrace.get(i).getSupplementRegistry().getSupplementor()))
//                return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);

            //人工操作的轨迹信息为空
            if(null==(storeToLogisticsDtoTrace.get(i).getTraceInfo()))
                return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息

            String userName = data.getName();
            String template = userName+"执行了补登操作";
            storeToLogisticsDtoTrace.get(i).getTraceInfo().setDescription(template);

            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setSupplementorName(data.getName());
//            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setSupplementor(userName); // 补登人
            //操作信息为空
//        if(StringUtils.isEmpty(storeToLogisticsDtoTrace.getTraceInfo().getDescription()))
//            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息


            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setSupplementor(data.getStaffNo());   //补登人 user code
            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setSignTime(new Date());  // signTime
            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setDepartId(data.getDepartId());
            storeToLogisticsDtoTrace.get(i).getSupplementRegistry().setOrderStatus(5);
        }


            return logisticsFien.signedOrder(storeToLogisticsDtoTrace);
    }



    @ApiOperation(value = "取消补登")
    @PostMapping("v1/cancel/signed/order")
    public ComResponse<Boolean> cancelSignOrder(@RequestBody @Valid  StoreToLogisticsDtoTrace storeToLogisticsDtoTrace,
                                                HttpServletRequest request){


        ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
        if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
        }
        if(null==userNo.getData())
        {
            return  ComResponse.fail(ComResponse.ERROR_STATUS,"用户数据错误"+userNo.getCode());
        }


        StaffImageBaseInfoDto data = userNo.getData();



        if(StringUtils.isEmpty(data.getName())){
            return ComResponse.fail(ComResponse.ERROR_STATUS, "用户名不存在");
        }




//        boolean isSucess = false;
        //参数错误
        if(null==storeToLogisticsDtoTrace)
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE);

        storeToLogisticsDtoTrace.getSupplementRegistry().setSupplementor(data.getName());
        //更新订单状态的信息为空
        if(null==storeToLogisticsDtoTrace.getSupplementRegistry()
                || StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getExpressNum())
                ||StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getSupplementor())
        )
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息

        //登记人空
        if(StringUtils.isEmpty(storeToLogisticsDtoTrace.getSupplementRegistry().getSupplementor()))
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);

        //人工操作的轨迹信息为空
        if(null==storeToLogisticsDtoTrace.getTraceInfo())
            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息

        String userName = data.getName();
        String template = userName+"执行了取消补登签收";
        storeToLogisticsDtoTrace.getTraceInfo().setDescription(template);
        //操作信息为空
//        if(StringUtils.isEmpty(storeToLogisticsDtoTrace.getTraceInfo().getDescription()))
//            return ComResponse.fail(ResponseCodeEnums.NO_DATA_CODE);  // 没有操作的登记信息


        storeToLogisticsDtoTrace.getSupplementRegistry().setSupplementor(data.getStaffNo());   // user code
        storeToLogisticsDtoTrace.getSupplementRegistry().setSignTime(new Date());  // signTime
        storeToLogisticsDtoTrace.getSupplementRegistry().setDepartId(data.getDepartId());
        storeToLogisticsDtoTrace.getSupplementRegistry().setOrderStatus(1);


        return logisticsFien.cancelSignOrder(storeToLogisticsDtoTrace);

    }



    @ApiOperation(value = "模糊搜索快递公司")
    @GetMapping("v1/like/search/expresscompany")
    public ComResponse<List<ObjectCommon>> getCompanyByName(@RequestParam("companyName") String companyName){
        return logisticsFien.getCompanyByName(companyName);
    }

    @ApiOperation(value = "接口信息加载")
    @GetMapping("/v1/express/company/interfaceinfo/list")
    public ComResponse<List<InterFaceInfo>>  listInterfaceInfo(@RequestParam("id") String id){
        return logisticsFien.listInterfaceInfo(id);
    }

    @ApiOperation(value = "查询物流轨迹", notes = "")
    @PostMapping("findLogisticsTraces")
    public GeneralResult<List<ExpressTraceResDTO>> findLogisticsTraces(@RequestBody @Valid ExpressFindTraceDTO dto){
        return logisticsFien.findLogisticsTraces(dto);
    }





    @ApiOperation(value = "物流-登记生产")
    @GetMapping("v1/generateBillOrderNo")
    public ComResponse<StoreToLogisticsDto> generateBillOrderNo(@RequestParam("orderNo") String orderNo, HttpServletRequest
                                                                request){

//        return  ComResponse.fail(111,"33232");
        RegistryOrderinfo registryOrderinfo  = new RegistryOrderinfo();

        try {
            ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
            if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
            }
            if(null==(userNo.getData()))
            {
                return  ComResponse.fail(ComResponse.ERROR_STATUS,"用户数据错误"+userNo.getCode());
            }


            StaffImageBaseInfoDto data = userNo.getData();



            if(StringUtils.isEmpty(data.getName())){
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户名不存在");
            }

            registryOrderinfo.setOrderNO(orderNo);
            registryOrderinfo.setRegisterName(data.getName());

        } catch (BizException e) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "获取用户认证！");
        }
//        RegistryOrderinfo registryOrderinfo  = new RegistryOrderinfo();
//        registryOrderinfo.setOrderNO(orderNo);
//        registryOrderinfo.setRegisterName("4324324");


        return logisticsFien.generateBillOrderNo(registryOrderinfo);
    }

    @ApiOperation(value = "物流-登记查询")
    @PostMapping("v1/searcha/exception")
    public ComResponse<Page<TransPortExceptionRegistry>> selectExceptionByCondition(@RequestBody SExceptionCondition sExceptionCondition){

        return logisticsFien.selectExceptionByCondition(sExceptionCondition);
    }

    @ApiOperation(value = "物流-取消登记单条")
    @PostMapping("v1/cancel/registry/exceptioninfo")
    public ComResponse<Boolean> cancelRegistryException(@RequestParam("id") String id){
         return logisticsFien.cancelRegistryException(id);

    }

    @ApiOperation(value = "物流-取消批量登记")
    @PostMapping("v1/cancelbatch/registry/exceptioninfo")
    public ComResponse<Boolean> cancelBatchRegistryException(@RequestBody List<String> ids){
        return logisticsFien.cancelBatchRegistryException(ids);
    }

    @ApiOperation(value = "合同下载")
    @GetMapping("/fastDfs/download")
    public String downloadFile(@RequestParam(value = "id") String  id) throws IOException {

        return logisticsFien.downLoadContract(id);
//        String fileName = "";
//        StorePath storePath = StorePath.parseFromUrl(filePath);
//        if (org.apache.commons.lang.StringUtils.isBlank(fileName)) {
//            fileName = FilenameUtils.getName(storePath.getPath());
//        }
//
//
//        InputStream bytes =fastdfsUtils.download(filePath, fileName); ;
//        response.setContentType("application/force-download");// 设置强制下载不打开
//        fileName = URLEncoder.encode(fileName, "utf-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
//        IOUtils.copy(bytes, response.getOutputStream());
//        response.flushBuffer();

    }

    @ApiOperation(value = "合同上传")
    @PostMapping("/fastDfs/upload")
    public ComResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        return logisticsFien.uploadFile(file);

        StorePath upload = fastdfsUtils.upload(file);

        String path = fastDFSConfig.getUrl()+"/"+upload.getFullPath();

//        if(logisticsFien.recordFilePath(path)){
//            return ComResponse.fail(51, "文件保存失败！");
//        };
        return ComResponse.success(path);
    }

//    @Override
//    public GeneralResult<List<ExpressTraceResDTO>> findLogisticsTraces(@Valid ExpressFindTraceDTO dto) {
//        return null;
//    }

    /*
     * 11111
     * */
    @ApiOperation(value="分页查询物流公司列表")
    @PostMapping("v1/expresscompany/listPage")
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody @Valid ExpressSearchDTO expressSearchDTO) {

        ComResponse<List<StorePo>> list = storeFeginService.selectStoreAny();

        List<String> storePoList = new ArrayList<>();
        List<StorePo> storePoList1 = null;
        if(list.getCode()==200){
//            storePoList =list.getData().stream().filter(s->s.getName().contains(expressSearchDTO.getWarehouseId())).map(a->a.getNo()).collect(Collectors.toList());
            storePoList1 =list.getData();

            if(expressSearchDTO.getWarehouseId()!=null) {
                for (int i = 0; i < storePoList1.size(); i++) {
                    if (storePoList1.get(i).getName().indexOf(expressSearchDTO.getWarehouseId()) > -1) {
                        storePoList.add(storePoList1.get(i).getNo());
                    }


                }
            }

            if(storePoList!=null){
                expressSearchDTO.setWarehouseId(StringUtils.join(storePoList.toArray(),","));
            }else{
                expressSearchDTO.setWarehouseId(StringUtils.join(Collections.EMPTY_LIST.toArray(),","));
            }
        }else{
            expressSearchDTO.setWarehouseId(StringUtils.join(Collections.EMPTY_LIST.toArray(),","));
        }

        JSONObject  json =(JSONObject) JSONObject.toJSON(expressSearchDTO);
        String s = json.toJSONString();
        log.info(s);
        return logisticsFien.listPage(expressSearchDTO);
    }

    @ApiOperation(value="物流公司编码和名称")
    @PostMapping("v1/express/company/list")
    public ComResponse  viewExpressCompany() {
        return logisticsFien.viewExpressCompany();

    }



    @ApiOperation(value="查看物流公司")
    @GetMapping("v1/view")
    public ComResponse view(@RequestParam("id") @Valid String  id) {
        return logisticsFien.view(id);
    }


    @ApiOperation(value="编辑物流公司")
    @PostMapping("v1/update")
    public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO,HttpServletRequest request) {


        String userName = "";
        try {
            ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
            if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
            }
            if(null==(userNo.getData()))
            {
                return  ComResponse.fail(ComResponse.ERROR_STATUS,"用户数据错误"+userNo.getCode());
            }


            StaffImageBaseInfoDto data = userNo.getData();



            if(StringUtils.isEmpty(data.getName())){
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户名不存在");
            }

//            registryOrderinfo.setOrderNO(orderNo);
//            registryOrderinfo.setRegisterName(data.getName());
            userName = data.getName();
        } catch (BizException e) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "获取用户认证！");
        }
        saveDTO.setOperator(userName);
        saveDTO.setUpdateCode(userName);

        return  logisticsFien.update(saveDTO);

    }
    @ApiOperation(value="添加物流公司")
    @PostMapping("v1/save")
    public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO,HttpServletRequest request) {

        String userName = "";
        try {
            ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
            if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
            }
            if(null==(userNo.getData()))
            {
                return  ComResponse.fail(ComResponse.ERROR_STATUS,"用户数据错误"+userNo.getCode());
            }


            StaffImageBaseInfoDto data = userNo.getData();



            if(StringUtils.isEmpty(data.getName())){
                return ComResponse.fail(ComResponse.ERROR_STATUS, "用户名不存在");
            }

//            registryOrderinfo.setOrderNO(orderNo);
//            registryOrderinfo.setRegisterName(data.getName());
            userName = data.getName();
        } catch (BizException e) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "获取用户认证！");
        }
        saveDTO.setOperator(userName);
        saveDTO.setUpdateCode(userName);
        saveDTO.setCreateCode(userName);

        return logisticsFien.save(saveDTO);
    }

    @ApiOperation(value="物流公司的账户信息")
    @PostMapping("v1/save/account")
    public  ComResponse<Boolean> saveAccountInfo(@RequestBody @Valid ExpressCompanyBankInfoDTO saveDTO) {

        return logisticsFien.saveAccountInfo(saveDTO);


    }

    @ApiOperation(value="启用禁用状态")
    @PostMapping("v1/update/state")
    public  ComResponse<Boolean> updateState(@RequestBody @Valid ExpressCompanyStateInfo saveDTO) {
        return logisticsFien.updateState(saveDTO);
    }



    @ApiOperation(value="维护接口信息")
    @PostMapping("v1/manage/interface")
    public  ComResponse<Boolean> manageInterface(@RequestBody @Valid ExpressForInterfaceSaveDTO saveDTO) {

        return logisticsFien.manageInterface(saveDTO);


    }




    @ApiOperation(value="删除物流公司")
    @PostMapping("v1/deleteById")
    public GeneralResult<Integer> deleteById(@RequestParam("id")
                                             @NotBlank(message="物流公司id不能为空")
                                             @ApiParam(name="id",value="物流公司id",required=true)  Integer id) {
        return logisticsFien.deleteById(id);
    }
    /*
       物流快递追踪轨迹信息
       11111
        */
    @ApiOperation(value = "查询物流公司状态数组格式", notes = "查询物流公司状态")
//    @ApiImplicitParam(name = "pid",paramType = "query",value = "父级编号",defaultValue = "0",required = true)
    @GetMapping("v1/selectExpressComponyCode")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyCode() {
        return logisticsFien.selectExpressComponyCode();
    }



    @ApiOperation(value = "查询物流公司状态对象格式", notes = "查询物流公司状态")
    @GetMapping("v1/selectExpressComponyCodeForObject")
    public ComResponse<ExpressCode> selectExpressComponyCodeForObject() {
        return logisticsFien.selectExpressComponyCodeForObject();
    }

    /*
     * 物流快递公司信息
     * 1111111
     * */
    @ApiOperation(value = "查询物流公司编码名称信息", notes = "查询物流公司信息")
    @GetMapping("v1/selectExpressComponyDetail")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail() {

        return logisticsFien.selectExpressComponyDetail();
    }

}
