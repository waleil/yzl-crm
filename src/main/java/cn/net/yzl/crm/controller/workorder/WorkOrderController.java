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
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.dto.workorder.GetDistributionStaffDTO;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.workorder.WorkOrderService;
import cn.net.yzl.crm.utils.HandInUtils;
import cn.net.yzl.product.model.vo.product.dto.ProductDetailVO;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import cn.net.yzl.workorder.common.CommonConstants;
import cn.net.yzl.workorder.common.Constant;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.db.WorkOrderRuleConfigBean;
import cn.net.yzl.workorder.model.dto.FindByCalledPhoneIsEmptyDTO;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.InsertWorkOrderDisposeFlowDTO;
import cn.net.yzl.workorder.model.dto.IsHandInDTO;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import cn.net.yzl.workorder.model.dto.MyWorkOrderHotlineListDTO;
import cn.net.yzl.workorder.model.dto.ReceiveDTO;
import cn.net.yzl.workorder.model.dto.RecoveryDTO;
import cn.net.yzl.workorder.model.dto.SubmitWorkOrderDTO;
import cn.net.yzl.workorder.model.dto.UpdateAcceptStatusReceiveDTO;
import cn.net.yzl.workorder.model.dto.UpdateBatchDTO;
import cn.net.yzl.workorder.model.dto.UpdateBatchWorkOrderDTO;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustSubTDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateWorkOrderVisitDTO;
import cn.net.yzl.workorder.model.dto.WorkOrderFlowDTO;
import cn.net.yzl.workorder.model.dto.WorkOrderUnclaimedUserDTO;
import cn.net.yzl.workorder.model.enums.WorkOrderTypeEnums;
import cn.net.yzl.workorder.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private HandInUtils handInUtils;

    /**
     * 查询我的回访分页列表
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isVisitListPage")
    @ApiOperation(value = "我的回访工单-列表", notes = "我的回访工单-列表")
    public ComResponse<Page<WorkOrderBean>> isVisitListPage(@RequestBody IsListPageDTO isListPageDTO) {
        isListPageDTO.setStaffNO(QueryIds.userNo.get());
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.isVisitListPage(isListPageDTO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        List<String> lastBuyProductCodes = new ArrayList<>();
        List<String> firstBuyProductCodes = new ArrayList<>();
        String productNames = new String();
        for (int i = 0 ; i < workOrderBeans.size();i++) {
            WorkOrderBean workOrderBean = workOrderBeans.get(i);
            String lastBuyProductCode = workOrderBean.getLastBuyProductCode();
            String firstBuyProductCode = workOrderBean.getFirstBuyProductCode();
            firstBuyProductCodes.add(firstBuyProductCode);
            lastBuyProductCodes.add(lastBuyProductCode);
            productNames += "," + workOrderBean.getFirstBuyProductCode()+","+workOrderBean.getLastBuyProductCode();
            workOrderBean.setFirstBuyProductCode("");
            workOrderBean.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            for(int i = 0 ; i < workOrderBeans.size();i++) {
                String lastBuyProductCode = lastBuyProductCodes.get(i);
                String firstBuyProductCode = firstBuyProductCodes.get(i);
                WorkOrderBean workOrderBean1 = workOrderBeans.get(i);
                for(ProductMainDTO productMainDTO : data) {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(firstBuyProductCode) && firstBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getFirstBuyProductCode())) {
                            workOrderBean1.setFirstBuyProductCode(workOrderBean1.getFirstBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setFirstBuyProductCode(productMainDTO.getName());
                        }

                    }
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(lastBuyProductCode) &&lastBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getLastBuyProductCode())) {
                            workOrderBean1.setLastBuyProductCode(workOrderBean1.getLastBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setLastBuyProductCode(productMainDTO.getName());
                        }

                    }
                }

            }
        }
        pageWorkOrderBean.setItems(workOrderBeans);

        return ComResponse.success(pageWorkOrderBean);
    }

    @PostMapping("v1/pageList")
    @ApiOperation(value = "热线工单管理-列表", notes = "热线工单管理-列表")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO) {
        //是否是分线监控
        Integer isMonitoring = findWorkOrderHotlinePageListDTO.getIsMonitoring();
        if(0 == isMonitoring){
            //获取当前用户部门，以及员工
            ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            //智能工单--查询自己部门的数据
            if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
                return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
            }
            StaffImageBaseInfoDto data = detailsByNo.getData();
            Integer departId = data.getDepartId();//当前登录人部门编码
            findWorkOrderHotlinePageListDTO.setDeptId(departId);
        }
        //智能派单--默认查询所有部门的数据
        ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageComResponse = workOrderClient.pageList(findWorkOrderHotlinePageListDTO);
        return pageComResponse;

    }

    /**
     * 智能工单：热线工单管理-回收
     *
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "热线工单管理-回收", notes = "热线工单管理-回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated @RequestBody UpdateRecyclingDTO updateRecyclingDTO) {
        //获取当前用户部门，以及员工
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        //智能工单--查询自己部门的数据
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateRecyclingDTO.setStaffNo(data.getStaffNo());
        updateRecyclingDTO.setOperator(data.getName());
        updateRecyclingDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateRecycling(updateRecyclingDTO);
    }

    @ApiOperation(value = "待领取顾客池-列表", notes = "待领取顾客池-列表")
    @PostMapping("v1/queryUnclaimedUsers")
    public ComResponse<Page<WorkOrderUnclaimedUserVO>> queryUnclaimedUsers(@RequestBody   WorkOrderUnclaimedUserDTO workOrderUnclaimedUserDTO){
        ComResponse<Page<WorkOrderUnclaimedUserVO>> pageComResponse = workOrderClient.queryUnclaimedUsers(workOrderUnclaimedUserDTO);

        Page<WorkOrderUnclaimedUserVO> pageWorkOrderUnclaimedUserVO = pageComResponse.getData();
        if (null == pageWorkOrderUnclaimedUserVO) {
            return ComResponse.success();
        }
        List<WorkOrderUnclaimedUserVO> workOrderUnclaimedUserVOS = pageWorkOrderUnclaimedUserVO.getItems();
        String productNames = new String();
        List<String> lastBuyProductCodes = new ArrayList<>();
        List<String> firstBuyProductCodes = new ArrayList<>();
        for (WorkOrderUnclaimedUserVO workOrderUnclaimedUserVO : workOrderUnclaimedUserVOS) {
            String lastBuyProductCode = workOrderUnclaimedUserVO.getLastBuyProductCode();
            String firstBuyProductCode = workOrderUnclaimedUserVO.getFirstBuyProductCode();
            firstBuyProductCodes.add(firstBuyProductCode);
            lastBuyProductCodes.add(lastBuyProductCode);
            productNames += "," + workOrderUnclaimedUserVO.getFirstBuyProductCode()+","+workOrderUnclaimedUserVO.getLastBuyProductCode();
            workOrderUnclaimedUserVO.setFirstBuyProductCode("");
            workOrderUnclaimedUserVO.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            for(int i = 0 ; i < workOrderUnclaimedUserVOS.size();i++) {
                String lastBuyProductCode = lastBuyProductCodes.get(i);
                String firstBuyProductCode = firstBuyProductCodes.get(i);
                WorkOrderUnclaimedUserVO workOrderBean1 = workOrderUnclaimedUserVOS.get(i);
                for(ProductMainDTO productMainDTO : data) {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(firstBuyProductCode) && firstBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getFirstBuyProductCode())) {
                            workOrderBean1.setFirstBuyProductCode(workOrderBean1.getFirstBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setFirstBuyProductCode(productMainDTO.getName());
                        }

                    }
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(lastBuyProductCode) &&lastBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getLastBuyProductCode())) {
                            workOrderBean1.setLastBuyProductCode(workOrderBean1.getLastBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setLastBuyProductCode(productMainDTO.getName());
                        }

                    }
                }

            }
        }
        pageWorkOrderUnclaimedUserVO.setItems(workOrderUnclaimedUserVOS);

        return ComResponse.success(pageWorkOrderUnclaimedUserVO);
    }


    @ApiOperation(value = "待领取顾客池-领取", notes = "待领取顾客池-领取")
    @PostMapping("v1/receiveUsers")
    public ComResponse<Boolean> receiveUsers(@RequestBody List<WorkOrderFlowDTO> list) {
        return workOrderService.receiveUsers(list);
    }


    @ApiOperation(value = "回访工单管理-列表", notes = "回访工单管理-列表")
    @PostMapping(value = "v1/visitAdministrationListPage")
    public ComResponse<Page<WorkOrderBean>> visitAdministrationListPage(@Validated @RequestBody WorkOrderVisitVO workOrderVisitVO) {
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        workOrderVisitVO.setDeptId(detailsByNo.getData().getDepartId());
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.visitAdministrationListPage(workOrderVisitVO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        List<String> lastBuyProductCodes = new ArrayList<>();
        List<String> firstBuyProductCodes = new ArrayList<>();
        String productNames = new String();
        for (int i = 0 ; i < workOrderBeans.size();i++) {
            WorkOrderBean workOrderBean = workOrderBeans.get(i);
            String lastBuyProductCode = workOrderBean.getLastBuyProductCode();
            String firstBuyProductCode = workOrderBean.getFirstBuyProductCode();
            firstBuyProductCodes.add(firstBuyProductCode);
            lastBuyProductCodes.add(lastBuyProductCode);
            productNames += "," + workOrderBean.getFirstBuyProductCode()+","+workOrderBean.getLastBuyProductCode();
            workOrderBean.setFirstBuyProductCode("");
            workOrderBean.setLastBuyProductCode("");
        }
        productNames = productNames.substring(1);
        List<ProductMainDTO> data = productClient.queryByProductCodes(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            for(int i = 0 ; i < workOrderBeans.size();i++) {
                String lastBuyProductCode = lastBuyProductCodes.get(i);
                String firstBuyProductCode = firstBuyProductCodes.get(i);
                WorkOrderBean workOrderBean1 = workOrderBeans.get(i);
                for(ProductMainDTO productMainDTO : data) {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(firstBuyProductCode) && firstBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getFirstBuyProductCode())) {
                            workOrderBean1.setFirstBuyProductCode(workOrderBean1.getFirstBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setFirstBuyProductCode(productMainDTO.getName());
                        }

                    }
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(lastBuyProductCode) &&lastBuyProductCode.contains(productMainDTO.getProductCode())) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(workOrderBean1.getLastBuyProductCode())) {
                            workOrderBean1.setLastBuyProductCode(workOrderBean1.getLastBuyProductCode() + "," + productMainDTO.getName());
                        } else {
                            workOrderBean1.setLastBuyProductCode(productMainDTO.getName());
                        }

                    }
                }

            }
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
    @ApiOperation(value = "热线工单管理-单数据调整", notes = "热线工单管理-单数据调整")
    public ComResponse<Void> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO) {
        //获取当前登陆人信息
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateSingleAdjustDTO.setOperatorCode(data.getStaffNo());//操作人编码
        updateSingleAdjustDTO.setOperator(data.getName());//操作人名称
        //获取被分配人的信息
        ComResponse<StaffImageBaseInfoDto> detailsByNoOne = ehrStaffClient.getDetailsByNo(updateSingleAdjustDTO.getStaffNo());
        if(!StringUtils.isEmpty(detailsByNoOne) && !StringUtils.isEmpty(detailsByNoOne.getData())){
            StaffImageBaseInfoDto dataOne = detailsByNoOne.getData();
            updateSingleAdjustDTO.setStaffNo(dataOne.getStaffNo());//被分配人编码
            updateSingleAdjustDTO.setStaffName(dataOne.getName());//被分配人名称
            updateSingleAdjustDTO.setStaffLevel(StringUtils.isEmpty(dataOne.getPostLevelName())?"":String.valueOf(dataOne.getPostLevelName()));//被分配人级别
            updateSingleAdjustDTO.setDeptId(dataOne.getDepartId());//被分配人部门id
            updateSingleAdjustDTO.setDeptName(dataOne.getDepartName());//被分配人部门名称
        }

        updateSingleAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateSingleAdjustDTO.setAcceptStatus(1);//人工触发 改为未接受（热线的不管自动分配还是人工分配都需要接收）
        return workOrderClient.updateSingleAdjust(updateSingleAdjustDTO).setMessage("成功");
    }

    @ApiOperation(value = "查询所有用户首次购买商品", notes = "查询所有用户首次购买商品")
    @GetMapping("v1/queryFirstProduct")
    public ComResponse<List<ProductMainDTO>> queryFirstProduct() {
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        StaffImageBaseInfoDto data1 = detailsByNo.getData();
        Integer deptId = null;
        if(null != data1){
            deptId = data1.getDepartId();
        }
        String data = workOrderClient.queryFirstProduct(deptId).getData();
        return productClient.queryByProductCodes(data);
    }

    @ApiOperation(value = "所有用户最后一次购买商品-列表", notes = "所有用户最后一次购买商品-列表")
    @GetMapping("v1/queryLastProduct")
    public ComResponse<List<ProductMainDTO>> queryLastProduct() {
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        StaffImageBaseInfoDto data1 = detailsByNo.getData();
        Integer deptId = null;
        if(null != data1){
            deptId = data1.getDepartId();
        }
        String data = workOrderClient.queryLastProduct(deptId).getData();
        return productClient.queryByProductCodes(data);
    }

    /**
     * 智能工单：热线工单管理-多数据调整
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "热线工单管理-多数据调整", notes = "热线工单管理-多数据调整")
    public ComResponse<Void> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO) {
        //获取当前登陆人信息
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateMoreAdjustDTO.setOperatorCode(data.getStaffNo());
        updateMoreAdjustDTO.setOperator(data.getName());
        //获取被分配人的信息
        List<UpdateMoreAdjustSubTDTO> names = updateMoreAdjustDTO.getNames();
        if(!CollectionUtils.isEmpty(names)){
            for (int i = 0; i < names.size(); i++) {
                ComResponse<StaffImageBaseInfoDto> detailsByNoOne = ehrStaffClient.getDetailsByNo(names.get(i).getStaffNo());
                if(!StringUtils.isEmpty(detailsByNoOne) && !StringUtils.isEmpty(detailsByNoOne.getData())){
                    StaffImageBaseInfoDto dataOne = detailsByNoOne.getData();
                    names.get(i).setStaffNo(dataOne.getStaffNo());//被分配人编码
                    names.get(i).setStaffName(dataOne.getName());//被分配人名称
                    names.get(i).setStaffLevel(StringUtils.isEmpty(dataOne.getPostLevelName())?"":String.valueOf(dataOne.getPostLevelName()));//被分配人级别
                    names.get(i).setDeptId(dataOne.getDepartId());//被分配人部门id
                    names.get(i).setDeptName(dataOne.getDepartName());//被分配人部门名称
                    names.get(i).setAcceptStatus(1);//不管是人工还是自动分配都是未接收
                } else {
                    return ComResponse.fail(ComResponse.ERROR_STATUS,"被分配的员工信息不存在");
                }
            }
        }
        updateMoreAdjustDTO.setAcceptStatus(1);//人工触发 改为未接收，不管是人工还是自动分配都是未接收
        updateMoreAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateMoreAdjust(updateMoreAdjustDTO).setMessage("成功");
    }

    @ApiOperation(value = "回访工单管理-单条分配", notes = "我的回访工单-单条分配")
    @PostMapping(value = "v1/adjustment")
    public ComResponse<Void> adjustment(@RequestBody UpdateWorkOrderVisitDTO updateWorkOrderVisitDTO) {
        updateWorkOrderVisitDTO.setCreateId(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        updateWorkOrderVisitDTO.setCreateName(detailsByNo.getData().getName());
        return workOrderClient.adjustment(updateWorkOrderVisitDTO);
    }

    @ApiOperation(value = "回访工单管理-多条分配", notes = "我的回访工单-多条分配")
    @PostMapping(value = "v1/batchAdjustment")
    public ComResponse<Void> batchAdjustment(@RequestBody UpdateBatchDTO updateBatchDTO) {
        updateBatchDTO.setCreateId(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        updateBatchDTO.setCreateName(detailsByNo.getData().getName());
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
    @ApiOperation(value = "我的热线工单-列表", notes = "我的热线工单-列表")
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
    @ApiOperation(value = "我的热线工单-接收", notes = "我的热线工单-接收")
    public ComResponse<Void> updateAcceptStatusReceive(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        //获取当前登陆人信息
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateAcceptStatusReceiveDTO.setOperatorCode(data.getStaffNo());
        updateAcceptStatusReceiveDTO.setOperator(data.getName());
        updateAcceptStatusReceiveDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateAcceptStatusReceive(updateAcceptStatusReceiveDTO);
    }

    /**
     * 智能工单：我的热线工单-处理工单详情
     *
     * @param
     * @return
     */
    @PostMapping("v1/findDWorkOrderHotlineDetails")
    @ApiOperation(value = "热线&&回访-处理工单详情", notes = "热线&&回访-处理工单详情")
    public ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        ComResponse<FindDWorkOrderHotlineDetailsVO> dWorkOrderHotlineDetails = workOrderClient.findDWorkOrderHotlineDetails(updateAcceptStatusReceiveDTO);
        FindDWorkOrderHotlineDetailsVO data = dWorkOrderHotlineDetails.getData();
        if(!StringUtils.isEmpty(data)){
            //获取详情中的阶梯商品，通过商品编码获取商品列表
            String firstBuyProductCode = data.getFirstBuyProductCode();//商品编码，多个是商品逗号拼接
            if(!StringUtils.isEmpty(firstBuyProductCode)) {
                String[] split = firstBuyProductCode.split(",");
                //获取数组
                Stream<String> distinct = Arrays.stream(split).distinct();
                //创建Map集合
                List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                distinct.forEach(s -> {
                    //循环调用商品详情服务
                    ComResponse<ProductDetailVO> productDetailVOComResponse = productClient.queryProductDetail(s);
                    ProductDetailVO productDetailVO = productDetailVOComResponse.getData();
                    if(!StringUtils.isEmpty(productDetailVO)){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("productDetail",productDetailVO);
                        maps.add(map);
                    }
                });
                data.setProductDetailMaps(maps);
            }
        }
        return dWorkOrderHotlineDetails;
    }

    /**
     * 智能工单：我的热线工单-被叫号码查询工单是否存在
     *
     * @return
     */
    @PostMapping("v1/findByCalledPhoneIsEmpty")
    @ApiOperation(value = "热线&&回访-被叫号码查询工单是否存在", notes = "热线&&回访-被叫号码查询工单是否存在")
    public ComResponse<Boolean> findByCalledPhoneIsEmpty(@Validated @RequestBody FindByCalledPhoneIsEmptyDTO findByCalledPhoneIsEmptyDTO) {
        return workOrderClient.findByCalledPhoneIsEmpty(findByCalledPhoneIsEmptyDTO);
    }

    /**
     * 智能工单：热线工单管理-可分配员工
     *回收
     * @param
     * @return
     */
    @PostMapping("v1/getDistributionStaff")
    @ApiOperation(value = "热线工单管理-可分配员工", notes = "热线工单管理-可分配员工")
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
    @ApiOperation(value = "热线&&回访-创建处理工单流水", notes = "热线&&回访-创建处理工单流水")
    public ComResponse<String> insertWorkOrderDisposeFlow(@Validated @RequestBody InsertWorkOrderDisposeFlowDTO insertWorkOrderDisposeFlowDTO) {
        //获取当前登陆人信息
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户不存在");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        String staffNo = data.getStaffNo();
        String name = data.getName();
        insertWorkOrderDisposeFlowDTO.setCreateId(staffNo);
        insertWorkOrderDisposeFlowDTO.setCreateName(name);
        insertWorkOrderDisposeFlowDTO.setUpdateId(staffNo);
        insertWorkOrderDisposeFlowDTO.setUpdateName(name);
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
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        recoveryDTO.setStaffName(detailsByNo.getData().getName());
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
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        recoveryDTO.setCreateName(detailsByNo.getData().getName());
        recoveryDTO.setCreateId(QueryIds.userNo.get());
        recoveryDTO.setCreateName(detailsByNo.getData().getName());
        return workOrderClient.handIn(recoveryDTO);
    }

    /**
     * 智能工单-我的回访工单-接收
     *
     * @param receiveDTO
     * @return
     */
    @ApiOperation(value = "我的回访工单-接收", notes = "我的回访工单-接收")
    @PostMapping(value = "v1/receive")
    public ComResponse<Void> receive(@RequestBody ReceiveDTO receiveDTO) {
        return workOrderClient.receive(receiveDTO);
    }

    @ApiOperation(value = "我的回访工单-单条上交",notes = "我的回访工单-单条上交")
    @PostMapping(value = "v1/isHandIn")
    public ComResponse<Boolean> isHandIn(@RequestBody IsHandInDTO isHandInDTO){
        isHandInDTO.setStaffNo(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata("用户信息不存在!");
        }
        isHandInDTO.setStaffName(detailsByNo.getData().getName());
        ComResponse<List<WorkOrderRuleConfigBean>> listComResponse = turnRulnClient.submissionRules(1, 2, 1, 0);
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
        Boolean flag = Boolean.FALSE;
        if(!CollectionUtils.isEmpty(listComResponse.getData())) {
            for (WorkOrderRuleConfigBean workOrderRuleConfigBean : data) {
                wORCBean = workOrderRuleConfigBean;
                switch (workOrderRuleConfigBean.getId()) {
                    case 1:
                        flag = handInUtils.emptyNumberShutdown(isHandInDTO, wORCBean);
                        break;

                    case 2:
                        flag = handInUtils.unableToContact(isHandInDTO, wORCBean);
                        break;

                    case 3:
                        flag = handInUtils.customerRefusedToVisit(isHandInDTO, wORCBean);
                        break;

                    case 4:
                        flag = handInUtils.customerRefund(isHandInDTO, wORCBean);
                        break;

                    case 5:
                        flag = handInUtils.dormantCustomers(isHandInDTO, wORCBean);
                        break;

                /*case 6:
                    flag = handInUtils.mCustomerLExceeded(isHandInDTO,wORCBean);
                    break;*/

                    case 8:
                        flag = handInUtils.overtimeReturnVisit(isHandInDTO, wORCBean);
                        break;

                    default: {
                    }
                }
                if (BooleanUtils.isTrue(flag)) {
                    break;
                }
            }
        }else{
            flag = Boolean.TRUE;
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
            recoveryDTO.setStatus(CommonConstants.ONE);
            workOrderClient.handIn(recoveryDTO);
        }else {
            isHandInDTO.setApplyUpStatus(1);
            turnRulnClient.rulesHandedIn(isHandInDTO);
        }
        return ComResponse.success(flag);
    }

    @ApiOperation(value = "热线&&回访-处理工单提交",notes = "热线&&回访-处理工单提交")
    @PostMapping(value = "v1/submitWorkOrder")
    public ComResponse<Void> submitWorkOrder(@Validated @RequestBody SubmitWorkOrderDTO submitWorkOrderDTO){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"用户信息不存在");
        }
        submitWorkOrderDTO.setUpdateId(userNo);
        submitWorkOrderDTO.setUpdateName(detailsByNo.getData().getName());
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
