package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 工作台
 */
@FeignClient(name = "workbench", url = "${api.gateway.url}/workorderServer")
//@FeignClient(name = "workbench", url = "127.0.0.1:4602")
public interface WorkbenchClient {

    @RequestMapping(value = "workbench/workbenchHotline",method = RequestMethod.GET)
    ComResponse<WorkbenchHotlineVO> workbenchHotline(@RequestParam("staffNo") String staffNo);

    @RequestMapping(value = "workbench/visitSeats",method = RequestMethod.GET)
    ComResponse<VisitSeatsVo> visitSeats(@RequestParam("staffNo") String staffNo);

    @RequestMapping(value = "workbench/getWorkbenchHotlineManager",method = RequestMethod.GET)
    ComResponse<WorkbenchHotlineManagerVO> getWorkbenchHotlineManager(@RequestParam("staffNo") String staffNo);

    @RequestMapping(value = "workbench/getWorkbenchVisitManager",method = RequestMethod.GET)
    ComResponse<WorkbenchVisitManagerVo> getWorkbenchVisitManager(@RequestParam("staffNo") String staffNo);

    @RequestMapping(value = "workbench/getVisitSeatMonitoring",method = RequestMethod.GET)
    ComResponse<List<VisitSeatMonitoringVo>> getVisitSeatMonitoring(@RequestParam("staffNo") String staffNo);

    @RequestMapping(value = "workbench/getWorkbenchHotlineManagerMonitoring",method = RequestMethod.GET)
    ComResponse<List<WorkbenchHotlineManagerMonitoringVO>> getWorkbenchHotlineManagerMonitoring(@RequestParam("staffNo") String staffNo);
}
