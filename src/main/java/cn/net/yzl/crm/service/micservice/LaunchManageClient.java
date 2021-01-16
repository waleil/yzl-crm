package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.dmc.LaunchManageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "activityDB", url = "${api.gateway.url}/activityDB")
public interface LaunchManageClient {

    @GetMapping("db/v1/launchManage/getAllLaunchManage")
    ComResponse<List<LaunchManageDto>> getAllLaunchManage();

}
