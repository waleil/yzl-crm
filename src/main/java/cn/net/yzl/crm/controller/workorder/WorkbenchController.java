package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.workorder.WorkbenchClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.workorder.model.vo.VisitSeatsVo;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineManagerVO;
import cn.net.yzl.workorder.model.vo.WorkbenchHotlineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台
 */
@RestController
@RequestMapping("workbench")
@Api(tags = "工作台")
public class WorkbenchController {


    @Autowired
    WorkbenchClient workbenchClient;

    @RequestMapping(value = "workbenchHotline",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线", notes = "工作台-热线")
    public ComResponse<WorkbenchHotlineVO> workbenchHotline(){
        ComResponse<WorkbenchHotlineVO> workbenchHotlineVO = workbenchClient.workbenchHotline(QueryIds.userNo.get());
        return workbenchHotlineVO;
    }

    @ApiOperation(value = "工作台-回访坐席",notes = "工作台-回访坐席接口")
    @GetMapping(value = "v1/visitSeats")
    public ComResponse<VisitSeatsVo> visitSeats(){
       return workbenchClient.visitSeats(QueryIds.userNo.get());
    }

    @RequestMapping(value = "getWorkbenchHotlineManager",method = RequestMethod.GET)
    @ApiOperation(value = "工作台-热线经理", notes = "工作台-热线经理")
    public ComResponse<WorkbenchHotlineManagerVO> getWorkbenchHotlineManager(){
        ComResponse<WorkbenchHotlineManagerVO> workbenchHotlineManager = workbenchClient.getWorkbenchHotlineManager(QueryIds.userNo.get());
        return workbenchHotlineManager;
    }
}
