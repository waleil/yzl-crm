package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.MyWorkOrderHotlineListDTO;
import cn.net.yzl.workorder.model.dto.UpdateAcceptStatusReceiveDTO;
import cn.net.yzl.workorder.model.dto.UpdateDisposeWorkOrderCommit;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.vo.FindDWorkOrderHotlineDetailsVO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.workorder.model.vo.MyWorkOrderHotlineListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *  工单规则配置相关功能
 */
@FeignClient(name = "WorkOrderHotline",url = "${api.gateway.url}/workorderServer/hotline")
//@FeignClient(name = "WorkOrderHotline",url = "127.0.0.1:4602/hotline")
public interface WorkOrderHotlineClient {

    /**
     *  智能工单：热线工单管理-回收
     * @param updateRecyclingDTO
     * @return
     */
//    @RequestMapping(value = "v1/updateRecycling",method = RequestMethod.POST)
//    ComResponse<Void> updateRecycling(@RequestBody UpdateRecyclingDTO updateRecyclingDTO);


    /**
     * 智能工单：热线工单管理-单数据调整
     * @param updateSingleAdjustDTO
     * @return
     */
    @RequestMapping(value = "v1/updateSingleAdjust",method = RequestMethod.POST)
    ComResponse<Void> updateSingleAdjust(@RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO);

    /**
     * 智能工单：热线工单管理-多数据调整
     * @param updateMoreAdjustDTO
     * @return
     */
    @RequestMapping(value = "v1/updateMoreAdjust",method = RequestMethod.POST)
    ComResponse<Void> updateMoreAdjust(@RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO);

    /**
     * 智能工单：热线工单管理-列表
     * @param findWorkOrderHotlinePageListDTO
     * @return
     */
    @RequestMapping(value = "v1/findWorkOrderHotlinePageList",method = RequestMethod.POST)
    ComResponse<Page<FindWorkOrderHotlinePageListVO>> findWorkOrderHotlinePageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO);

    /**
     * 智能工单：我的热线工单-列表
     * @param
     * @return
     */
    @RequestMapping(value = "v1/findMyWorkOrderHotlinePageList",method = RequestMethod.POST)
    ComResponse<Page<MyWorkOrderHotlineListVO>> findMyWorkOrderHotlinePageList(@RequestBody MyWorkOrderHotlineListDTO myWorkOrderHotlineListDTO);

    /**
     * 智能工单：我的热线工单-接收
     * @param updateAcceptStatusReceiveDTO
     * @return
     */
    @RequestMapping(value = "v1/updateAcceptStatusReceive",method = RequestMethod.POST)
    ComResponse<Void> updateAcceptStatusReceive(UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO);

    /**
     * 智能工单：我的热线工单-处理工单详情
     * @param updateAcceptStatusReceiveDTO
     * @return
     */
    @RequestMapping(value = "v1/findDWorkOrderHotlineDetails",method = RequestMethod.POST)
    ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO);

    /**
     * 智能工单：我的热线工单-处理工单提交
     * @param updateDisposeWorkOrderCommit
     */
    @RequestMapping(value = "v1/updateDisposeWorkOrderCommit",method = RequestMethod.POST)
    ComResponse<Void> updateDisposeWorkOrderCommit(UpdateDisposeWorkOrderCommit updateDisposeWorkOrderCommit);
}
