package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.constant.EhrParamEnum;
import cn.net.yzl.crm.dto.dmc.CoopCompanyMediaDto;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import cn.net.yzl.crm.dto.ehr.StaffStatusDto;
import cn.net.yzl.crm.service.micservice.CoopCompanyMediaClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.LaunchManageClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "投放管理-所有广告")
    @PostMapping("v1/getAllLaunchManage")
    public ComResponse<List<LaunchManageDto>> getAllLaunchManage() {
        return launchManageClient.getAllLaunchManage();
    }

}