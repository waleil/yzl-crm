package cn.net.yzl.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.ehr.StaffDetailDto;
import cn.net.yzl.crm.service.StaffLassoService;
import cn.net.yzl.crm.service.micservice.BiTaskClient;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.staff.dto.lasso.*;
import cn.net.yzl.crm.staff.model.mogo.RestPage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
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


    @Override
    public Integer calculationDto(CalculationDto calculationDto) throws Exception {
        if (null == calculationDto) {
            return 0;
        }
        //获取原线程的请求参数
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        //基础信息相关条件查询
        CompletableFuture<List<String>> baseCompletable = CompletableFuture.supplyAsync(() -> {
            Base base = calculationDto.getBase();
            if (null != base && (null != base.getSex() || null != base.getNature() || CollectionUtils.isNotEmpty(base.getWorkplaceList()))) {
                RequestContextHolder.setRequestAttributes(attributes);
                return ehrStaffClient.getStaffBaseInfoList(base);
            }
            return null;
        });
        //工单类型条件查询
        CompletableFuture<List<String>> workOrderTypeCompletable = CompletableFuture.supplyAsync(() -> {
            List<WorkOrderTypeDto> workOrderType = calculationDto.getWorkOrderTypeList();
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
                return null;
            }
            return null;
        });
        //媒体相关条件查询
        CompletableFuture<List<String>> mediaCompletable = CompletableFuture.supplyAsync(() -> {
            List<MediaDto> mediaList = calculationDto.getMediaList();
            if (CollectionUtils.isNotEmpty(mediaList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                return null;
            }
            return null;
        });
        //售卖商品相关条件查询
        CompletableFuture<List<String>> saleProductCompletable = CompletableFuture.supplyAsync(() -> {
            List<SaleProductDto> saleProductList = calculationDto.getSaleProductList();
            if (CollectionUtils.isNotEmpty(saleProductList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                return null;
            }
            return null;
        });
        //病症相关条件查询
        CompletableFuture<List<String>> diseaseCompletable = CompletableFuture.supplyAsync(() -> {
            List<DiseaseDto> diseaseDtoList = calculationDto.getDiseaseList();
            if (CollectionUtils.isNotEmpty(diseaseDtoList)) {
                RequestContextHolder.setRequestAttributes(attributes);
                return null;
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

        CompletableFuture<List<String>> all = CompletableFuture.allOf(baseCompletable, workOrderTypeCompletable
                , advertCompletable, mediaCompletable, saleProductCompletable, diseaseCompletable, scheduleCompletable
                , trainProductCompletable, indicatorCompletable).thenApply(x -> {
            try {
                return (List<String>)intersection(baseCompletable.get(), workOrderTypeCompletable.get(), advertCompletable.get()
                        , mediaCompletable.get(), saleProductCompletable.get(), diseaseCompletable.get(), scheduleCompletable.get()
                        , trainProductCompletable.get(), indicatorCompletable.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        return CollectionUtils.isNotEmpty(all.get()) ? all.get().size() : 0;
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
        if(newColl.size()==1){
            return newColl.get(0);
        }
        if(newColl.size()==2){
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
    public ComResponse<Integer> trialStaffNo(long groupId) throws Exception {
        ComResponse<StaffCrowdGroup> staffCrowdGroup = crmStaffClient.getStaffCrowdGroup(groupId);
        if (null == staffCrowdGroup) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        StaffCrowdGroup data = staffCrowdGroup.getData();
        if (null == data) {
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE);
        }
        return ComResponse.success(this.calculationDto(data.getCalculationDto()));
    }

    @Override
    public ComResponse<RestPage<StaffCrowdGroupListDTO>> getGroupListByPage(String crowdGroupName, Integer status, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
        ComResponse<RestPage<StaffCrowdGroupListDTO>> groupListByPage = crmStaffClient.getGroupListByPage(crowdGroupName, status, startTime, endTime, pageNo, pageSize);
        if (null == groupListByPage) {
            return ComResponse.success();
        }
        RestPage<StaffCrowdGroupListDTO> data = groupListByPage.getData();
        if (null == data) {
            return ComResponse.success();
        }
        List<StaffCrowdGroupListDTO> content = data.getContent();
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
}
