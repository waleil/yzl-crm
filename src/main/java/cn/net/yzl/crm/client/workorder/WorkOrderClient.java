package cn.net.yzl.crm.client.workorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "workOrder",url = "${api.gateway.url}/workorderServer")
public class WorkOrderClient {


}
