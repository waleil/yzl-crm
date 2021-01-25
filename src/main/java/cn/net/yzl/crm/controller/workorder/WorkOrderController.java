package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import io.swagger.annotations.Api;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
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
}
