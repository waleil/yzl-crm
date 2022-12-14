package cn.net.yzl.crm.controller.workorder;

import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.common.util.YLoggerUtil;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.client.workorder.TurnRulnClient;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.config.QueryIds;
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
import cn.net.yzl.workorder.model.dto.InformationGoodsDTOS;
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
import cn.net.yzl.workorder.model.dto.WorkOrderVisitTypeDTO;
import cn.net.yzl.workorder.model.enums.MemberLevelEnums;
import cn.net.yzl.workorder.model.vo.FindDWorkOrderHotlineDetailsVO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.workorder.model.vo.MyWorkOrderHotlineListVO;
import cn.net.yzl.workorder.model.vo.WorkOrderUnclaimedUserVO;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.*;
import java.util.stream.Collectors;

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
@Api(tags = "????????????")
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
     * ??????????????????????????????
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isVisitListPage")
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
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
            //????????????????????????
            Date lastOrderTime = workOrderBean.getLastOrderTime();
            if(null != lastOrderTime){
                String yyyy = DateFormatUtil.dateToString(lastOrderTime, DateFormatUtil.YEAR_FORMAT);
                if(Constant.YRAR_YYYY.equals(yyyy)){
                    workOrderBean.setLastOrderTime(null);
                }
            }
            Date lastCallTime = workOrderBean.getLastCallTime();
            if(null != lastCallTime){
                String yyyy = DateFormatUtil.dateToString(lastCallTime, DateFormatUtil.YEAR_FORMAT);
                if(Constant.YRAR_YYYY.equals(yyyy)){
                    workOrderBean.setLastCallTime(null);
                }
            }
            //??????????????????
            this.getMgradeCodeName(workOrderBean);
        }
        if(!StringUtils.isEmpty(productNames)){
            productNames = productNames.substring(1);
            productNames = Arrays.asList(productNames.split(",")).stream()
                    .distinct()
                    .collect(Collectors.joining(","));
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
        }
        return ComResponse.success(pageWorkOrderBean);
    }

    @PostMapping("v1/workOrderHotlineList")
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> workOrderHotlineList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO) {
        //?????????????????????
        Integer isMonitoring = findWorkOrderHotlinePageListDTO.getIsMonitoring();
        if(0 == isMonitoring){
            //???????????????????????????????????????
            ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
            //????????????--???????????????????????????
            if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
                return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
            }
            StaffImageBaseInfoDto data = detailsByNo.getData();
            Integer departId = data.getDepartId();//???????????????????????????
            findWorkOrderHotlinePageListDTO.setDeptId(departId);
        }
        //????????????--?????????????????????????????????
        ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageComResponse = workOrderClient.workOrderHotlineList(findWorkOrderHotlinePageListDTO);
        return pageComResponse;

    }

    /**
     * ?????????????????????????????????-??????
     *
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated @RequestBody UpdateRecyclingDTO updateRecyclingDTO) {
        //???????????????????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        //????????????--???????????????????????????
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateRecyclingDTO.setStaffNo(data.getStaffNo());
        updateRecyclingDTO.setOperator(data.getName());
        updateRecyclingDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateRecycling(updateRecyclingDTO);
    }

    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
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
        if(org.apache.commons.lang3.StringUtils.isBlank(productNames)){
            return ComResponse.success(pageWorkOrderUnclaimedUserVO);
        }
        productNames = productNames.substring(1);
        productNames = Arrays.asList(productNames.split(",")).stream()
                .distinct()
                .collect(Collectors.joining(","));
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


    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    @PostMapping("v1/receiveUsers")
    public ComResponse<Boolean> receiveUsers(@RequestBody List<WorkOrderFlowDTO> list) {
        return workOrderService.receiveUsers(list);
    }


    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
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
        if(!StringUtils.isEmpty(productNames)){
            productNames = productNames.substring(1);
            productNames = Arrays.asList(productNames.split(",")).stream()
                    .distinct()
                    .collect(Collectors.joining(","));
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
        }
        return ComResponse.success(pageWorkOrderBean);
    }

    /**
     * ?????????????????????????????????-???????????????
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    @ApiOperation(value = "??????????????????-???????????????", notes = "??????????????????-???????????????")
    public ComResponse<Boolean> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO) {
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateSingleAdjustDTO.setOperatorCode(data.getStaffNo());//???????????????
        updateSingleAdjustDTO.setOperator(data.getName());//???????????????
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNoOne = ehrStaffClient.getDetailsByNo(updateSingleAdjustDTO.getStaffNo());
        if(!StringUtils.isEmpty(detailsByNoOne) && !StringUtils.isEmpty(detailsByNoOne.getData())){
            StaffImageBaseInfoDto dataOne = detailsByNoOne.getData();
            updateSingleAdjustDTO.setStaffNo(dataOne.getStaffNo());//??????????????????
            updateSingleAdjustDTO.setStaffName(dataOne.getName());//??????????????????
            updateSingleAdjustDTO.setStaffLevel(StringUtils.isEmpty(dataOne.getPostLevelName())?"":String.valueOf(dataOne.getPostLevelName()));//??????????????????
            updateSingleAdjustDTO.setDeptId(dataOne.getDepartId());//??????????????????id
            updateSingleAdjustDTO.setDeptName(dataOne.getDepartName());//????????????????????????
        }

        updateSingleAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateSingleAdjustDTO.setAcceptStatus(1);//???????????? ?????????????????????????????????????????????????????????????????????????????????
        ComResponse<Boolean> comResponse = workOrderClient.updateSingleAdjust(updateSingleAdjustDTO);
        if(null != comResponse.getData() && comResponse.getData()){
            comResponse.setMessage("??????");
        }
        return comResponse;
    }

    @ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????????????????")
    @GetMapping("v1/queryFirstProduct")
    public ComResponse<List<ProductMainDTO>> queryFirstProduct(@ApiParam(value = "1:??????;2:?????????")@RequestParam("status") Integer status) {

        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        StaffImageBaseInfoDto data1 = detailsByNo.getData();
        Integer deptId = null;
        if(null != data1){
            deptId = data1.getDepartId();
        }
        String data = workOrderClient.queryFirstProduct(deptId,status).getData();
        if(!StringUtils.isEmpty(data)){
            return productClient.queryByProductCodes(data);
        } else {
            return ComResponse.success();
        }
    }

    @ApiOperation(value = "????????????????????????????????????-??????", notes = "????????????????????????????????????-??????")
    @GetMapping("v1/queryLastProduct")
    public ComResponse<List<ProductMainDTO>> queryLastProduct(@ApiParam(value = "1:??????;2:?????????")@RequestParam("status") Integer status) {
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        StaffImageBaseInfoDto data1 = detailsByNo.getData();
        Integer deptId = null;
        if(null != data1){
            deptId = data1.getDepartId();
        }
        String data = workOrderClient.queryLastProduct(deptId,status).getData();
        if(!StringUtils.isEmpty(data)){
            return productClient.queryByProductCodes(data);
        }
        return ComResponse.success();
    }

    /**
     * ?????????????????????????????????-???????????????
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "??????????????????-???????????????", notes = "??????????????????-???????????????")
    public ComResponse<Boolean> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO) {
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateMoreAdjustDTO.setOperatorCode(data.getStaffNo());
        updateMoreAdjustDTO.setOperator(data.getName());
        //???????????????????????????
        List<UpdateMoreAdjustSubTDTO> names = updateMoreAdjustDTO.getNames();
        if(!CollectionUtils.isEmpty(names)){
            for (int i = 0; i < names.size(); i++) {
                ComResponse<StaffImageBaseInfoDto> detailsByNoOne = ehrStaffClient.getDetailsByNo(names.get(i).getStaffNo());
                if(!StringUtils.isEmpty(detailsByNoOne) && !StringUtils.isEmpty(detailsByNoOne.getData())){
                    StaffImageBaseInfoDto dataOne = detailsByNoOne.getData();
                    Integer departId = dataOne.getDepartId();
                    if(StringUtils.isEmpty(departId)){
                        return ComResponse.fail(ComResponse.ERROR_STATUS,"?????????????????????");
                    }
                    names.get(i).setStaffNo(dataOne.getStaffNo());//??????????????????
                    names.get(i).setStaffName(dataOne.getName());//??????????????????
                    names.get(i).setStaffLevel(StringUtils.isEmpty(dataOne.getPostLevelName())?"":String.valueOf(dataOne.getPostLevelName()));//??????????????????
                    names.get(i).setDeptId(departId);//??????????????????id
                    names.get(i).setDeptName(dataOne.getDepartName());//????????????????????????
                    names.get(i).setAcceptStatus(1);//????????????????????????????????????????????????
                } else {
                    return ComResponse.fail(ComResponse.ERROR_STATUS,"?????????????????????????????????");
                }
            }
        }
        updateMoreAdjustDTO.setAcceptStatus(1);//???????????? ??????????????????????????????????????????????????????????????????
        updateMoreAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateMoreAdjust(updateMoreAdjustDTO);
    }

    @ApiOperation(value = "??????????????????-????????????", notes = "??????????????????-????????????")
    @PostMapping(value = "v1/adjustment")
    public ComResponse<Boolean> adjustment(@RequestBody UpdateWorkOrderVisitDTO updateWorkOrderVisitDTO) {
        updateWorkOrderVisitDTO.setCreateId(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        updateWorkOrderVisitDTO.setCreateName(detailsByNo.getData().getName());
        return workOrderClient.adjustment(updateWorkOrderVisitDTO);
    }

    @ApiOperation(value = "??????????????????-????????????", notes = "??????????????????-????????????")
    @PostMapping(value = "v1/batchAdjustment")
    public ComResponse<Boolean> batchAdjustment(@RequestBody UpdateBatchDTO updateBatchDTO) {
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
            return ComResponse.nodata("????????????????????????????????????!");
        }
        return workOrderClient.batchAdjustment(updateBatchDTO);
    }

    @PostMapping("v1/findMyWorkOrderHotlinePageList")
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    public ComResponse<Page<MyWorkOrderHotlineListVO>> findMyWorkOrderHotlinePageList(@RequestBody MyWorkOrderHotlineListDTO myWorkOrderHotlineListDTO) {
        String userId = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userId)) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
        }
        myWorkOrderHotlineListDTO.setStaffNo(userId);
        ComResponse<Page<MyWorkOrderHotlineListVO>> myWorkOrderHotlinePageList = workOrderClient.findMyWorkOrderHotlinePageList(myWorkOrderHotlineListDTO);
//        List<MyWorkOrderHotlineListVO> items = myWorkOrderHotlinePageList.getData().getItems();
//        if(!StringUtils.isEmpty(items))
//            items.stream().forEach(s -> {
//                s.setAllocateTime(StringUtils.isEmpty(s.getAllocateTime())?null:MonggoDateHelper.getMongoDate(s.getAllocateTime()));
//            });
        return myWorkOrderHotlinePageList;
    }

    /**
     * ?????????????????????????????????-??????
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateAcceptStatusReceive")
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    public ComResponse<Void> updateAcceptStatusReceive(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        updateAcceptStatusReceiveDTO.setOperatorCode(data.getStaffNo());
        updateAcceptStatusReceiveDTO.setOperator(data.getName());
        updateAcceptStatusReceiveDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateAcceptStatusReceive(updateAcceptStatusReceiveDTO);
    }

    /**
     * ?????????????????????????????????-??????????????????
     *
     * @param
     * @return
     */
    @PostMapping("v1/findDWorkOrderHotlineDetails")
    @ApiOperation(value = "??????&&??????-??????????????????", notes = "??????&&??????-??????????????????")
    public ComResponse<FindDWorkOrderHotlineDetailsVO> findDWorkOrderHotlineDetails(@Validated @RequestBody UpdateAcceptStatusReceiveDTO updateAcceptStatusReceiveDTO) {
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto staffImageBaseInfoDto = detailsByNo.getData();
        updateAcceptStatusReceiveDTO.setOperator(staffImageBaseInfoDto.getName());
        updateAcceptStatusReceiveDTO.setOperatorCode(staffImageBaseInfoDto.getStaffNo());
        updateAcceptStatusReceiveDTO.setOperatorType(CommonConstants.TWO);
        ComResponse<FindDWorkOrderHotlineDetailsVO> dWorkOrderHotlineDetails = workOrderClient.findDWorkOrderHotlineDetails(updateAcceptStatusReceiveDTO);
        FindDWorkOrderHotlineDetailsVO data = dWorkOrderHotlineDetails.getData();
        if(!StringUtils.isEmpty(data)){
            //?????????????????????????????????????????????????????????????????????
            WorkOrderVisitTypeDTO workOrderVisitTypeDTO = JSONUtil.toBean(data.getInformationGoods(),WorkOrderVisitTypeDTO.class);
            List<InformationGoodsDTOS> ladderSales = workOrderVisitTypeDTO.getLadderSales();
            if(!StringUtils.isEmpty(ladderSales) && ladderSales.size() > 0) {
                //??????Map??????
                List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                ladderSales.forEach(informationGoodsDTOS -> {
                    //??????????????????????????????
                    ComResponse<ProductDetailVO> productDetailVOComResponse = productClient.queryProductDetail(informationGoodsDTOS.getCode());
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
     * ?????????????????????????????????-????????????????????????????????????
     *
     * @return
     */
    @PostMapping("v1/findByCalledPhoneIsEmpty")
    @ApiOperation(value = "??????&&??????-????????????????????????????????????", notes = "??????&&??????-????????????????????????????????????")
    public ComResponse<Boolean> findByCalledPhoneIsEmpty(@Validated @RequestBody FindByCalledPhoneIsEmptyDTO findByCalledPhoneIsEmptyDTO) {
        return workOrderClient.findByCalledPhoneIsEmpty(findByCalledPhoneIsEmptyDTO);
    }

    /**
     * ?????????????????????????????????-???????????????
     *??????
     * @param
     * @return
     */
    @PostMapping("v1/getDistributionStaff")
    @ApiOperation(value = "??????????????????-???????????????", notes = "??????????????????-???????????????")
    public ComResponse<Page<EhrStaff>> getDistributionStaff(@RequestBody GetDistributionStaffDTO getDistributionStaffDTO) {
        String userId = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userId)) {
            ComResponse.fail(ComResponse.ERROR_STATUS, "??????????????????");
        }
        getDistributionStaffDTO.setStaffNo(userId);
        return workOrderService.getDistributionStaff(getDistributionStaffDTO);
    }


    /**
     * ?????????????????????????????????-????????????????????????
     *
     * @return
     */
    @PostMapping("v1/insertWorkOrderDisposeFlow")
    @ApiOperation(value = "??????&&??????-????????????????????????", notes = "??????&&??????-????????????????????????")
    public ComResponse<String> insertWorkOrderDisposeFlow(@Validated @RequestBody InsertWorkOrderDisposeFlowDTO insertWorkOrderDisposeFlowDTO) {
        YLoggerUtil.infoLog("?????????????????????????????????-????????????????????????Request???????????????:", JsonUtil.toJsonStr(insertWorkOrderDisposeFlowDTO));
        //???????????????????????????
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"???????????????");
        }
        StaffImageBaseInfoDto data = detailsByNo.getData();
        String staffNo = data.getStaffNo();
        String name = data.getName();
        insertWorkOrderDisposeFlowDTO.setCreateId(staffNo);
        insertWorkOrderDisposeFlowDTO.setCreateName(name);
        insertWorkOrderDisposeFlowDTO.setUpdateId(staffNo);
        insertWorkOrderDisposeFlowDTO.setUpdateName(name);
        ComResponse<String> stringComResponse = workOrderClient.insertWorkOrderDisposeFlow(insertWorkOrderDisposeFlowDTO);
        YLoggerUtil.infoLog("?????????????????????????????????-????????????????????????Response???????????????:", JsonUtil.toJsonStr(stringComResponse));
        return stringComResponse;
    }

    /**
     * ????????????:??????????????????-??????
     *
     * @param recoveryDTO
     * @return
     */
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
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
     * ????????????:??????????????????-??????
     *
     * @param recoveryDTO
     * @return
     */
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    @PostMapping(value = "v1/handIn")
    public ComResponse<Void> handIn(@RequestBody RecoveryDTO recoveryDTO) {
        recoveryDTO.setStaffNo(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata();
        }
        recoveryDTO.setStaffName(detailsByNo.getData().getName());
        recoveryDTO.setCreateId(QueryIds.userNo.get());
        //recoveryDTO.setDeptId(detailsByNo.getData().getDepartId());
        return workOrderClient.handIn(recoveryDTO);
    }

    /**
     * ????????????-??????????????????-??????
     *
     * @param receiveDTO
     * @return
     */
    @ApiOperation(value = "??????????????????-??????", notes = "??????????????????-??????")
    @PostMapping(value = "v1/receive")
    public ComResponse<Void> receive(@RequestBody ReceiveDTO receiveDTO) {
        return workOrderClient.receive(receiveDTO);
    }

    @ApiOperation(value = "??????????????????-????????????",notes = "??????????????????-????????????")
    @PostMapping(value = "v1/isHandIn")
    public ComResponse<Boolean> isHandIn(@RequestBody IsHandInDTO isHandInDTO){
        isHandInDTO.setStaffNo(QueryIds.userNo.get());
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(null == detailsByNo.getData()){
            return ComResponse.nodata("?????????????????????!");
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
            recoveryDTO.setApplyUpMemo(isHandInDTO.getApplyUpMemo());
            recoveryDTO.setStatus(2);
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

                    /*case 8:
                        flag = handInUtils.overtimeReturnVisit(isHandInDTO, wORCBean);
                        break;
*/
                    default: {
                    }
                }
                if (BooleanUtils.isTrue(flag)) {
                    break;
                }
            }
        }else{
            flag = Boolean.FALSE;
        }
        if(flag){
            /*RecoveryDTO recoveryDTO = new RecoveryDTO();
            recoveryDTO.setStaffName(isHandInDTO.getStaffName());
            recoveryDTO.setStaffNo(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateId(isHandInDTO.getStaffNo());
            recoveryDTO.setCreateName(isHandInDTO.getStaffName());
            recoveryDTO.setCode(isHandInDTO.getCode());
            recoveryDTO.setMemberCard(isHandInDTO.getMemberCard());
            recoveryDTO.setMemberName(isHandInDTO.getMemberName());
            recoveryDTO.setApplyUpMemo(isHandInDTO.getApplyUpMemo());
            recoveryDTO.setDeptId(detailsByNo.getData().getDepartId());
            recoveryDTO.setDeptName(detailsByNo.getData().getDepartName());
            recoveryDTO.setStatus(CommonConstants.ONE);
            workOrderClient.handIn(recoveryDTO);*/
            RecoveryDTO recoveryDTO = new RecoveryDTO();
            StaffImageBaseInfoDto staffImageBaseInfoDto = detailsByNo.getData();
            recoveryDTO.setCode(isHandInDTO.getCode());
            recoveryDTO.setMemberCard(isHandInDTO.getMemberCard());
            recoveryDTO.setMemberName(isHandInDTO.getMemberName());
            recoveryDTO.setStaffName(staffImageBaseInfoDto.getName());
            recoveryDTO.setCreateId(staffImageBaseInfoDto.getStaffNo());
            recoveryDTO.setDeptId(staffImageBaseInfoDto.getDepartId());
            workOrderClient.handIn(recoveryDTO);
        }else {

           isHandInDTO.setApplyUpStatus(1);
           turnRulnClient.rulesHandedIn(isHandInDTO);
        }
        return ComResponse.success(flag);
    }

    @ApiOperation(value = "??????&&??????-??????????????????",notes = "??????&&??????-??????????????????")
    @PostMapping(value = "v1/submitWorkOrder")
    public ComResponse<Void> submitWorkOrder(@Validated @RequestBody SubmitWorkOrderDTO submitWorkOrderDTO){
        String userNo = QueryIds.userNo.get();
        ComResponse<StaffImageBaseInfoDto> detailsByNo = ehrStaffClient.getDetailsByNo(QueryIds.userNo.get());
        if(StringUtils.isEmpty(detailsByNo) || StringUtils.isEmpty(detailsByNo.getData())){
            return ComResponse.fail(ComResponse.ERROR_STATUS,"?????????????????????");
        }
        submitWorkOrderDTO.setUpdateId(userNo);
        submitWorkOrderDTO.setUpdateName(detailsByNo.getData().getName());
        return workOrderClient.submitWorkOrder(submitWorkOrderDTO);
    }

    @ApiOperation(value = "?????????????????????????????????:?????????",notes = "?????????????????????????????????:?????????")
    @PostMapping(value = "v1/productDosage")
    public ComResponse<Boolean> productDosage(@ApiParam("???????????????")@RequestParam("memberCard") List<String> memberCard){
        return workOrderClient.productDosage(memberCard);
    }

    @ApiOperation(value = "???????????????",notes = "???????????????")
    @GetMapping(value = "v1/newMember")
    public ComResponse<Boolean> newMember(@ApiParam("???????????????")@RequestParam("memberCard") String memberCard,@ApiParam("??????:1?????????;2:?????????")@RequestParam("status")Integer status ){
        return workOrderClient.newMember(memberCard,status);
    }


    /**
     * ??????????????????????????????????????????
     * @param workOrderBean
     */
    private void getMgradeCodeName(WorkOrderBean workOrderBean) {
        String mGradeCode = workOrderBean.getMGradeCode();
        if(MemberLevelEnums.MEMBER_LEVEL_NO_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_NO_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_CLASSIC_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_CLASSIC_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_COPPER_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_COPPER_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_SILVER_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_SILVER_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_GOLD_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_GOLD_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_DIAMOND_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_DIAMOND_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_VIP_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_VIP_CARD.getName());
        } else if(MemberLevelEnums.MEMBER_LEVEL_SUPER_VIP_CARD.getCode().equals(mGradeCode)){
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_SUPER_VIP_CARD.getName());
        } else {
            workOrderBean.setMGradeCode(MemberLevelEnums.MEMBER_LEVEL_NO_CARD.getName());
        }
    }
}
