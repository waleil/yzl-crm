package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 工作台
 */
@FeignClient(name = "workbench", url = "${api.gateway.url}/workorderServer")
//@FeignClient(name = "workbench", url = "127.0.0.1:4602")
public interface WorkbenchClient {

    @RequestMapping(value = "workbench/workbenchHotline",method = RequestMethod.GET)
    ComResponse<WorkbenchHotlineVO> workbenchHotline(@RequestParam("staffNo") String staffNo);
}
