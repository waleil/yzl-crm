package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.member.customerJourney.QueryWorkOrderVo;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.dto.*;
import cn.net.yzl.workorder.model.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 智能工单
 */
@FeignClient(name = "workOrder", url = "${api.gateway.url}/workorderServer/workOrder")
//@FeignClient(name = "workOrder", url = "127.0.0.1:4602/workOrder")
public interface WorkOrderClient {

    /**
     * 智能工单: 我的回访工单-列表
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isVisitListPage")
    ComResponse<Page<WorkOrderBean>> isVisitListPage(IsListPageDTO isListPageDTO);

    /**
     * 智能工单: 热线工单管理-列表
     *
     * @param findWorkOrderHotlinePageListDTO
     * @return
     */
    @PostMapping(value = "v1/workOrderHotlineList")
    ComResponse<Page<FindWorkOrderHotlinePageListVO>> workOrderHotlineList(FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO);

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
    @PostMapping(value = "v1/visitAdministrationListPage")
    ComResponse<Page<WorkOrderBean>> visitAdministrationListPage(WorkOrderVisitVO workOrderVisitVO);

    /**
     * 智能工单：热线工单管理-单数据调整
     *
     * @param updateSingleAdjustDTO
     * @return
     */
    @PostMapping(value = "v1/updateSingleAdjust")
    ComResponse<Boolean> updateSingleAdjust(UpdateSingleAdjustDTO updateSingleAdjustDTO);

    /**
     * 智能工单:回访工单管理-查询所有用户首次购买商品
     *
     * @return
     */
    @GetMapping(value = "v1/queryFirstProduct")
    ComResponse<String> queryFirstProduct(@RequestParam("deptId") Integer deptId,@RequestParam("status") Integer status);

    /**
     * 智能工单:回访工单管理-查询所有用户最后一次购买商品
     *
     * @return
     */
    @GetMapping(value = "v1/queryLastProduct")
    ComResponse<String> queryLastProduct(@RequestParam("deptId") Integer deptId,@RequestParam("status") Integer status);

    /**
     * 智能工单：热线工单管理-多数据调整
     *
     * @param updateMoreAdjustDTO
     * @return
     */
    @PostMapping(value = "v1/updateMoreAdjust")
    ComResponse<Boolean> updateMoreAdjust(UpdateMoreAdjustDTO updateMoreAdjustDTO);

    /**
     * 智能工单-单条调整
     *
     * @param updateWorkOrderVisitDTO
     * @return
     */
    @PostMapping(value = "v1/adjustment")
    ComResponse<Boolean> adjustment(UpdateWorkOrderVisitDTO updateWorkOrderVisitDTO);

    /**
     * 智能工单-批量调整
     *
     * @param updateBatchDTO
     * @return
     */
    @PostMapping(value = "v1/batchAdjustment")
    ComResponse<Boolean> batchAdjustment(UpdateBatchDTO updateBatchDTO);

    /**
     * 智能工单：我的热线工单-列表
     *
     * @param myWorkOrderHotlineListDTO
     * @return
     */
    @PostMapping(value = "v1/findMyWorkOrderHotlinePageList")
    ComResponse<Page<MyWorkOrderHotlineListVO>> findMyWorkOrderHotlinePageList(MyWorkOrderHotlineListDTO myWorkOrderHotlineListDTO);

    /**
     * 智能工单：我的热线工单-接收
     *
     * @param updateAcceptStatusReceiveDTO
     * @return
     */
    @PostMapping("v1/updateAcceptStatusReceive")
    ComResponse<Void> updateAcceptStatusReceive(UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO);

    /**
     * 我的热线工单-处理工单详情
     *
     * @param updateAcceptStatusReceiveDTO
     * @return
     */
    @PostMapping("v1/findDWorkOrderHotlineDetails")
    ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO);

    /**
     * 智能工单：我的热线工单-被叫号码查询工单是否存在
     *
     * @param findByCalledPhoneIsEmptyDTO
     * @return
     */
    @PostMapping("v1/findByCalledPhoneIsEmpty")
    ComResponse<Boolean> findByCalledPhoneIsEmpty(FindByCalledPhoneIsEmptyDTO findByCalledPhoneIsEmptyDTO);

    /**
     * 智能工单：我的热线工单-创建处理工单流水
     *
     * @param insertWorkOrderDisposeFlowDTO
     * @return
     */
    @PostMapping("v1/insertWorkOrderDisposeFlow")
    ComResponse<String> insertWorkOrderDisposeFlow(InsertWorkOrderDisposeFlowDTO insertWorkOrderDisposeFlowDTO);

    @PostMapping(value = "v1/queryUnclaimedUsers")
    ComResponse<Page<WorkOrderUnclaimedUserVO>> queryUnclaimedUsers(WorkOrderUnclaimedUserDTO workOrderUnclaimedUserDTO);


    @PostMapping(value = "v1/receiveUsers")
    ComResponse<Boolean> receiveUsers(WorkOrderReceiveDTO workOrderReceiveDTO);


    /**
     * 智能工单:回访工单管理-回收
     *
     * @param recoveryDTO
     * @return
     */
    @PostMapping("v1/recovery")
    ComResponse<Void> recovery(RecoveryDTO recoveryDTO);

    /**
     * 智能工单:回访工单管理-上交
     *
     * @param recoveryDTO
     * @return
     */
    @PostMapping("v1/handIn")
    ComResponse<Void> handIn(RecoveryDTO recoveryDTO);

    /**
     * 智能工单-我的回访工单-接收
     *
     * @param receiveDTO
     * @return
     */
    @PostMapping(value = "v1/receive")
    ComResponse<Void> receive(ReceiveDTO receiveDTO);

    /**
     * 查询顾客旅程
     * @param memberCard
     * @return
     */
    @GetMapping(value = "v1/userRoute")
    ComResponse<List<WorkOrderFlowVO>>  userRoute(@RequestParam(name = "memberCard", required = true)String memberCard);

    /**
     * 智能工单-顾客旅程-根据顾客会员号查询顾客工单信息
     * @param queryVo
     * @return
     */
//    @GetMapping(value = "v1/queryWorkOrder")
//    ComResponse<List<WorkOrderVo>> queryWorkOrder(@RequestParam("memberCard") String memberCard,@RequestParam(name = "year")String year);
    @ApiOperation(value = "智能工单-顾客旅程-根据顾客会员号查询顾客工单信息",notes = "智能工单-顾客旅程-根据顾客会员号查询顾客工单信息")
    @PostMapping(value = "v1/queryWorkOrder")
    ComResponse<Page<WorkOrderVo>> queryWorkOrder(@RequestBody QueryWorkOrderVo queryVo);

    /**
     * 智能工单-我的回访工单-处理工单-提交
     * @param submitWorkOrderDTO
     * @return
     */
    @PostMapping(value = "v1/submitWorkOrder")
    ComResponse<Void> submitWorkOrder(SubmitWorkOrderDTO submitWorkOrderDTO);

    /**
     * 智能工单-回访规则校验第一条
     * @param memberCard
     * @return
     */
    @PostMapping(value = "v1/productDosage")
    ComResponse<Boolean> productDosage(@RequestParam("memberCard") List<String> memberCard);

    /**
     * 智能工单-回访规则校验第3条-新客户回访
     * @param memberCard
     * @param status
     * @return
     */
    @GetMapping(value = "v1/newMember")
    ComResponse<Boolean> newMember(@RequestParam("memberCard") String memberCard,@RequestParam("status") Integer status);
}
