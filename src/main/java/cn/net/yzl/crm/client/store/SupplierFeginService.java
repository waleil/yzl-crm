package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.model.pojo.SupplierPo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 9:16
 */
@FeignClient(name = "supplierClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface SupplierFeginService {


    @ApiOperation(value = "新增供应商管理列表", notes = "新增供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "supplier/v1/insertsupplier", method = RequestMethod.POST)
    public ComResponse<Integer> insertSupplier(@RequestBody SupplierPo supplierPo);

    @ApiOperation(value = "编辑供应商管理列表", notes = "编辑供应商管理列表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "supplier/v1/updatesupplier", method = RequestMethod.POST)
    public ComResponse<Integer> updateSupplier(@RequestBody SupplierPo supplierPo);

    @ApiOperation(value = "编辑供应商状态", notes = "编辑供应商状态",consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "supplier/v1/nosearch", method = RequestMethod.POST)
    public ComResponse<Integer> updateState(@RequestBody SupplierPo supplierPo);


    @RequestMapping(value = "supplier/v1/selectByPrimaryKey", method = RequestMethod.GET)
    public ComResponse<SupplierPo> selectByPrimaryKey(@RequestParam(value = "id")Integer id);


    @GetMapping("supplier/v1/selectStoreListPage")
    @ApiOperation(value = "条件查询供应商管理列表", notes = "条件查询供应商管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "noAndShortName", value = "供应商编号/供应商简称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "supplierType", value = "供应商类型", required = false, dataType = "byte", paramType = "query")
    })
    public ComResponse<Page<SupplierPo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam(value = "noAndShortName",required = false) String noAndShortName,
                                                             @RequestParam(value = "supplierType",required = false) Byte supplierType);


    @ApiOperation(value = "删除供应商", notes = "删除查询供应商")
    @RequestMapping(value = "supplier/v1/delete", method = RequestMethod.GET)
    public ComResponse<Integer> deleteSupplier(@RequestParam(value = "id")Integer id);

    @ApiOperation(value = "批量删除供应商", notes = "批量查询供应商",consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "supplier/v1/deleteList", method = RequestMethod.POST)
    public ComResponse<Integer> deleteSupplierList(@RequestBody List<Integer> ids);

    @ApiOperation(value = "验证供应商编码/简称", notes = "验证供应商编码/简称")
    @RequestMapping(value = "supplier/v1/insertCheck", method = RequestMethod.GET)
    public ComResponse<Integer> insertCheck(@RequestParam(value = "no",required = false)String no, @RequestParam(value = "shortName",required = false)String shortName);

    @ApiOperation(value = "供应商营业执照下载", notes = "供应商营业执照下载")
    @RequestMapping(value = "supplier/v1/filedownload", method = RequestMethod.GET)
    public ComResponse selectUrl(@RequestParam(value = "id")Integer id);

    @ApiOperation(value = "供应商营业执照上传", notes = "供应商营业执照下载",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "supplier/v1/upLoadFile", method = RequestMethod.POST)
    public ComResponse<String> upLoadFile(@RequestParam(value = "file")MultipartFile file);

    @ApiOperation(value = "按照编号查询供应商", notes = "按照编号查询供应商")
    @RequestMapping(value = "supplier/v1/selectByNo", method = RequestMethod.GET)
    public ComResponse selectByNo(@RequestParam(value = "no")String  no);


}
