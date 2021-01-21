package cn.net.yzl.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.StaffLassoService;
import cn.net.yzl.crm.service.micservice.BiTaskClient;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.staff.dto.lasso.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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


    @Override
    public Integer calculationDto(CalculationDto calculationDto) throws Exception {
        if (null == calculationDto) {
            return 0;
        }
        //基础信息相关条件查询
        CompletableFuture<List<String>> baseCompletable = CompletableFuture.supplyAsync(() -> {
            Base base = calculationDto.getBase();
            if (null != base && (null != base.getSex() || null != base.getNature() || CollectionUtils.isNotEmpty(base.getWorkplaceList()))) {
                return ehrStaffClient.getStaffScheduleInfoList(base);
            }
            return Collections.emptyList();
        });
        //工单类型条件查询
        CompletableFuture<List<String>> workOrderTypeCompletable = CompletableFuture.supplyAsync(() -> {
            List<WorkOrderTypeDto> workOrderType = calculationDto.getWorkOrderTypeList();
            if (CollectionUtils.isNotEmpty(workOrderType)) {
                return ehrStaffClient.getStaffWorkOrderTypeList(workOrderType);
            }
            return Collections.emptyList();
        });
        //广告相关条件查询
        CompletableFuture<List<String>> advertCompletable = CompletableFuture.supplyAsync(() -> {
            List<AdvertDto> advertList = calculationDto.getAdvertList();
            if (CollectionUtils.isNotEmpty(advertList)) {
                return null;
            }
            return Collections.emptyList();
        });
        //媒体相关条件查询
        CompletableFuture<List<String>> mediaCompletable = CompletableFuture.supplyAsync(() -> {
            List<MediaDto> mediaList = calculationDto.getMediaList();
            if (CollectionUtils.isNotEmpty(mediaList)) {
                return null;
            }
            return Collections.emptyList();
        });
        //售卖商品相关条件查询
        CompletableFuture<List<String>> saleProductCompletable = CompletableFuture.supplyAsync(() -> {
            List<SaleProductDto> saleProductList = calculationDto.getSaleProductList();
            if (CollectionUtils.isNotEmpty(saleProductList)) {
                return null;
            }
            return Collections.emptyList();
        });
        //病症相关条件查询
        CompletableFuture<List<String>> diseaseCompletable = CompletableFuture.supplyAsync(() -> {
            List<DiseaseDto> diseaseDtoList = calculationDto.getDiseaseList();
            if (CollectionUtils.isNotEmpty(diseaseDtoList)) {
                return null;
            }
            return Collections.emptyList();
        });
        //排班相关条件查询
        CompletableFuture<List<String>> scheduleCompletable = CompletableFuture.supplyAsync(() -> {
            ScheduleDto schedule = calculationDto.getSchedule();
            if (null != schedule) {
                return ehrStaffClient.getStaffSchedule(schedule);
            }
            return Collections.emptyList();
        });
        //培训商品相关条件查询
        CompletableFuture<List<String>> trainProductCompletable = CompletableFuture.supplyAsync(() -> {
            TrainProductDto trainProduct = calculationDto.getTrainProduct();
            if (null != trainProduct && CollectionUtils.isNotEmpty(trainProduct.getTrainProductList())) {
                return ehrStaffClient.getStaffTrainProductList(trainProduct);
            }
            return Collections.emptyList();
        });
        //员工指标相关条件查询
        CompletableFuture<List<String>> indicatorCompletable = CompletableFuture.supplyAsync(() -> {
            IndicatorDto indicator = calculationDto.getIndicator();
            if (null != indicator && CollectionUtils.isNotEmpty(indicator.getIndicatorList())) {
                return biTaskClient.getStaffIndicatorList(indicator);
            }
            return Collections.emptyList();
        });

        CompletableFuture<List<String>> all = CompletableFuture.allOf(baseCompletable, workOrderTypeCompletable
                , advertCompletable, mediaCompletable, saleProductCompletable, diseaseCompletable, scheduleCompletable
                , trainProductCompletable, indicatorCompletable).thenApply(x -> {
            try {
                return (List<String>) CollUtil.intersection(baseCompletable.get(), workOrderTypeCompletable.get(), advertCompletable.get()
                        , mediaCompletable.get(), saleProductCompletable.get(), diseaseCompletable.get(), scheduleCompletable.get()
                        , trainProductCompletable.get(), indicatorCompletable.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        });
        return CollectionUtils.isNotEmpty(all.get()) ? all.get().size() : 0;
    }

    @Override
    public ComResponse<Integer> trialStaffNo(long groupId) throws Exception {
        ComResponse<StaffCrowdGroup> staffCrowdGroup = crmStaffClient.getStaffCrowdGroupDTO(groupId);
        if (null == staffCrowdGroup) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        StaffCrowdGroup data = staffCrowdGroup.getData();
        if (null == data) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        return ComResponse.success(this.calculationDto(data.getCalculationDto()));
    }
}
