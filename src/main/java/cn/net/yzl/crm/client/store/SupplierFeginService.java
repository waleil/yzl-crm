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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 9:16
 */
@FeignClient(name = "supplierClient",url = "${api.gateway.url}/storeServer")
@Service("supplierFeginService")
public interface SupplierFeginService {


    @RequestMapping(value = "supplier/v1/insertsupplier", method = RequestMethod.POST)
    ComResponse<Integer> insertSupplier(@RequestBody SupplierPo supplierPo);

    @RequestMapping(value = "supplier/v1/updatesupplier", method = RequestMethod.POST)
    ComResponse<Integer> updateSupplier(@RequestBody SupplierPo supplierPo);

    @RequestMapping(value = "supplier/v1/nosearch", method = RequestMethod.GET)
    ComResponse<Integer> updateState(@RequestParam(value = "state") Byte state, @RequestParam(value = "id")Integer id);


    @ApiOperation(value = "无条件查询供应商状态", notes = "无条件查询供应商状态",consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "supplier/v1/updatestate", method = RequestMethod.POST)
    ComResponse<Page<SupplierPo>> selectByPrimaryKeyOrAll(@RequestParam(value = "id")Integer id, @RequestParam(value = "pageNum")Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize);


    @GetMapping("supplier/v1/search")
    public ComResponse<Page<SupplierPo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam(value = "noAndShortName",required = false) String noAndShortName,
                                                             @RequestParam(value = "supplierType",required = false) Byte supplierType);


    @ApiOperation(value = "删除供应商", notes = "删除查询供应商")
    @RequestMapping(value = "supplier/v1/delete", method = RequestMethod.GET)
    public ComResponse<Integer> deleteSupplier(@RequestParam(value = "id")Integer id);

    @ApiOperation(value = "批量删除供应商", notes = "批量查询供应商")
    @RequestMapping(value = "supplier/v1/deleteList", method = RequestMethod.GET)
    public ComResponse<Integer> deleteSupplierList(@RequestBody List<Integer> ids);

    @RequestMapping(value = "supplier/v1/insertCheck", method = RequestMethod.GET)
    public ComResponse<Integer> insertCheck(@RequestParam(value = "no")String no, @RequestParam(value = "shortName")String shortName);

    @RequestMapping(value = "supplier/v1/filedownload", method = RequestMethod.GET)
    public ComResponse selectUrl(@RequestParam(value = "id")Integer id);

    @RequestMapping(value = "supplier/v1/upLoadFile", method = RequestMethod.POST)
    public ComResponse<String> upLoadFile(@RequestParam(value = "file") MultipartFile file);

    @ApiOperation(value = "按照编号查询供应商", notes = "按照编号查询供应商")
    @RequestMapping(value = "supplier/v1/selectByNo", method = RequestMethod.GET)
    public ComResponse selectByNo(@RequestParam(value = "no")String  no);


}
