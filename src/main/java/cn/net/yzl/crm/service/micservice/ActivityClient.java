package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.dmc.ActivityDetailResponse;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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


}
