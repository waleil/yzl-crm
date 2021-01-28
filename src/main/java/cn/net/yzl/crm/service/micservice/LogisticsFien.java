package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.logistics.model.ExpressCompany;
import cn.net.yzl.logistics.model.ExpressFindTraceDTO;
import cn.net.yzl.logistics.model.ExpressTraceResDTO;
import cn.net.yzl.logistics.model.pojo.*;
import cn.net.yzl.logistics.model.vo.ExpressCodeVo;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 顾客服务接口
 */
@FeignClient(name = "yzl-logistics-server",url = "${api.gateway.url}/logisticsServer")
//@FeignClient(value = "yzl-crm-customer-api")
public interface LogisticsFien {


    @PostMapping("23323/ee/22")
    public String getTest();

    @ApiOperation(value = "快递公司编码和名称")
    @PostMapping("v1/express/company/list")
    public ComResponse viewExpressCompany();

    @ApiOperation(value = "获取所有仓库和编码")
    @GetMapping("v1/store/listPage")
    public List<ObjectCommon> storeService();

    @ApiOperation("物流公司")
    @GetMapping("/v1/selectExpressComponyCode")
    ComResponse<List<ExpressCodeVo>> selectExpressComponyCode();

    @ApiOperation("物流快递公司信息")
    @GetMapping("/v1/selectExpressComponyDetail")
    ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail();

    @ApiOperation(value = "分页查询物流公司列表")
    @PostMapping("v1/listPage")
//    cn.net.yzl.logistics.model.vo.logistics.ExpressCompany
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody ExpressSearchDTO expressSearchDTO);


    @ApiOperation(value = "查询物流公司")
    @PostMapping("v1/view")
    public ComResponse view(@RequestBody @Valid ExpressCompany expressCompany);

    @ApiOperation(value = "编辑物流公司")
    @PostMapping("v1/update")
    public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO);

    @ApiOperation(value = "添加物流公司")
    @PostMapping("v1/save")
    public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO);

    @ApiOperation(value = "物流公司的账户信息")
    @PostMapping("v1/save/account")
    public ComResponse<Boolean> saveAccountInfo(@RequestBody @Valid ExpressCompanyBankInfoDTO saveDTO);

    @ApiOperation(value = "启用禁用状态")
    @PostMapping("v1/update/state")
    public ComResponse<Boolean> updateState(@RequestBody @Valid ExpressCompanyStateInfo saveDTO);

    @ApiOperation(value = "维护接口信息")
    @PostMapping("v1/manage/interface")
    public ComResponse<Boolean> manageInterface(@RequestBody @Valid ExpressForInterfaceSaveDTO saveDTO);

    @ApiOperation(value = "删除物流公司")
    @PostMapping("v1/deleteById")
    public GeneralResult<Integer> deleteById(@RequestParam("id")
                                             @NotBlank(message = "物流公司id不能为空")
                                             @ApiParam(name = "id", value = "物流公司id", required = true) Integer id);

    @ApiOperation(value = "查询物流轨迹", notes = "")
    @PostMapping("logistics/findLogisticsTraces")
    public GeneralResult<List<ExpressTraceResDTO>> findLogisticsTraces(@RequestBody @Valid ExpressFindTraceDTO dto) ;

}
