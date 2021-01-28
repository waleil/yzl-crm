package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.logistics.model.ExpressCompany;
import cn.net.yzl.logistics.model.pojo.*;
import cn.net.yzl.logistics.model.vo.ExpressCodeVo;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("logistics")
@Api(tags = "物流信息")
public class LogisticsController {

    @Autowired
    LogisticsFien logisticsFien;


    @ApiOperation(value="物流公司编码和名称")
    @PostMapping("v1/express/company/list")
    public ComResponse  viewExpressCompany() {
        return  ComResponse.success(logisticsFien.viewExpressCompany()) ;
//        List<ObjectCommon>

    }

    @ApiOperation(value="获取所有仓库和编码")
    @GetMapping("v1/store/listPage")
    public List<ObjectCommon>   storeService(){
        List<ObjectCommon> storeBasic=logisticsFien.storeService();
        return storeBasic;
//                = storeService.getStoreBasic();
    }

    @ApiOperation("物流快递追踪轨迹信息")
    @GetMapping ("v1/selectExpressComponyCode")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyCode() {
        return logisticsFien.selectExpressComponyCode();
    }

    @ApiOperation("物流快递公司信息")
    @GetMapping("v1/selectExpressComponyDetail")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail() {
        return logisticsFien.selectExpressComponyDetail();
    }

    @ApiOperation(value = "分页查询物流公司列表")
    @PostMapping("v1/listPage")
//    cn.net.yzl.logistics.model.vo.logistics.ExpressCompany
    public ComResponse<Page<ExpressCompany>> listPage(@RequestBody ExpressSearchDTO expressSearchDTO){
        return logisticsFien.listPage(expressSearchDTO);
    }


    @ApiOperation(value = "查询物流公司")
    @PostMapping("v1/view")
    public ComResponse view(@RequestBody @Valid ExpressCompany expressCompany){
        return logisticsFien.view(expressCompany);
    }

    @ApiOperation(value = "编辑物流公司")
    @PostMapping("v1/update")
    public ComResponse<Integer> update(@RequestBody @Valid ExpressCompanySaveDTO saveDTO){
        return logisticsFien.update(saveDTO);
    }

    @ApiOperation(value = "添加物流公司")
    @PostMapping("v1/save")
    public ComResponse<Integer> save(@RequestBody @Valid ExpressCompanySaveDTO saveDTO){
        return logisticsFien.save(saveDTO);
    }

    @ApiOperation(value = "物流公司的账户信息")
    @PostMapping("v1/save/account")
    public ComResponse<Boolean> saveAccountInfo(@RequestBody @Valid ExpressCompanyBankInfoDTO saveDTO){
        return logisticsFien.saveAccountInfo(saveDTO);
    }

    @ApiOperation(value = "启用禁用状态")
    @PostMapping("v1/update/state")
    public ComResponse<Boolean> updateState(@RequestBody @Valid ExpressCompanyStateInfo saveDTO){
        return logisticsFien.updateState(saveDTO);
    }

    @ApiOperation(value = "维护接口信息")
    @PostMapping("v1/manage/interface")
    public ComResponse<Boolean> manageInterface(@RequestBody @Valid ExpressForInterfaceSaveDTO saveDTO){
        return logisticsFien.manageInterface(saveDTO);
    }

    @ApiOperation(value = "删除物流公司")
    @PostMapping("v1/deleteById")
    public GeneralResult<Integer> deleteById(@RequestParam("id")
                                             @NotBlank(message = "物流公司id不能为空")
                                             @ApiParam(name = "id", value = "物流公司id", required = true) Integer id){
        return logisticsFien.deleteById(id);
    }


}
