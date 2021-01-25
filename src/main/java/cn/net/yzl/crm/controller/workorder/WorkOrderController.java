package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import io.swagger.annotations.Api;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("workOrder")
@Api(tags = "智能工单")
public class WorkOrderController {

    @Autowired
    private WorkOrderClient workOrderClient;

    @PostMapping(value = "v1/isListPage")
    public ComResponse<Page<WorkOrderBean>> isListPage(@RequestBody IsListPageDTO isListPageDTO){
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.isListPage(isListPageDTO);
        return listPage;
    }

    @PostMapping("v1/pageList")
    @ApiOperation(value = "智能工单: 热线工单管理-列表", notes = "智能工单: 热线工单管理-列表")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO) {
        ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageComResponse = workOrderClient.pageList(findWorkOrderHotlinePageListDTO);
        return pageComResponse;
    }
}
