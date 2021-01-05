package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.model.Media;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Dmc接口
 */
@FeignClient(value = "marketBaseDB",url = "http://api.staff.yuzhilin.net.cn/marketBaseDB")
public interface CoopCompanyMediaFien {
    @GetMapping(value="/db/v1/coopCompanyMedia/getMedia")
    GeneralResult<List<Media>> getMediaList();


}
