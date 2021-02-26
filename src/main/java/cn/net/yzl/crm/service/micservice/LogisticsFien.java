package cn.net.yzl.crm.service.micservice;

import java.util.List;
import cn.net.yzl.common.entity.ComResponse;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.logistics.model.*;
import cn.net.yzl.logistics.model.pojo.*;
import cn.net.yzl.logistics.model.vo.*;
import cn.net.yzl.logistics.settleexpresscharge.*;
import cn.net.yzl.model.dto.StoreToLogisticsDto;
import cn.net.yzl.order.model.vo.order.ImportParam;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 顾客服务接口
 */
@FeignClient(name = "yzl-logistics-server",url = "${api.gateway.url}/logisticsServer")
public interface LogisticsFien {


    @PostMapping("/settlement/reconciliation")
    @ApiOperation("对账")
    public  ComResponse<Boolean>  settlementInterface(@RequestBody @Valid UpdateSearchVo updateSearchVo);

    @PostMapping("settlement/close/account")
    @ApiOperation("结算")
    public  ComResponse<Boolean>  closeAccount(@RequestBody @Valid GeneratorSettVo searchVo);

    @PostMapping("settlement/search/settle/detail")
    @ApiOperation("结算单号查询")
    public ComResponse<List<SettDetailVo>> searchSettDertail(@RequestBody @Valid String setNum);

//    @PostMapping("settlement/search/settle")
//    @ApiOperation("结算查询")
//    public ComResponse<Page<SettleBillSearchResultVo>> searchSettBill(@RequestBody @Valid SettleBillSearchVo searchVo);
//


    @PostMapping("settlement/seach/nosett")
    @ApiOperation("未对账数据查询")
    public  ComResponse<Page<ResultVo>>  searchSettlementData(@RequestBody @Valid SearchVo searchVo);

    @ApiOperation(value = "导入")
    @PostMapping("settlement/logistics/import/freight")
    ComResponse<ImportResult> importFromExcel(@RequestBody ImportParam param);

    @ApiOperation(value = "快递运单查询")
    @PostMapping("/track/v1/search/orderexpress")
    public ComResponse<Page<LogisticsOrderInofoReturn>> searchOrderExpress(@RequestBody @Valid ExpressTraceNumSearchVo searchVo) ;

    @ApiOperation(value = "补登签单")
    @PostMapping("/track/v1/signed/order")
    public ComResponse<Boolean> signedOrder(@RequestBody @Valid  List<StoreToLogisticsDtoTrace> storeToLogisticsDtoTrace);


    @ApiOperation(value = "取消补登")
    @PostMapping("/track/v1/cancel/signed/order")
    public ComResponse<Boolean> cancelSignOrder(@RequestBody @Valid  StoreToLogisticsDtoTrace storeToLogisticsDtoTrace);

    @ApiOperation(value = "接口信息加载")
    @GetMapping("exp/company/v1/express/company/interfaceinfo/list")
    public ComResponse<List<InterFaceInfo>>  listInterfaceInfo(@RequestParam("id") String id);


    @ApiOperation(value = "物流-取消批量登记")
    @PostMapping("exp/company/v1/cancelbatch/registry/exceptioninfo")
    public ComResponse<Boolean> cancelBatchRegistryException(@RequestBody List<String> ids);


    @ApiOperation(value = "物流-取消批量登记")
    @PostMapping("exp/company/v1/cancelbatch/registry/exceptioninfo")
    public ComResponse<Boolean> cancelBatchRegistryException(@RequestBody String ids);

    @ApiOperation(value = "物流-登记生产")
    @PostMapping("exp/company/v1/generateBillOrderNo")
    public ComResponse<StoreToLogisticsDto> generateBillOrderNo(@RequestBody RegistryOrderinfo registryOrderinfo);

    @ApiOperation(value = "物流-登记查询")
    @PostMapping("exp/company/v1/searcha/exception")
    public ComResponse<Page<TransPortExceptionRegistry>> selectExceptionByCondition(@RequestBody SExceptionCondition sExceptionCondition);

    @ApiOperation(value = "物流-登记查询")
    @PostMapping("exp/company/v1/cancel/registry/exceptioninfo")
    public ComResponse<Boolean> cancelRegistryException(@RequestParam("id") String id);

    @ApiOperation(value = "下载文件", notes = "")
    @PostMapping("/fastDfs/download/file")
    String downLoadContract(String id);

    @ApiOperation(value = "记录文件路径", notes = "")
    @PostMapping("fastDfs/record/file")
    Boolean recordFilePath(@RequestParam("path") String path);


    @ApiOperation(value = "查询物流轨迹", notes = "")
    @PostMapping("logistics/findLogisticsTraces")
    public GeneralResult<List<ExpressTraceResDTO>> findLogisticsTraces(@RequestBody @Valid ExpressFindTraceDTO dto) ;

/*    @ApiOperation(value="获取所有仓库和编码")
    @GetMapping("/exp/company/v1/store/listPage")
    public ComResponse<Page<StoreVO>> storeService();*/

    @ApiOperation(value="分页快递公司档案列表")
    @PostMapping("/exp/company/v1/listPage")
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody ExpressSearchDTO expressSearchDTO) ;

    @ApiOperation(value="快递公司档案编码列表")
    @PostMapping("/exp/company/v1/express/company/list")
    public ComResponse viewExpressCompany() ;



    @ApiOperation(value="快递公司档案详情")
    @GetMapping("/exp/company/v1/view")
    public ComResponse view(@RequestParam("id") @Valid String  id) ;


    @ApiOperation(value="编辑快递公司档案")
    @PostMapping("/exp/company/v1/update")
    public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO) ;
    @ApiOperation(value="添加快递公司档案")
    @PostMapping("/exp/company/v1/save")
    public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO);

    @ApiOperation(value="维护快递公司档案-->账户信息")
    @PostMapping("/exp/company/v1/save/account")
    public  ComResponse<Boolean> saveAccountInfo(@RequestBody @Valid ExpressCompanyBankInfoDTO saveDTO) ;

    @ApiOperation(value="快递公司档案-->启用禁用状态; 0:1")
    @PostMapping("/exp/company/v1/update/state")
    public  ComResponse<Boolean> updateState(@RequestBody @Valid ExpressCompanyStateInfo saveDTO);


    @ApiOperation(value="快递公司档案-->维护接口信息")
    @PostMapping("/exp/company/v1/manage/interface")
    public  ComResponse<Boolean> manageInterface(@RequestBody @Valid ExpressForInterfaceSaveDTO saveDTO) ;

//    @ApiOperation(value="添加物流公司接口信息")
//    @PostMapping("v1/saveInterfaceDetail")
//    public ComResponse<Integer> saveInterfaceDetail(@RequestBody @Valid ExpressForInterfaceSaveDTO expressForInterfaceSaveDTO) {
//
//        return service.saveInterfaceDetail(expressForInterfaceSaveDTO);
//    }


    @ApiOperation(value="快递公司档案-->删除")
    @PostMapping("/exp/company/v1/deleteById")
    public GeneralResult<Integer> deleteById(@RequestParam("id")
                                             @NotBlank(message="物流公司id不能为空")
                                             @ApiParam(name="id",value="物流公司id",required=true)  Integer id);
    /*
       物流快递追踪轨迹信息
       11111
        */
    @ApiOperation(value = "查询物流公司状态数组格式", notes = "查询物流公司状态")
    @GetMapping("/exp/company/v1/selectExpressComponyCode")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyCode() ;



    @ApiOperation(value = "查询物流公司状态对象格式", notes = "查询物流公司状态")
//    @ApiImplicitParam(name = "pid",paramType = "query",value = "父级编号",defaultValue = "0",required = true)
    @GetMapping("/exp/company/v1/selectExpressComponyCodeForObject")
    public ComResponse<ExpressCode> selectExpressComponyCodeForObject() ;

    /*
     * 物流快递公司信息
     * 1111111
     * */
    @ApiOperation(value = "查询物流公司编码名称信息", notes = "查询物流公司信息")
//    @ApiImplicitParam(name = "pid",paramType = "query",value = "父级编号",defaultValue = "0",required = true)
    @GetMapping("/exp/company/v1/selectExpressComponyDetail")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail() ;

    @ApiOperation(value = "模糊搜索快递公司")
    @GetMapping("exp/company/v1/like/search/expresscompany")
    ComResponse<List<ObjectCommon>> getCompanyByName(@RequestParam("companyName") String companyName);

    /**
     * 物流赔付接口
     *
     * @param indemnity {@link ExpressIndemnity}
     * @return
     * @author zhangweiwei
     * @date 2021年2月22日,下午3:11:56
     */
    @PostMapping("/settlement/logistics/charge/indemnity")
    ComResponse<Boolean> settlementLogisticsChargeIndemnity(@RequestBody ExpressIndemnity indemnity);

    @PostMapping("settlement/add/settle/detail")
    @ApiOperation("添加结算")
    ComResponse addSettleDetail(@RequestBody @Valid ExpressSettleDetailAddVO addVO);

    @PostMapping("settlement/search/settle")
    @ApiOperation("结算查询")
    ComResponse<Page<ExpressSettlementPageVo>> searchSettBill(@RequestBody SettleBillSearchVo searchVo);

    @PostMapping("settlement/express/charge/detail")
    @ApiOperation("运费结算明细")
    ComResponse<Page<SettlementDetailResult>>  expressChargeSettlementDetailSearch(@RequestBody ExpressChargeSettlementDetail
                                                                                                    expressChargeSettlementDetail);

    @PostMapping("settlement/export/ExpressChargeExcel")
    @ApiOperation("已对账未对账导出")
    ComResponse<List<ResultVo>> exportExpressChargeExcel(SearchVo searchVo);


    @PostMapping("settlement/v1/allCreateSettle")
    @ApiOperation("全选生成结算单接口")
    ComResponse allCreateSettle(@RequestBody CreateSettleByCondition createSettleByCondition);
}