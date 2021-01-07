package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.model.DiseaseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 获取病症层级列表
 */
@FeignClient("yzl-product-api")
public interface ProductFien {



//    @RequestMapping(method = RequestMethod.GET, value = "/api/product/getDiseaseByPid")
//    GeneralResult<DiseaseBean> productClassiService(String pid);

    @RequestMapping(method = RequestMethod.GET, value = "/api/product/getDiseaseByPid")
    GeneralResult<List<DiseaseBean>> productClassiService(@RequestParam("pid") String pid);

}
