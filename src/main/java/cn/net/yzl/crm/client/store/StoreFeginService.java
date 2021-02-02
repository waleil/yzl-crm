package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.model.dto.StoreDto;
import cn.net.yzl.model.dto.StoreLocalDto;
import cn.net.yzl.model.pojo.ProductStockPo;
import cn.net.yzl.model.pojo.StoreLocalPo;
import cn.net.yzl.model.pojo.StorePo;
import cn.net.yzl.model.pojo.SysDictDataPo;
import cn.net.yzl.model.vo.StoreLocalVo;
import cn.net.yzl.model.vo.StoreVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/16 13:37
 */
@FeignClient("yzl-store-server")
//@FeignClient(name = "storeClient",url = "${api.gateway.url}/storeServer")
public interface StoreFeginService {

    @GetMapping("store/v1/selectStoreListPage")
    public ComResponse<Page<StorePo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "storeNo",required = false) String storeNo,
                                                          @RequestParam(value = "sType",required = false) Integer sType,
                                                          @RequestParam(value = "mType",required = false) Integer mType);


    @GetMapping("store/v1/selectStore")
    public ComResponse<StoreDto> selectStore(@RequestParam("id") Integer id);


    @ApiOperation(value = "编辑/修改仓库", notes = "编辑/修改仓库")
    @PostMapping("store/v1/updateStore")
    public ComResponse<Integer> updateStore(@RequestBody StoreVO storeVO);


    @PostMapping("store/v1/insertStore")
    public ComResponse insertStore(@RequestBody StoreVO storeVO);


    @ApiOperation(value = "开启/关闭仓库状态", notes = "开启/关闭仓库状态")
    @GetMapping("store/v1/updateStoreEnable")
    public ComResponse<Integer> updateStoreEnable(@RequestParam("id") Integer id,@RequestParam("status") Integer status,@RequestParam("updator")String updator);


    @PostMapping("store/v1/insertStoreLocal")
    public ComResponse insertStoreLocal(@RequestBody StoreLocalVo storeLocalVo);



    @GetMapping("store/v1/selectStoreLocalInfo")
    public ComResponse<StoreLocalDto> selectStoreLocalInfo(@RequestParam("id") Integer id);

    @PostMapping("store/v1/updateStoreLocal")
    public ComResponse updateStoreLocal(@RequestBody StoreLocalVo storeLocalVo);


    @ApiOperation(value = "开启/关闭库位状态", notes = "开启/关闭库位状态")
    @GetMapping("store/v1/updateStoreLocalStatus")
    ComResponse<Integer> updateStoreLocalStatus(@RequestParam("id") Integer id,@RequestParam("status") Integer status,@RequestParam("updator")String updator);



    @GetMapping("store/v1/selectStoreLocalListPage")
    public ComResponse<Page<StoreLocalPo>> selectStoreLocalListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam(value = "storeLocalNo",required = false) String storeLocalNo,
                                                                    @RequestParam(value = "storeAreaKindId",required = false) Integer storeAreaKindId);

    @GetMapping("store/v1/delStoreArea")
    public ComResponse delStoreArea(@RequestParam(value = "id")Integer id);

    @ApiOperation(value = "修改新增库位属性", notes = "修改新增库位属性")
    @PostMapping("store/v1/insertAndUpdateStoreArea")
    public ComResponse insertAndUpdateStoreArea(@RequestBody List<SysDictDataPo> list);


    @ApiOperation(value = "查询库区类型", notes = "查询库区类型")
    @GetMapping("store/v1/selectAreaType")
    ComResponse<List<SysDictDataPo>> selectAreaType();

    //TODO 库位查询是否返回list
    @ApiOperation(value = "根据库位编码查询", notes = "根据库位编码查询")
    @GetMapping("store/v1/selectAllByNo")
    public ComResponse selectAllByNo(@RequestParam (value = "no") String no);


    @ApiOperation(value = "库位字典查询", notes = "库位字典查询")
    @GetMapping("store/v1/selectSysDictDate")
    public ComResponse<List<SysDictDataPo>> selectSysDictDate();

    @ApiOperation(value = "查询单个库位", notes = "查询单个库位")
    @GetMapping("store/v1/selectStoreLocal")
    public ComResponse<StoreLocalPo> selectStoreLocal(@RequestParam(value = "no") String no);

    @GetMapping("store/v1/storeLocalPageList")
    public ComResponse<Page<ProductStockPo>> storeLocalPageList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "codeAndName",required = false) String codeAndName,
                                                          @RequestParam(value = "storeNo",required = false) String storeNo);


    @ApiOperation(value = "获取已存在的财务归属", notes = "获取已存在的财务归属", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "store/v1/getAllFinanceDepart", method = RequestMethod.GET)
    ComResponse<List<DepartDto>> getAllFinanceDepart();


    @ApiOperation(value = "根据仓库状态查询仓库",notes = "根据仓库状态查询仓库")
    @GetMapping("store/v1/selectOrderStatus")
    ComResponse<List<StoreVO>> selectOrderStatus(@RequestParam("status") Integer status);



    @ApiOperation(value = "下拉库位",notes = "下拉库位")
    @GetMapping("store/v1/selectStoreLocalPullDown")
    ComResponse<List<StoreLocalPo>> selectStoreLocalPullDown(@RequestParam("storeId") Integer storeId);


    @ApiOperation(value = "下拉仓库列表",notes = "下拉仓库列表")
    @GetMapping("store/v1/selectStoreAny")
    ComResponse<List<StorePo>> selectStoreAny();

    @ApiOperation(value = "新增仓库的时候下拉库位的查询",notes = "新增仓库的时候下拉库位的查询")
    @GetMapping("store/v1/storeLocalPullDown")
    ComResponse<List<StoreLocalVo>> storeLocalPullDown();


    @ApiOperation(value = "分页查询绑定的库位",notes = "分页查询绑定的库位")
    @GetMapping("store/v1/storeLocalPageList")
    public ComResponse<Page<StoreLocalPo>> storeLocalPageList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "storeId",required = false) Integer storeI);

}
