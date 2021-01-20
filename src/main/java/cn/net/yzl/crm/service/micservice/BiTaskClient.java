package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.biTask.Indicators;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/18 13:27
 * @desc: //TODO  请说明该类的用途
 **/
@FeignClient(name = "BiTask", url = "${api.gateway.url}/taskDB")
public interface BiTaskClient {

    @GetMapping("/indicatorsSetting/getBiIndicatorsSettingList")
    ComResponse<Page<Indicators>> getBiIndicatorsSettingList(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam("indicatorsDomainType") Integer indicatorsDomainType);
}
