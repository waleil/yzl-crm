package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
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
import cn.net.yzl.model.dto.StoreToLogisticsDto;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

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
    @PostMapping("v1/generateBillOrderNo")
    public ComResponse<StoreToLogisticsDto> generateBillOrderNo(@RequestBody String orderNo, HttpServletRequest
                                                                request){

//        return  ComResponse.fail(111,"33232");
        RegistryOrderinfo registryOrderinfo  = new RegistryOrderinfo();

        try {
            ComResponse<StaffImageBaseInfoDto> userNo = ehrStaffClient.getDetailsByNo(request.getHeader("userNo"));
            if (!userNo.getStatus().equals(ComResponse.SUCCESS_STATUS)) {

                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), userNo.getMessage());
            }
            StaffImageBaseInfoDto data = userNo.getData();

            registryOrderinfo.setOrderNO(orderNo);
            registryOrderinfo.setRegisterName(data.getName());
        } catch (BizException e) {
            ComResponse.fail(12, "获取用户认证！");
        }

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
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody ExpressSearchDTO expressSearchDTO) {


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
    public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO) {
        return  logisticsFien.update(saveDTO);

    }
    @ApiOperation(value="添加物流公司")
    @PostMapping("v1/save")
    public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO) {
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
