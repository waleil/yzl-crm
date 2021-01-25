package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("workOrder")
public class WorkOrderController {

    @Autowired
    private WorkOrderClient workOrderClient;
}
