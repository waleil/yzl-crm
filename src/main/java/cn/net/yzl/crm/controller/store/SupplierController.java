package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.store.SupplierFeginService;
import cn.net.yzl.model.pojo.SupplierPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "供应商管理", tags = {"供应商管理"})
@RestController
@RequestMapping("supplier")
public class SupplierController{


//    @Autowired
//    private SupplierFeginService supplierFeginService;
//
//    @ApiOperation(value = "新增供应商管理列表", notes = "新增供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/v1/insertsupplier", method = RequestMethod.POST)
//    public ComResponse<Integer> insertSupplier(SupplierPo supplierPo) {
//        return supplierFeginService.insertSupplier(supplierPo);
//    }
//
//    @ApiOperation(value = "编辑供应商管理列表", notes = "编辑供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/v1/updatesupplier", method = RequestMethod.POST)
//    public ComResponse<Integer> updateSupplier(SupplierPo supplierPo) {
//        return supplierFeginService.updateSupplier(supplierPo);
//    }
//
//    @ApiOperation(value = "编辑供应商状态", notes = "编辑供应商状态")
//    @RequestMapping(value = "/v1/nosearch", method = RequestMethod.GET)
//    public ComResponse<Integer> updateState(Byte state, Integer id) {
//        return supplierFeginService.updateState(state,id);
//    }
//
//    @ApiOperation(value = "无条件查询供应商状态", notes = "无条件查询供应商状态",consumes = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/v1/updatestate", method = RequestMethod.POST)
//    public ComResponse<Page<SupplierPo>> selectByPrimaryKeyOrAll(Integer id, Integer pageNum, Integer pageSize) {
//        return supplierFeginService.selectByPrimaryKeyOrAll(id,pageNum,pageSize);
//    }
//
//    @GetMapping("/v1/search")
//    @ApiOperation(value = "条件查询供应商管理列表", notes = "条件查询供应商管理列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
//            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
//            @ApiImplicitParam(name = "noAndShortName", value = "供应商编号/供应商简称", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "supplierType", value = "供应商类型", required = false, dataType = "byte", paramType = "query"),
//    })
//    public ComResponse<Page<SupplierPo>> selectStoreListPage(Integer pageNo, Integer pageSize, String noAndShortName, Byte supplierType) {
//        return supplierFeginService.selectStoreListPage(pageNo,pageSize,noAndShortName,supplierType);
//    }
//
//
//    @ApiOperation(value = "删除供应商", notes = "删除查询供应商")
//    @RequestMapping(value = "/v1/delete", method = RequestMethod.GET)
//    public ComResponse<Integer> deleteSupplier(Integer id) {
//        return supplierFeginService.deleteSupplier(id);
//    }
//
//    @ApiOperation(value = "批量删除供应商", notes = "批量查询供应商")
//    @RequestMapping(value = "/v1/deleteList", method = RequestMethod.GET)
//    public ComResponse<Integer> deleteSupplierList(List<Integer> ids) {
//        return supplierFeginService.deleteSupplierList(ids);
//    }
//
//
//    @ApiOperation(value = "验证供应商编码/简称", notes = "验证供应商编码/简称")
//    @RequestMapping(value = "/v1/insertCheck", method = RequestMethod.GET)
//    public ComResponse<Integer> insertCheck(String no, String shortName) {
//        return supplierFeginService.insertCheck(no,shortName);
//    }
//
//    @ApiOperation(value = "供应商营业执照下载", notes = "供应商营业执照下载")
//    @RequestMapping(value = "/v1/filedownload", method = RequestMethod.GET)
//    public ComResponse selectUrl(Integer id) {
//        return supplierFeginService.selectUrl(id);
//    }
//
//
//    @ApiOperation(value = "供应商营业执照上传", notes = "供应商营业执照下载",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @RequestMapping(value = "/v1/upLoadFile", method = RequestMethod.POST)
//    public ComResponse<String> upLoadFile(MultipartFile file) {
//        return supplierFeginService.upLoadFile(file);
//    }
//
//
//    @ApiOperation(value = "按照编号查询供应商", notes = "按照编号查询供应商")
//    @RequestMapping(value = "/v1/selectByNo", method = RequestMethod.GET)
//    public ComResponse selectByNo(String no) {
//        return supplierFeginService.selectByNo(no);
//    }
}
