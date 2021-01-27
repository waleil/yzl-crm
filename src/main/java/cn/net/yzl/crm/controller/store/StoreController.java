package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.StoreFeginService;
import cn.net.yzl.model.dto.DepartDto;
import cn.net.yzl.model.dto.StoreDto;
import cn.net.yzl.model.dto.StoreLocalDto;
import cn.net.yzl.model.pojo.ProductStockPo;
import cn.net.yzl.model.pojo.StoreLocalPo;
import cn.net.yzl.model.pojo.StorePo;
import cn.net.yzl.model.pojo.SysDictDataPo;
import cn.net.yzl.model.vo.StoreLocalVo;
import cn.net.yzl.model.vo.StoreVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/16 13:41
 */
@Api(value = "仓储中心心心心心-仓库管理", tags = {"仓储中心心心心心-仓库管理"})
@RequestMapping("store")
@RestController
public class StoreController {

    @Autowired
    private StoreFeginService storeFeginService;


    @GetMapping("v1/selectStoreListPage")
    @ApiOperation(value = "分页查询仓库管理列表", notes = "分页查询仓库管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "storeNo", value = "仓库编号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sType", value = "仓库类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mType", value = "经营类型", required = false, dataType = "String", paramType = "query"),
    })
    public ComResponse<Page<StorePo>> selectStoreListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "storeNo",required = false) String storeNo,
                                                          @RequestParam(value = "sType",required = false) Integer sType,
                                                          @RequestParam(value = "mType",required = false) Integer mType){

        return storeFeginService.selectStoreListPage(pageNo,pageSize,storeNo,sType,mType);
    }

    @ApiOperation(value = "查询单个仓库信息", notes = "查询单个仓库信息")
    @ApiImplicitParam(name = "id", value = "仓库id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/selectStore")
    public ComResponse<StoreDto> selectStore(@RequestParam("id") Integer id){
        return storeFeginService.selectStore(id);
    }


    @ApiOperation(value = "编辑/修改仓库", notes = "编辑/修改仓库")
    @PostMapping("v1/updateStore")
    public ComResponse<Integer> updateStore(@RequestBody StoreVO storeVO){
        return storeFeginService.updateStore(storeVO);
    }


    @ApiOperation(value = "新增仓库", notes = "新增仓库")
    @PostMapping("v1/insertStore")
    public ComResponse insertStore(@RequestBody StoreVO storeVO){
        return storeFeginService.insertStore(storeVO);
    }


    @ApiOperation(value = "开启/关闭仓库状态", notes = "开启/关闭仓库状态")
    @GetMapping("v1/updateStoreEnable")
    public ComResponse<Integer> updateStoreEnable(@RequestParam("id") Integer id,@RequestParam("status") Integer status,@RequestParam("updator")String updator){
        return storeFeginService.updateStoreEnable(id,status,updator);
    }

    @ApiOperation(value = "新增库位", notes = "新增库位")
    @PostMapping("v1/insertStoreLocal")
    public ComResponse insertStoreLocal(@RequestBody StoreLocalVo storeLocalVo){
        return storeFeginService.insertStoreLocal(storeLocalVo);
    }

    @ApiOperation(value = "查询库位详情",notes = "查询库位详情")
    @ApiImplicitParam(name = "id", value = "仓库库位id", required = true, dataType = "Int", paramType = "query")
    @GetMapping("v1/selectStoreLocalInfo")
    public ComResponse<StoreLocalDto> selectStoreLocalInfo(@RequestParam("id") Integer id){
        return storeFeginService.selectStoreLocalInfo(id);
    }

    @ApiOperation(value = "编辑库位", notes = "编辑库位")
    @PostMapping("v1/updateStoreLocal")
    public ComResponse updateStoreLocal(@RequestBody StoreLocalVo storeLocalVo){
        return storeFeginService.updateStoreLocal(storeLocalVo);
    }

    @ApiOperation(value = "查询库区类型", notes = "查询库区类型")
    @GetMapping("v1/selectAreaType")
    public ComResponse<List<SysDictDataPo>> selectAreaType(){
        //store_area_type
        return storeFeginService.selectAreaType();
    }

    @ApiOperation(value = "开启/关闭库位状态", notes = "开启/关闭库位状态")
    @GetMapping("/v1/updateStoreLocalStatus")
    public ComResponse<Integer> updateStoreLocalStatus(@RequestParam("id") Integer id,@RequestParam("status") Integer status,@RequestParam("updator")String updator){
        //@RequestParam("id") Integer id,@RequestParam("status") Integer status
        //return storeService.updateStatusLocalEnable(id,status);
        return storeFeginService.updateStoreLocalStatus(id,status,updator);
    }


    @GetMapping("v1/selectStoreLocalListPage")
    @ApiOperation(value = "分页查询库位管理列表", notes = "分页查询库位管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "storeNo", value = "库位编号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "storeAreaKindId", value = "库位类型", required = false, dataType = "String", paramType = "query"),
    })
    public ComResponse<Page<StoreLocalPo>> selectStoreLocalListPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam(value = "storeNo",required = false) String storeNo,
                                                                    @RequestParam(value = "storeAreaKindId",required = false) Integer storeAreaKindId){

        return storeFeginService.selectStoreLocalListPage(pageNo,pageSize,storeNo,storeAreaKindId);
    }


    @ApiOperation(value = "删除库位属性操作", notes = "删除库位属性操作")
    @GetMapping("v1/delStoreArea")
    public ComResponse delStoreArea(@RequestParam(value = "id")Integer id){
        return storeFeginService.delStoreArea(id);
    }

    @ApiOperation(value = "修改新增库位属性", notes = "修改新增库位属性")
    @PostMapping("v1/insertAndUpdateStoreArea")
    public ComResponse insertAndUpdateStoreArea(@RequestBody List<SysDictDataPo> list){
        return storeFeginService.insertAndUpdateStoreArea(list);
    }


    @ApiOperation(value = "根据库位编码查询", notes = "根据库位编码查询")
    @GetMapping("v1/selectAllByNo")
    public ComResponse selectAllByNo(@RequestParam (value = "no") String no){
        return storeFeginService.selectAllByNo(no);
    }


    @ApiOperation(value = "库位字典查询", notes = "库位字典查询")
    @GetMapping("v1/selectSysDictDate")
    public ComResponse<List<SysDictDataPo>> selectSysDictDate(){
        return storeFeginService.selectSysDictDate();
    }

    @ApiOperation(value = "查询单个库位", notes = "查询单个库位")
    @GetMapping("v1/selectStoreLocal")
    public ComResponse<StoreLocalPo> selectStoreLocal(@RequestParam(value = "no") String no){
        return storeFeginService.selectStoreLocal(no);
    }

    @GetMapping("v1/stockInquiry")
    @ApiOperation(value = "库存查询", notes = "库存查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页开始页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "codeAndName", value = "商品编码/条形码/商品名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "storeName", value = "仓库名称", required = false, dataType = "String", paramType = "query"),
    })
    public ComResponse<Page<ProductStockPo>> stockInquiry(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "codeAndName",required = false) String codeAndName    ,
                                                          @RequestParam(value = "storeNo",required = false) String storeNo){

        return storeFeginService.stockInquiry(pageNo,pageSize,codeAndName,storeNo);
    }

    @ApiOperation(value = "获取已存在的财务归属", notes = "获取已存在的财务归属", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "v1/getAllFinanceDepart", method = RequestMethod.GET)
    ComResponse<List<DepartDto>> getAllFinanceDepart() {
        return storeFeginService.getAllFinanceDepart();
    }


    @ApiOperation(value = "根据仓库状态查询仓库",notes = "根据仓库状态查询仓库")
    @GetMapping("v1/selectOrderStatus")
    public ComResponse<List<StoreVO>> selectOrderStatus(@RequestParam("status") Integer status) {
        return storeFeginService.selectOrderStatus(status);
    }


    @ApiOperation(value = "下拉库位",notes = "下拉库位")
    @GetMapping("v1/selectStoreLocalPullDown")
    public ComResponse<List<StoreLocalPo>> selectStoreLocalPullDown(@RequestParam("storeId") Integer storeId) {
        ComResponse<List<StoreLocalPo>> listComResponse = storeFeginService.selectStoreLocalPullDown(storeId);
        return listComResponse;
    }


    @ApiOperation(value = "下拉仓库列表",notes = "下拉仓库列表")
    @GetMapping("v1/selectStoreAny")
    public ComResponse<List<StorePo>> selectStoreAny() {
        return storeFeginService.selectStoreAny();
    }



    @ApiOperation(value = "新增仓库的时候下拉库位的查询",notes = "新增仓库的时候下拉库位的查询")
    @GetMapping("v1/StoreLocalPullDown")
    public ComResponse<List<StoreLocalVo>> StoreLocalPullDown() {
        return storeFeginService.storeLocalPullDown();
    }

}
