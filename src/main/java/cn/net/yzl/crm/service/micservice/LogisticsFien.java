package cn.net.yzl.crm.service.micservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.ComResponse;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.logistics.model.ExpressCompany;
import cn.net.yzl.logistics.model.pojo.*;
import cn.net.yzl.logistics.model.vo.ExpressCode;
import cn.net.yzl.logistics.model.vo.ExpressCodeVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
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
//@FeignClient(value = "yzl-crm-customer-api")
public interface LogisticsFien {

    @ApiOperation(value="获取所有仓库和编码")
    @GetMapping("/exp/company/v1/store/listPage")
    public List<ObjectCommon>   storeService();

    @ApiOperation(value="分页快递公司档案列表")
    @PostMapping("/exp/company/v1/listPage")
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody ExpressSearchDTO expressSearchDTO) ;

    @ApiOperation(value="快递公司档案编码列表")
    @PostMapping("/exp/company/v1/express/company/list")
    public ComResponse  viewExpressCompany() ;



    @ApiOperation(value="快递公司档案详情")
    @PostMapping("/exp/company/v1/view")
    public ComResponse view(@RequestBody @Valid ExpressCompany expressCompany) ;


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
}