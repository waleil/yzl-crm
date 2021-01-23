package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.constant.EhrParamEnum;
import cn.net.yzl.crm.dto.biTask.Indicators;
import cn.net.yzl.crm.dto.dmc.CoopCompanyMediaDto;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import cn.net.yzl.crm.dto.ehr.EhrPostLevelDto;
import cn.net.yzl.crm.dto.ehr.StaffStatusDto;
import cn.net.yzl.crm.dto.product.ProduceDto;
import cn.net.yzl.crm.service.CommonService;
import cn.net.yzl.crm.service.micservice.BiTaskClient;
import cn.net.yzl.crm.service.micservice.CoopCompanyMediaClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LaunchManageClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/16 15:13
 * @desc: 共用请求方法
 **/
@Slf4j
@RestController
@RequestMapping("common/")
@Api(tags = "共用接口服务")
public class CommonController {

    @Autowired
    CoopCompanyMediaClient coopCompanyMediaClient;

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private LaunchManageClient launchManageClient;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BiTaskClient biTaskClient;

    @ApiOperation(value = "获取岗位列表")
    @GetMapping("/v1/getPostLevelList")
    public ComResponse<List<EhrPostLevelDto>> getPostLevelList() {
        return ehrStaffClient.getPostLevelList();
    }

    @ApiOperation(value = "获取媒体列表")
    @GetMapping("/v1/getMediaList")
    public ComResponse<List<CoopCompanyMediaDto>> getMediaList() {
        return coopCompanyMediaClient.getMedia();
    }

    @ApiOperation(value = "获取职场", httpMethod = "GET")
    @GetMapping("v1/getWorkplace")
    public ComResponse<List<StaffStatusDto>> getWorkplace() {
        return ehrStaffClient.getAllStuffStatus(EhrParamEnum.EHR_DICT_WORKPLACE_STATUS);
    }

    @ApiOperation(value = "查询媒体下所有投放的广告")
    @GetMapping("v1/getLaunchManageByRelationBusNo")
    public ComResponse<List<LaunchManageDto>> getLaunchManageByRelationBusNo(@RequestParam("relationBusNo") Long relationBusNo) {
        return launchManageClient.getLaunchManageByRelationBusNo(relationBusNo);
    }

    @ApiOperation(value = "所有广告")
    @GetMapping("v1/getAllLaunchManage")
    public ComResponse<List<LaunchManageDto>> getAllLaunchManage() {
        return launchManageClient.getAllLaunchManage();
    }

    @ApiOperation(value = "培训过的商品")
    @GetMapping("v1/selectProduct")
    public ComResponse<List<ProduceDto>> selectProduct() {
        return commonService.selectProduct();
    }

    @ApiOperation(value = "指标名称")
    @GetMapping("v1/getBiIndicatorsSettingList")
    public ComResponse<Page<Indicators>> getBiIndicatorsSettingList(@RequestParam("pageNum") Integer pageNum,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("indicatorsDomainType") Integer indicatorsDomainType) {
        return biTaskClient.getBiIndicatorsSettingList(pageNum,pageSize,indicatorsDomainType);
    }


}
