package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.dmc.ActivityDetailResponse;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import cn.net.yzl.crm.dto.dmc.MemberLevelResponse;
import cn.net.yzl.crm.dto.dmc.PageModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "activityDB", url = "${api.gateway.url}/activityDB")
public interface ActivityClient {

    @GetMapping("db/v1/launchManage/getAllLaunchManage")
    ComResponse<List<LaunchManageDto>> getAllLaunchManage();

    @GetMapping("db/v1/launchManage/getLaunchManageByRelationBusNo")
    ComResponse<List<LaunchManageDto>> getLaunchManageByRelationBusNo(@RequestParam("relationBusNo") Long relationBusNo);

    @ApiOperation(value = "商品促销活动-获取所有的活动")
    @GetMapping("db/v1/productSales/getActivityList")
    ComResponse<List<ActivityDetailResponse>> getActivityList();

    @ApiOperation(value = "会员管理-会员级别管理-会员级别设置列表")
    @PostMapping("db/v1/memberLevelManager/getMemberLevelPages")
    ComResponse<Page<MemberLevelResponse>> getMemberLevelPages(@RequestBody PageModel request);
}
