package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 智能工单
 */
@FeignClient(name = "workOrder",url = "${api.gateway.url}/workorderServer/workOrder")
//@FeignClient(name = "workOrder",url = "127.0.0.1:4602/workOrder")
public interface WorkOrderClient {

    @PostMapping(value = "v1/isListPage")
    ComResponse<Page<WorkOrderBean>> isListPage(IsListPageDTO isListPageDTO);

    /**
     * 智能工单: 热线工单管理-列表
     * @param findWorkOrderHotlinePageListDTO
     * @return
     */
    @PostMapping(value = "v1/pageList")
    ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO);
}
