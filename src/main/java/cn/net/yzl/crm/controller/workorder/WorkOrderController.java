package cn.net.yzl.crm.controller.workorder;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.client.workorder.TurnRulnClient;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.dto.ehr.EhrStaff;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.workorder.WorkOrderService;
import cn.net.yzl.crm.utils.HandInUtils;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.workorder.common.Constant;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.db.WorkOrderDisposeFlowBean;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.dto.*;
import cn.net.yzl.workorder.model.enums.WorkOrderTypeEnums;
import cn.net.yzl.workorder.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("workOrder")
@Api(tags = "智能工单")
public class WorkOrderController {

    @Autowired
    private WorkOrderClient workOrderClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private TurnRulnClient turnRulnClient;

    @Autowired
    private MemberFien memberFien;

    /**
     * 查询我的回访分页列表
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isListPage")
    @ApiOperation(value = "查询我的回访分页列表", notes = "查询我的回访分页列表")
    public ComResponse<Page<WorkOrderBean>> isListPage(@RequestBody IsListPageDTO isListPageDTO) {
        isListPageDTO.setStaffNO(QueryIds.userNo.get());
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.isListPage(isListPageDTO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        String productNames = new String();
        for (WorkOrderBean workOrderBean : workOrderBeans) {
            productNames += "," + workOrderBean.getFirstBuyProductCode()+","+workOrderBean.getLastBuyProductCode();
            workOrderBean.setFirstBuyProductCode("");
            workOrderBean.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            Map<String, ProductMainDTO> collect = data.stream().collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
            workOrderBeans.stream().forEach(workOrderBean -> {
                if (workOrderBean.getFirstBuyProductCode().contains(collect.get(workOrderBean.getFirstBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getFirstBuyProductCode())){
                        workOrderBean.setFirstBuyProductCode(workOrderBean.getFirstBuyProductCode()+","+collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }else{
                        workOrderBean.setFirstBuyProductCode(collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }

                }
                if (workOrderBean.getLastBuyProductCode().contains(collect.get(workOrderBean.getLastBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getLastBuyProductCode())){
                        workOrderBean.setLastBuyProductCode(workOrderBean.getLastBuyProductCode()+","+collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }else {
                        workOrderBean.setLastBuyProductCode(collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }

                }
            });
        }
        pageWorkOrderBean.setItems(workOrderBeans);

        return ComResponse.success(pageWorkOrderBean);
    }

    @PostMapping("v1/pageList")
    @ApiOperation(value = "智能工单: 热线工单管理-列表", notes = "智能工单: 热线工单管理-列表")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO) {
        ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageComResponse = workOrderClient.pageList(findWorkOrderHotlinePageListDTO);
        return pageComResponse;
    }

    /**
     * 智能工单：热线工单管理-回收
     *
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "智能工单：热线工单管理-回收", notes = "智能工单：热线工单管理-回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated @RequestBody UpdateRecyclingDTO updateRecyclingDTO) {
        updateRecyclingDTO.setStaffNo(QueryIds.userNo.get());
        updateRecyclingDTO.setOperator(QueryIds.userName.get());
        updateRecyclingDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateRecycling(updateRecyclingDTO);
    }

    @ApiOperation(value = "查询待领取顾客池", notes = "待领取顾客池")
    @PostMapping("v1/queryUnclaimedUsers")
    public ComResponse<Page<WorkOrderUnclaimedUserVO>> queryUnclaimedUsers(@RequestBody   WorkOrderUnclaimedUserDTO workOrderUnclaimedUserDTO){
        ComResponse<Page<WorkOrderUnclaimedUserVO>> pageComResponse = workOrderClient.queryUnclaimedUsers(workOrderUnclaimedUserDTO);

        Page<WorkOrderUnclaimedUserVO> pageWorkOrderUnclaimedUserVO = pageComResponse.getData();
        if (null == pageWorkOrderUnclaimedUserVO) {
            return ComResponse.success();
        }
        List<WorkOrderUnclaimedUserVO> workOrderUnclaimedUserVOS = pageWorkOrderUnclaimedUserVO.getItems();
        String productNames = new String();
        for (WorkOrderUnclaimedUserVO workOrderUnclaimedUserVO : workOrderUnclaimedUserVOS) {
            productNames += "," + workOrderUnclaimedUserVO.getFirstBuyProductCode()+","+workOrderUnclaimedUserVO.getLastBuyProductCode();
            workOrderUnclaimedUserVO.setFirstBuyProductCode("");
            workOrderUnclaimedUserVO.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            Map<String, ProductMainDTO> collect = data.stream().collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
            workOrderUnclaimedUserVOS.stream().forEach(workOrderBean -> {
                if (workOrderBean.getFirstBuyProductCode().contains(collect.get(workOrderBean.getFirstBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getFirstBuyProductCode())){
                        workOrderBean.setFirstBuyProductCode(workOrderBean.getFirstBuyProductCode()+","+collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }else{
                        workOrderBean.setFirstBuyProductCode(collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }

                }
                if (workOrderBean.getLastBuyProductCode().contains(collect.get(workOrderBean.getLastBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getLastBuyProductCode())){
                        workOrderBean.setLastBuyProductCode(workOrderBean.getLastBuyProductCode()+","+collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }else {
                        workOrderBean.setLastBuyProductCode(collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }

                }
            });
        }
        pageWorkOrderUnclaimedUserVO.setItems(workOrderUnclaimedUserVOS);

        return ComResponse.success(pageWorkOrderUnclaimedUserVO);
    }


    @ApiOperation(value = "待领取顾客池-领取", notes = "待领取顾客池-领取")
    @PostMapping("v1/receiveUsers")
    public ComResponse<Boolean> receiveUsers(@RequestBody List<WorkOrderFlowDTO> list) {
        return workOrderService.receiveUsers(list);
    }


    @ApiOperation(value = "回访工单管理列表", notes = "回访工单管理列表")
    @PostMapping(value = "v1/listPage")
    public ComResponse<Page<WorkOrderBean>> listPage(@Validated @RequestBody WorkOrderVisitVO workOrderVisitVO) {
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.listPage(workOrderVisitVO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        String productNames = new String();
        for (WorkOrderBean workOrderBean : workOrderBeans) {
            productNames += "," + workOrderBean.getFirstBuyProductCode()+","+workOrderBean.getLastBuyProductCode();
            workOrderBean.setFirstBuyProductCode("");
            workOrderBean.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            Map<String, ProductMainDTO> collect = data.stream().collect(Collectors.toMap(ProductMainDTO::getProductCode, Function.identity()));
            workOrderBeans.stream().forEach(workOrderBean -> {
                if (workOrderBean.getFirstBuyProductCode().contains(collect.get(workOrderBean.getFirstBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getFirstBuyProductCode())){
                        workOrderBean.setFirstBuyProductCode(workOrderBean.getFirstBuyProductCode()+","+collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }else{
                        workOrderBean.setFirstBuyProductCode(collect.get(workOrderBean.getFirstBuyProductCode()).getName());
                    }

                }
                if (workOrderBean.getLastBuyProductCode().contains(collect.get(workOrderBean.getLastBuyProductCode()).getProductCode())) {
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean.getLastBuyProductCode())){
                        workOrderBean.setLastBuyProductCode(workOrderBean.getLastBuyProductCode()+","+collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }else {
                        workOrderBean.setLastBuyProductCode(collect.get(workOrderBean.getLastBuyProductCode()).getName());
                    }

                }
            });
        }
        pageWorkOrderBean.setItems(workOrderBeans);

        return ComResponse.success(pageWorkOrderBean);
    }

    /**
     * 智能工单：热线工单管理-单数据调整
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-单数据调整", notes = "智能工单：热线工单管理-单数据调整")
    public ComResponse<Void> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO) {
        updateSingleAdjustDTO.setStaffNo(QueryIds.userNo.get());
        updateSingleAdjustDTO.setOperator(QueryIds.userName.get());
        updateSingleAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateSingleAdjustDTO.setAcceptStatus(2);//人工触发 改为已接受
        return workOrderClient.updateSingleAdjust(updateSingleAdjustDTO);
    }

    @ApiOperation(value = "查询所有用户首次购买商品", notes = "查询所有用户首次购买商品")
    @GetMapping("v1/queryFirstProduct")
    public ComResponse<List<ProductMainDTO>> queryFirstProduct() {
        String data = workOrderClient.queryFirstProduct().getData();
        return productClient.queryByProductCodes(data);
    }

    @ApiOperation(value = "查询所有用户最后一次购买商品", notes = "查询所有用户最后一次购买商品")
    @GetMapping("v1/queryLastProduct")
    public ComResponse<List<ProductMainDTO>> queryLastProduct() {
        String data = workOrderClient.queryLastProduct().getData();
        return productClient.queryByProductCodes(data);
    }

    /**
     * 智能工单：热线工单管理-多数据调整
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-多数据调整", notes = "智能工单：热线工单管理-多数据调整")
    public ComResponse<Void> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO) {
        updateMoreAdjustDTO.setAcceptStatus(2);//人工触发 改为已接受
        updateMoreAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateMoreAdjustDTO.setOperatorCode(QueryIds.userNo.get());
        updateMoreAdjustDTO.setOperator(QueryIds.userName.get());
        return workOrderClient.updateMoreAdjust(updateMoreAdjustDTO);
    }

    @ApiOperation(value = "回访工单单条分配工单", notes = "回访工单单条分配工单")
    @PostMapping(value = "v1/adjustment")
    public ComResponse<Void> adjustment(@RequestBody UpdateWorkOrderVisitDTO updateWorkOrderVisitDTO) {
        updateWorkOrderVisitDTO.setCreateId(QueryIds.userNo.get());
        updateWorkOrderVisitDTO.setCreateName(QueryIds.userName.get());
        return workOrderClient.adjustment(updateWorkOrderVisitDTO);
    }

    @ApiOperation(value = "回访工单多条分配工单", notes = "回访工单多条分配工单")
    @PostMapping(value = "v1/batchAdjustment")
    public ComResponse<Void> batchAdjustment(@RequestBody UpdateBatchDTO updateBatchDTO) {
        updateBatchDTO.setCreateId(QueryIds.userNo.get());
        updateBatchDTO.setCreateName(QueryIds.userName.get());
        List<UpdateBatchWorkOrderDTO> updateBatchWorkOrderDTOS = updateBatchDTO.getUpdateBatchWorkOrderDTOS();
        int count = 0;
        for (UpdateBatchWorkOrderDTO updateBatchWorkOrderDTO : updateBatchWorkOrderDTOS) {
            count += updateBatchWorkOrderDTO.getCount();
        }

        if (count != updateBatchDTO.getCount()) {
            return ComResponse.nodata("总线数必须和分配线数相等!");
        }
        return workOrderClient.batchAdjustment(updateBatchDTO);
    }

    @PostMapping("v1/findMyWorkOrderHotlinePageList")
    @ApiOperation(value = "智能工单：我的热线工单-列表", notes = "智能工单：我的热线工单-列表")
    public ComResponse<Page<MyWorkOrderHotlineListVO>> findMyWorkOrderHotlinePageList(@RequestBody MyWorkOrderHotlineListDTO myWorkOrderHotlineListDTO) {
        String userId = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userId)) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "用户校验失败");
        }
        myWorkOrderHotlineListDTO.setStaffNo(userId);
        return workOrderClient.findMyWorkOrderHotlinePageList(myWorkOrderHotlineListDTO);
    }

    /**
     * 智能工单：我的热线工单-接收
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateAcceptStatusReceive")
    @ApiOperation(value = "智能工单：我的热线工单-接收", notes = "智能工单：我的热线工单-接收")
    public ComResponse<Void> updateAcceptStatusReceive(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        updateAcceptStatusReceiveDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateAcceptStatusReceiveDTO.setOperator(QueryIds.userName.get());
        updateAcceptStatusReceiveDTO.setOperatorCode(QueryIds.userNo.get());
        return workOrderClient.updateAcceptStatusReceive(updateAcceptStatusReceiveDTO);
    }

    /**
     * 智能工单：我的热线工单-处理工单详情
     *
     * @param
     * @return
     */
    @PostMapping("v1/findDWorkOrderHotlineDetails")
    @ApiOperation(value = "我的热线工单-处理工单详情", notes = "我的热线工单-处理工单详情")
    public ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        return workOrderClient.findDWorkOrderHotlineDetails(updateAcceptStatusReceiveDTO);
    }

    /**
     * 智能工单：我的热线工单-被叫号码查询工单是否存在
     *
     * @return
     */
    @PostMapping("v1/findByCalledPhoneIsEmpty")
    @ApiOperation(value = "智能工单：我的热线工单-被叫号码查询工单是否存在", notes = "智能工单：我的热线工单-被叫号码查询工单是否存在")
    public ComResponse<Boolean> findByCalledPhoneIsEmpty(@Validated @RequestBody FindByCalledPhoneIsEmptyDTO findByCalledPhoneIsEmptyDTO) {
        return workOrderClient.findByCalledPhoneIsEmpty(findByCalledPhoneIsEmptyDTO);
    }

    /**
     * 智能工单：热线工单管理-可分配员工
     *
     * @param
     * @return
     */
    @PostMapping("v1/getDistributionStaff")
    @ApiOperation(value = "智能工单：热线工单管理-可分配员工", notes = "智能工单：热线工单管理-可分配员工")
    public ComResponse<Page<EhrStaff>> getDistributionStaff(@RequestBody GetDistributionStaffDTO getDistributionStaffDTO) {
        String userId = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userId)) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "用户校验失败");
        }
        getDistributionStaffDTO.setStaffNo(userId);
        return workOrderService.getDistributionStaff(getDistributionStaffDTO);
    }


    /**
     * 智能工单：我的热线工单-创建处理工单流水
     *
     * @return
     */
    @PostMapping("v1/insertWorkOrderDisposeFlow")
    @ApiOperation(value = "智能工单：我的热线工单-创建处理工单流水", notes = "智能工单：我的热线工单-创建处理工单流水")
    public ComResponse<String> insertWorkOrderDisposeFlow(@Validated @RequestBody InsertWorkOrderDisposeFlowDTO insertWorkOrderDisposeFlowDTO) {
        insertWorkOrderDisposeFlowDTO.setCreateId(QueryIds.userNo.get());
        insertWorkOrderDisposeFlowDTO.setCreateName(QueryIds.userName.get());
        insertWorkOrderDisposeFlowDTO.setUpdateId(QueryIds.userNo.get());
        insertWorkOrderDisposeFlowDTO.setUpdateName(QueryIds.userName.get());
        return workOrderClient.insertWorkOrderDisposeFlow(insertWorkOrderDisposeFlowDTO);
    }

    /**
     * 智能工单:回访工单管理-回收
     *
     * @param recoveryDTO
     * @return
     */
    @ApiOperation(value = "回访工单管理-回收", notes = "回访工单管理-回收")
    @PostMapping(value = "v1/recovery")
    public ComResponse<Void> recovery(@RequestBody RecoveryDTO recoveryDTO) {
        recoveryDTO.setStaffNo(QueryIds.userNo.get());
        recoveryDTO.setStaffName(QueryIds.userName.get());
        return workOrderClient.recovery(recoveryDTO);
    }

    /**
     * 智能工单:回访工单管理-上交
     *
     * @param recoveryDTO
     * @return
     */
    @ApiOperation(value = "回访工单管理-上交", notes = "回访工单管理-上交")
    @PostMapping(value = "v1/handIn")
    public ComResponse<Void> handIn(@RequestBody RecoveryDTO recoveryDTO) {
        recoveryDTO.setStaffNo(QueryIds.userNo.get());
        recoveryDTO.setStaffName(QueryIds.userName.get());
        return workOrderClient.handIn(recoveryDTO);
    }

    /**
     * 智能工单-我的回访工单-接收
     *
     * @param receiveDTO
     * @return
     */
    @ApiOperation(value = "智能工单-我的回访工单-接收", notes = "智能工单-我的回访工单-接收")
    @PostMapping(value = "v1/receive")
    public ComResponse<Void> receive(@RequestBody ReceiveDTO receiveDTO) {
        return workOrderClient.receive(receiveDTO);
    }

    /**
     * 智能工单：我的热线工单-修改处理工单流水
     * @return
     */
//    @PostMapping("v1/updateWorkOrderDisposeFlow")
//    @ApiOperation(value = "智能工单：我的热线工单-修改处理工单流水", notes = "智能工单：我的热线工单-修改处理工单流水")
//    public ComResponse<String> updateWorkOrderDisposeFlow(@RequestBody WorkOrderDisposeFlowBean workOrderDisposeFlowBean){
//        return workOrderClient.updateWorkOrderDisposeFlow(workOrderDisposeFlowBean);
//    }

    @ApiOperation(value = "智能工单-我的回访工单-单条上交",notes = "智能工单-我的回访工单-单条上交")
    @PostMapping(value = "v1/isHandIn")
    public ComResponse<Boolean> isHandIn(@RequestBody IsHandInDTO isHandInDTO){
        isHandInDTO.setStaffNo(QueryIds.userNo.get());
        isHandInDTO.setStaffName(QueryIds.userName.get());
        ComResponse<List<WorkOrderRuleConfigBean>> listComResponse = turnRulnClient.submissionRules(1, 2, 1, 0);
        if(CollectionUtils.isEmpty(listComResponse.getData())){
            return ComResponse.success(Boolean.TRUE);
        }
        List<WorkOrderRuleConfigBean> data = listComResponse.getData();
        if(null != isHandInDTO.getSouce() && isHandInDTO.getSouce() == 2){
            WorkOrderRuleConfigBean workOrderRuleConfigBean1 = null;
            for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data){
                if(workOrderRuleConfigBean.getId() == 7)
                    workOrderRuleConfigBean1 = workOrderRuleConfigBean;
            }
            if(null != workOrderRuleConfigBean1){
                isHandInDTO.setApplyUpStatus(1);
                turnRulnClient.rulesHandedIn(isHandInDTO);
                return ComResponse.success(Boolean.TRUE);
            }
            RecoveryDTO recoveryDTO = new RecoveryDTO();
            recoveryDTO.setStaffName(isHandInDTO.getStaffName());
            recoveryDTO.setStaffNo(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateId(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateName(isHandInDTO.getStaffName());
            recoveryDTO.setCode(isHandInDTO.getCode());
            recoveryDTO.setMemberCard(isHandInDTO.getMemberCard());
            recoveryDTO.setMemberName(isHandInDTO.getMemberName());
            workOrderClient.handIn(recoveryDTO);
            return ComResponse.success(Boolean.TRUE);
        }
        WorkOrderRuleConfigBean wORCBean = null;
        HandInUtils handInUtils = new HandInUtils();
        Boolean flag = Boolean.FALSE;
        for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
            wORCBean = workOrderRuleConfigBean;
            switch (workOrderRuleConfigBean.getId()){
                case 1:
                    flag = handInUtils.emptyNumberShutdown(isHandInDTO,wORCBean);
                    break;

                case 2:
                    flag = handInUtils.unableToContact(isHandInDTO,wORCBean);
                    break;

                case 3:
                    flag = handInUtils.customerRefusedToVisit(isHandInDTO,wORCBean);
                    break;

                case 4:
                    flag = handInUtils.customerRefund(isHandInDTO,wORCBean);
                    break;

                case 5:
                    flag = handInUtils.dormantCustomers(isHandInDTO,wORCBean);
                    break;

                case 6:
                    flag = handInUtils.mCustomerLExceeded(isHandInDTO,wORCBean);
                    break;

                case 8:
                    flag = handInUtils.overtimeReturnVisit(isHandInDTO,wORCBean);
                    break;

                default:{}
                if(BooleanUtils.isTrue(flag)){
                    break;
                }
            }
        }
        if(flag){
            RecoveryDTO recoveryDTO = new RecoveryDTO();
            recoveryDTO.setStaffName(isHandInDTO.getStaffName());
            recoveryDTO.setStaffNo(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateId(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateName(isHandInDTO.getStaffName());
            recoveryDTO.setCode(isHandInDTO.getCode());
            recoveryDTO.setMemberCard(isHandInDTO.getMemberCard());
            recoveryDTO.setMemberName(isHandInDTO.getMemberName());
            workOrderClient.handIn(recoveryDTO);
        }else {
            isHandInDTO.setApplyUpStatus(1);
            turnRulnClient.rulesHandedIn(isHandInDTO);
        }
        return ComResponse.success(flag);
    }

    @ApiOperation(value = "智能工单-顾客旅程-根据顾客会员号查询顾客工单信息",notes = "智能工单-顾客旅程-根据顾客会员号查询顾客工单信息")
    @GetMapping(value = "v1/queryWorkOrder")
    public ComResponse<List<WorkOrderVo>> queryWorkOrder(@ApiParam(value = "顾客会员号")@RequestParam(value = "memberCard")String memberCard){
        return workOrderClient.queryWorkOrder(memberCard);
    }
    @ApiOperation(value = "查询顾客旅程",notes = "查询顾客旅程")
    @GetMapping(value = "v1/userRoute")
    public ComResponse<List<WorkOrderFlowVO>> userRoute(@RequestParam(name = "memberCard",required = true)String memberCard){
        return workOrderClient.userRoute(memberCard);
    }

    @ApiOperation(value = "智能工单-我的回访工单-处理工单-提交",notes = "智能工单-我的回访工单-处理工单-提交")
    @PostMapping(value = "v1/submitWorkOrder")
    public ComResponse<Void> submitWorkOrder(@Validated @RequestBody SubmitWorkOrderDTO submitWorkOrderDTO){
        String userNo = QueryIds.userNo.get();
        String userName = QueryIds.userName.get();
        submitWorkOrderDTO.setUpdateId(userNo);
        submitWorkOrderDTO.setUpdateName(userName);
        //解析长字符
        String informationGoods = submitWorkOrderDTO.getWorkOrderDisposeFlowSubBean().getInformationGoods();
        if(StringUtils.isEmpty(informationGoods)) {
            JSONObject jsonObject = new JSONObject(informationGoods);
            JSONArray hotline400= new JSONArray(jsonObject.get(WorkOrderTypeEnums.HOTLINE_400.getName()));
            List<ProductConsultationInsertVO> productConsultationInsertVOS = new ArrayList<ProductConsultationInsertVO>();
            hotline400.stream().forEach(o -> {
                ProductConsultationInsertVO productConsultationInsertVO = new ProductConsultationInsertVO();
                productConsultationInsertVO.setMemberCard(submitWorkOrderDTO.getWorkOrderDisposeFlowSubBean().getMemberCard());
                JSONObject object = new JSONObject(o);
                Object name = object.get("name");
                Object code = object.get("code");
                if(!StringUtils.isEmpty(name)) productConsultationInsertVO.setProductName((String) name);
                if(!StringUtils.isEmpty(code)) productConsultationInsertVO.setProductCode((String) code);
                productConsultationInsertVO.setConsultationTime(new Date());
                productConsultationInsertVOS.add(productConsultationInsertVO);
            });
            JSONArray ladderSales= new JSONArray(jsonObject.get(WorkOrderTypeEnums.LADDER_SALES.getName()));
            ladderSales.stream().forEach(o -> {
                ProductConsultationInsertVO productConsultationInsertVO = new ProductConsultationInsertVO();
                productConsultationInsertVO.setMemberCard(submitWorkOrderDTO.getWorkOrderDisposeFlowSubBean().getMemberCard());
                JSONObject object = new JSONObject(o);
                Object name = object.get("name");
                Object code = object.get("code");
                if(!StringUtils.isEmpty(name)) productConsultationInsertVO.setProductName((String) name);
                if(!StringUtils.isEmpty(code)) productConsultationInsertVO.setProductCode((String) code);
                productConsultationInsertVO.setConsultationTime(new Date());
                productConsultationInsertVOS.add(productConsultationInsertVO);
            });
            JSONArray regularReview= new JSONArray(jsonObject.get(WorkOrderTypeEnums.REGULAR_REVIEW.getName()));
            regularReview.stream().forEach(o -> {
                ProductConsultationInsertVO productConsultationInsertVO = new ProductConsultationInsertVO();
                productConsultationInsertVO.setMemberCard(submitWorkOrderDTO.getWorkOrderDisposeFlowSubBean().getMemberCard());
                JSONObject object = new JSONObject(o);
                Object name = object.get("name");
                Object code = object.get("code");
                if(!StringUtils.isEmpty(name)) productConsultationInsertVO.setProductName((String) name);
                if(!StringUtils.isEmpty(code)) productConsultationInsertVO.setProductCode((String) code);
                productConsultationInsertVO.setConsultationTime(new Date());
                productConsultationInsertVOS.add(productConsultationInsertVO);
            });
            JSONArray eventMarketing= new JSONArray(jsonObject.get(WorkOrderTypeEnums.EVENT_MARKETING.getName()));
            eventMarketing.stream().forEach(o -> {
                ProductConsultationInsertVO productConsultationInsertVO = new ProductConsultationInsertVO();
                productConsultationInsertVO.setMemberCard(submitWorkOrderDTO.getWorkOrderDisposeFlowSubBean().getMemberCard());
                JSONObject object = new JSONObject(o);
                Object name = object.get("name");
                Object code = object.get("code");
                if(!StringUtils.isEmpty(name)) productConsultationInsertVO.setProductName((String) name);
                if(!StringUtils.isEmpty(code)) productConsultationInsertVO.setProductCode((String) code);
                productConsultationInsertVO.setConsultationTime(new Date());
                productConsultationInsertVOS.add(productConsultationInsertVO);
            });
            if(!CollectionUtils.isEmpty(productConsultationInsertVOS)){
                memberFien.addProductConsultation(productConsultationInsertVOS);
            }
        }
        return workOrderClient.submitWorkOrder(submitWorkOrderDTO);
    }

    @ApiOperation(value = "回访规则校验Job",notes = "回访规则校验Job")
    @GetMapping(value = "v1/returnVisitRules")
    public ComResponse<Boolean> returnVisitRules(){
        ComResponse<List<WorkOrderRuleConfigBean>> listComResponse = turnRulnClient.submissionRules(2, 2, 1, 0);
        List<WorkOrderRuleConfigBean> data = listComResponse.getData();
        if (CollectionUtils.isEmpty(data)){
            return ComResponse.success();
        }
        for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
            switch (workOrderRuleConfigBean.getId()){
                case 9:
                    break;
                case 10:
                    String paramsValue = workOrderRuleConfigBean.getParamsValue();
                    workOrderClient.visitDateLtCurrentDate(paramsValue);
                    break;
            }
        }
        return ComResponse.success();
    }

    @ApiOperation(value = "产品服用量小于规则配置:需回访",notes = "产品服用量小于规则配置:需回访")
    @PostMapping(value = "v1/productDosage")
    public ComResponse<Boolean> productDosage(@ApiParam("顾客会员号")@RequestParam("memberCard") List<String> memberCard){
        return workOrderClient.productDosage(memberCard);
    }

    @ApiOperation(value = "新客户回访",notes = "新客户回访")
    @GetMapping(value = "v1/newMember")
    public ComResponse<Boolean> newMember(@ApiParam("顾客会员号")@RequestParam("memberCard") String memberCard,@ApiParam("状态:1新顾客;2:老顾客")@RequestParam("status")Integer status ){
        return workOrderClient.newMember(memberCard,status);
    }
}
