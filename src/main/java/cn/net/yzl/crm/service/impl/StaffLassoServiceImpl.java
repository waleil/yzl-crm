package cn.net.yzl.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.dto.ehr.StaffDetailDto;
import cn.net.yzl.crm.dto.staff.CalculationResult;
import cn.net.yzl.crm.dto.staff.CalculationUerDetail;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.service.StaffLassoService;
import cn.net.yzl.crm.service.micservice.BiTaskClient;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.OrderClient;
import cn.net.yzl.crm.staff.dto.lasso.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.hutool.core.collection.CollUtil.isEmpty;

/**
 * @author: liuChangFu
 * @date: 2021/1/21 19:21
 * @desc: //TODO  请说明该类的用途
 **/
@Service
public class StaffLassoServiceImpl implements StaffLassoService {

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private BiTaskClient biTaskClient;


    @Autowired
    private CrmStaffClient crmStaffClient;

    @Autowired
    private OrderClient orderClient;


    @Override
    public List<String> calculationDto(CalculationDto calculationDto, Long id) throws Exception {
        if (null == calculationDto) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(calculationDto.getPostIdList())) {
            return Collections.emptyList();
        }
        //获取原线程的请求参数
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        //岗位相关条件查询
        CompletableFuture<List<String>> postIdCompletable = CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(attributes);
            return ehrStaffClient.getPostIdList(calculationDto.getPostIdList());
        });

        //基础信息相关条件查询
        CompletableFuture<List<String>> baseCompletable = CompletableFuture.supplyAsync(() -> {
            Base base = calculationDto.getBase();
            if (null != base) {
                RequestContextHolder.setRequestAttributes(attributes);
                return ehrStaffClient.getStaffBaseInfoList(base);
            }
            return null;
        });
        //工单类型条件查询
        CompletableFuture<List<String>> workOrderTypeCompletable = CompletableFuture.supplyAsync(() -> {
            List<WorkOrderTypeDto> workOrderType = calculationDto.getWorkOrderType();
            if (CollectionUtils.isNotEmpty(workOrderType)) {
                RequestContextHolder.setRequestAttributes(attributes);
                return ehrStaffClient.getStaffWorkOrderTypeList(workOrderType);
            }
            return null;
        });
        //广告相关条件查询
        CompletableFuture<List<String>> advertCompletable = CompletableFuture.supplyAsync(() -> {
            List<AdvertDto> advertList = calculationDto.getAdvertList();
            if (CollectionUtils.isNotEmpty(advertList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                OrderCalculationDto orderCalculationDto = new OrderCalculationDto();
                orderCalculationDto.setAdverts(advertList);
                return orderClient.getStaffOrderList(orderCalculationDto);
            }
            return null;
        });
        //媒体相关条件查询
        CompletableFuture<List<String>> mediaCompletable = CompletableFuture.supplyAsync(() -> {
            List<MediaDto> mediaList = calculationDto.getMediaList();
            if (CollectionUtils.isNotEmpty(mediaList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                OrderCalculationDto orderCalculationDto = new OrderCalculationDto();
                orderCalculationDto.setMedias(mediaList);
                return orderClient.getStaffOrderList(orderCalculationDto);
            }
            return null;
        });
        //售卖商品相关条件查询
        CompletableFuture<List<String>> saleProductCompletable = CompletableFuture.supplyAsync(() -> {
            List<SaleProductDto> saleProductList = calculationDto.getSaleProductList();
            if (CollectionUtils.isNotEmpty(saleProductList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                OrderCalculationDto orderCalculationDto = new OrderCalculationDto();
                orderCalculationDto.setProducts(saleProductList);
                return orderClient.getStaffOrderList(orderCalculationDto);
            }
            return null;
        });
        //病症相关条件查询
        CompletableFuture<List<String>> diseaseCompletable = CompletableFuture.supplyAsync(() -> {
            List<DiseaseDto> diseaseDtoList = calculationDto.getDiseaseList();
            if (CollectionUtils.isNotEmpty(diseaseDtoList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                OrderCalculationDto orderCalculationDto = new OrderCalculationDto();
                orderCalculationDto.setDiseases(diseaseDtoList);
                return orderClient.getStaffOrderList(orderCalculationDto);
            }
            return null;
        });
        //排班相关条件查询
        CompletableFuture<List<String>> scheduleCompletable = CompletableFuture.supplyAsync(() -> {
            ScheduleDto schedule = calculationDto.getSchedule();
            if (null != schedule) {
                RequestContextHolder.setRequestAttributes(attributes);
                return ehrStaffClient.getStaffSchedule(schedule);
            }
            return null;
        });
        //培训商品相关条件查询
        CompletableFuture<List<String>> trainProductCompletable = CompletableFuture.supplyAsync(() -> {
            TrainProductDto trainProduct = calculationDto.getTrainProduct();
            if (null != trainProduct && CollectionUtils.isNotEmpty(trainProduct.getTrainProductList())) {
                RequestContextHolder.setRequestAttributes(attributes);
                return ehrStaffClient.getStaffTrainProductList(trainProduct);
            }
            return null;
        });
        //员工指标相关条件查询
        CompletableFuture<List<String>> indicatorCompletable = CompletableFuture.supplyAsync(() -> {
            IndicatorDto indicator = calculationDto.getIndicator();
            if (null != indicator && CollectionUtils.isNotEmpty(indicator.getIndicatorList())) {
                RequestContextHolder.setRequestAttributes(attributes);
                return biTaskClient.getStaffIndicatorList(indicator);
            }
            return null;
        });

        CompletableFuture<List<String>> all = CompletableFuture.allOf(postIdCompletable, baseCompletable, workOrderTypeCompletable
                , advertCompletable, mediaCompletable, saleProductCompletable, diseaseCompletable, scheduleCompletable
                , trainProductCompletable, indicatorCompletable).thenApply(x -> {
            try {
                return (List<String>) intersection(postIdCompletable.get(), baseCompletable.get(), workOrderTypeCompletable.get(), advertCompletable.get()
                        , mediaCompletable.get(), saleProductCompletable.get(), diseaseCompletable.get(), scheduleCompletable.get()
                        , trainProductCompletable.get(), indicatorCompletable.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        });
        List<StaffCrowdGroup> staffCrowdGroup = crmStaffClient.getStaffCrowdGroupDefault(id, calculationDto.getRulePriorityLevel());

        List<String> staffNoList = all.get();
        if (CollectionUtils.isEmpty(staffNoList)) {
            return Collections.emptyList();
        }

        List<List<String>> collect = staffCrowdGroup.stream().map(StaffCrowdGroup::getStaffCodeList).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return CollectionUtils.isNotEmpty(all.get()) ? all.get() : Collections.emptyList();
        }
        for (List<String> staffNo : collect) {
            staffNoList = CollUtil.subtractToList(staffNoList, staffNo);
        }
        return staffNoList;
    }

    @SafeVarargs
    public static <T> Collection<T> intersection(Collection<T>... otherColls) {
        List<Collection<T>> newColl = new ArrayList<>();
        for (Collection<T> coll : otherColls) {
            if (null != coll) {
                newColl.add(coll);
            }
        }
        if (isEmpty(newColl)) {
            return Collections.emptyList();
        }
        if (newColl.size() == 1) {
            return newColl.get(0);
        }
        if (newColl.size() == 2) {
            return CollUtil.intersection(newColl.get(0), newColl.get(1));
        }
        List<Collection<T>> collect = newColl.stream().skip(2).collect(Collectors.toList());
        Collection<T> intersection = CollUtil.intersection(newColl.get(0), newColl.get(1));
        for (Collection<T> coll : collect) {
            intersection = CollUtil.intersection(intersection, coll);
            if (isEmpty(intersection)) {
                return intersection;
            }
        }
        return intersection;
    }

    @Override
    public ComResponse<CalculationResult> trialStaffNo(long groupId) throws Exception {
        ComResponse<StaffCrowdGroup> staffCrowdGroup = crmStaffClient.getStaffCrowdGroup(groupId);
        if (null == staffCrowdGroup) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        StaffCrowdGroup data = staffCrowdGroup.getData();
        if (null == data) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        List<String> userNoList = this.calculationDto(data.getCalculationDto(), groupId);
        if (org.springframework.util.CollectionUtils.isEmpty(userNoList)) {
            return ComResponse.success();
        }
        return ComResponse.success(calculationUerDetail(userNoList));
    }

    @Override
    public ComResponse<Page<StaffCrowdGroupListDTO>> getGroupListByPage(String crowdGroupName, Integer status, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
        ComResponse<Page<StaffCrowdGroupListDTO>> groupListByPage = crmStaffClient.getGroupListByPage(crowdGroupName, status, startTime, endTime, pageNo, pageSize);
        if (null == groupListByPage) {
            return ComResponse.success();
        }
        Page<StaffCrowdGroupListDTO> data = groupListByPage.getData();
        if (null == data) {
            return ComResponse.success();
        }
        List<StaffCrowdGroupListDTO> content = data.getItems();
        if (CollectionUtils.isEmpty(content)) {
            return ComResponse.success();
        }
        List<String> staffNos = content.stream().map(StaffCrowdGroupListDTO::getCreateCode).collect(Collectors.toList());
        Map<String, StaffDetailDto> mapByStaffNos = ehrStaffClient.getMapByStaffNos(staffNos);

        content.forEach(c -> {
            if (null != mapByStaffNos) {
                c.setCreateName(null != mapByStaffNos.get(c.getCreateCode()) ? mapByStaffNos.get(c.getCreateCode()).getName() : "");
            }
        });
        return groupListByPage;
    }

    @Override
    public void taskCalculation() {
        List<StaffCrowdGroup> staffCrowdGroup = crmStaffClient.getStaffCrowdGroupDefault(0L, 0);
        staffCrowdGroup.forEach(staffGroup -> {
            try {
                List<String> staffs = this.calculationDto(staffGroup.getCalculationDto(), staffGroup.getId());
                staffGroup.setStaffCodeList(staffs);
                staffGroup.setPersonCount(staffs.size());
                crmStaffClient.updateResult(staffGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public CalculationResult calculationUerDetail(List<String> userNoList) {
        CalculationResult calculationResult = new CalculationResult();
        int min = Math.min(userNoList.size(), 50);
        calculationResult.setExampleSize(min);
        calculationResult.setUserNoSize(userNoList.size());
        List<String> userNos = userNoList.subList(0, min);
        List<StaffDetail> staffDetailList = ehrStaffClient.getDetailsListByNoDefault(userNos);
        if (CollectionUtils.isEmpty(staffDetailList)) {
            return null;
        }
        String sourcesJson = JSONUtil.toJsonStr(staffDetailList);
        List<CalculationUerDetail> calculationUerDetails = JsonUtil.jsonToList(sourcesJson, CalculationUerDetail.class);
        calculationResult.setCalculationUerDetail(calculationUerDetails);
        return calculationResult;
    }
}
