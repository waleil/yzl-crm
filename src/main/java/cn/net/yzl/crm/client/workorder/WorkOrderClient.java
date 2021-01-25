package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sound.midi.SoundbankResource;

/**
 * 智能工单
 */
@FeignClient(name = "workOrder", url = "${api.gateway.url}/workorderServer/workOrder")
//@FeignClient(name = "workOrder",url = "127.0.0.1:4602/workOrder")
public interface WorkOrderClient {

    /**
     * 智能工单: 我的回访工单-列表
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isListPage")
    ComResponse<Page<WorkOrderBean>> isListPage(IsListPageDTO isListPageDTO);

    /**
     * 智能工单: 热线工单管理-列表
     *
     * @param findWorkOrderHotlinePageListDTO
     * @return
     */
    @PostMapping(value = "v1/pageList")
    ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO);

    /**
     * 智能工单：热线工单管理-回收
     *
     * @param updateRecyclingDTO
     * @return
     */
    @PostMapping(value = "v1/updateRecycling")
    ComResponse<Void> updateRecycling(UpdateRecyclingDTO updateRecyclingDTO);

    /**
     * 智能工单:回访工单管理-列表
     *
     * @param workOrderVisitVO
     * @return
     */
    @PostMapping(value = "v1/listPage")
    ComResponse<Page<WorkOrderBean>> listPage(WorkOrderVisitVO workOrderVisitVO);

    /**
     * 智能工单：热线工单管理-单数据调整
     *
     * @param updateSingleAdjustDTO
     * @return
     */
    @PostMapping(value = "v1/updateSingleAdjust")
    ComResponse<Void> updateSingleAdjust(UpdateSingleAdjustDTO updateSingleAdjustDTO);

    /**
     * 智能工单:回访工单管理-查询所有用户首次购买商品
     *
     * @return
     */
    @GetMapping(value = "queryFirstProduct")
    ComResponse<String> queryFirstProduct();

    /**
     * 智能工单:回访工单管理-查询所有用户最后一次购买商品
     *
     * @return
     */
    @GetMapping(value = "queryLastProduct")
    ComResponse<String> queryLastProduct();
}
