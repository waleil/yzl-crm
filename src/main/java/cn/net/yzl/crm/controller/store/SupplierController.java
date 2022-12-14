package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.SupplierFeginService;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.utils.FastdfsUtils;
import cn.net.yzl.model.pojo.SupplierPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 9:24
 */
@Api(value = "仓储中心心心心心-供应商管理", tags = {"仓储中心心心心心-仓库管理供应商管理"})
@RestController
@RequestMapping("supplier")
public class SupplierController {

    @Autowired
    private SupplierFeginService supplierFeginService;

    @Autowired
    private FastdfsUtils fastdfsUtils;

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Value("${fast-dfs.url}")
    private String fastUrlBase;

    @ApiOperation(value = "新增供应商管理列表", notes = "新增供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/v1/insertsupplier", method = RequestMethod.POST)
    public ComResponse<Integer> insertSupplier(@RequestBody SupplierPo supplierPo) {
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> ehrStaffClientDetailsByNo = ehrStaffClient.getDetailsByNo(userNo);
        supplierPo.setCreator(userNo);
        supplierPo.setCreatorName(ehrStaffClientDetailsByNo.getData().getName());
        return supplierFeginService.insertSupplier(supplierPo);
    }

    @ApiOperation(value = "编辑供应商管理列表", notes = "编辑供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/v1/updatesupplier", method = RequestMethod.POST)
    public ComResponse<Integer> updateSupplier(@RequestBody SupplierPo supplierPo) {
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> ehrStaffClientDetailsByNo = ehrStaffClient.getDetailsByNo(userNo);
        supplierPo.setUpdator(userNo);
        supplierPo.setUpdatorName(ehrStaffClientDetailsByNo.getData().getName());
        return supplierFeginService.updateSupplier(supplierPo);
    }

    @ApiOperation(value = "编辑供应商状态", notes = "编辑供应商状态",consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/v1/nosearch", method = RequestMethod.GET)
    public ComResponse<Integer> updateState(@RequestParam("id") Integer id,@RequestParam("status") Integer status,@RequestParam("updator")String updator){
        return supplierFeginService.updateState(id,status,updator);
    }


    @ApiOperation(value = "查询供应商详情", notes = "查询供应商详情")
    @RequestMapping(value = "/v1/selectByPrimaryKey", method = RequestMethod.GET)
    public ComResponse<SupplierPo> selectByPrimaryKey(@RequestParam(value = "id") Integer id) {
        return supplierFeginService.selectByPrimaryKey(id);
    }


    @GetMapping("/v1/selectStoreListPage")
    @ApiOperation(value = "条件查询供应商管理列表", notes = "条件查询供应商管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "noAndShortName", value = "供应商编号/供应商简称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "supplierType", value = "供应商类型", required = false, dataType = "byte", paramType = "query"),
    })
    public ComResponse<Page<SupplierPo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam(value = "noAndShortName", required = false) String noAndShortName,
                                                             @RequestParam(value = "supplierType", required = false) Byte supplierType) {
        return supplierFeginService.selectStoreListPage(pageNo, pageSize, noAndShortName, supplierType);

    }


    @ApiOperation(value = "删除供应商", notes = "删除查询供应商")
    @RequestMapping(value = "/v1/delete", method = RequestMethod.GET)
    public ComResponse<Integer> deleteSupplier(@RequestParam(value = "id") Integer id) {
        //TODO 存在采购、采购退货流程的档案，不可删除
        return supplierFeginService.deleteSupplier(id);
    }

    @ApiOperation(value = "批量删除供应商", notes = "批量查询供应商", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/v1/deleteList", method = RequestMethod.POST)
    public ComResponse<Integer> deleteSupplierList(@RequestBody List<Integer> ids) {
        //TODO 存在采购、采购退货流程的档案，不可删除
        return supplierFeginService.deleteSupplierList(ids);
    }

//    @ApiOperation(value = "验证供应商编码/简称", notes = "验证供应商编码/简称")
//    @RequestMapping(value = "/v1/insertCheck", method = RequestMethod.GET)
//    public ComResponse<Integer> insertCheck(@RequestParam(value = "no", required = false) String no, @RequestParam(value = "shortName", required = false) String shortName) {
//        return supplierFeginService.insertCheck(no, shortName);
//    }

//    @ApiOperation(value = "供应商营业执照下载", notes = "供应商营业执照下载")
//    @RequestMapping(value = "/v1/filedownload", method = RequestMethod.GET)
//    public ComResponse selectUrl(@RequestParam(value = "id") Integer id) {
//        return supplierFeginService.selectUrl(id);
//    }

    @ApiOperation(value = "供应商营业执照上传", notes = "供应商营业执照下载", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "/v1/upLoadFile", method = RequestMethod.POST)
    public ComResponse<String> upLoadFile(@RequestParam(value = "file") MultipartFile file) {
        String path = null;
        try {
            path = fastdfsUtils.upload(file).getFullPath();
            if (path != null && path.length() > 0) {
//                path = fastUrlBase + "/" + path;
                return ComResponse.success(path);
            } else {
                return ComResponse.fail(ResponseCodeEnums.UPLOAD_FAIL.getCode(), ResponseCodeEnums.UPLOAD_FAIL.getMessage());
            }
        } catch (IOException e) {
            return ComResponse.fail(ResponseCodeEnums.UPLOAD_FAIL.getCode(), ResponseCodeEnums.UPLOAD_FAIL.getMessage());
        }
    }

    @ApiOperation(value = "按照编号查询供应商", notes = "按照编号查询供应商")
    @RequestMapping(value = "/v1/selectByNo", method = RequestMethod.GET)
    public ComResponse selectByNo(@RequestParam(value = "no") String no) {
        return supplierFeginService.selectByNo(no);
    }

    @ApiOperation(value = "查询全部供应商", notes = "查询全部供应商")
    @RequestMapping(value = "/v1/selectAll", method = RequestMethod.GET)
    public ComResponse<List<SupplierPo>> selectAll() {
        return supplierFeginService.selectAll();

    }


    @ApiOperation(value = "采购单供应商下拉表", notes = "采购单供应商下拉表")
    @RequestMapping(value = "/v1/selectOrdersList", method = RequestMethod.GET)
    public ComResponse<List<SupplierPo>> selectOrdersList() {
        return supplierFeginService.selectOrdersList();
    }

}
