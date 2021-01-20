package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.dmc.CoopCompanyMediaDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Dmc接口
 */
@FeignClient(value = "marketBaseDB", url = "${api.gateway.url}/marketBaseDB")
public interface CoopCompanyMediaClient {

    @ApiOperation(value = "媒介管理-获取所有未删除的媒介信息")
    @GetMapping("db/v1/coopCompanyMedia/getMedia")
    ComResponse<List<CoopCompanyMediaDto>> getMedia();

}
