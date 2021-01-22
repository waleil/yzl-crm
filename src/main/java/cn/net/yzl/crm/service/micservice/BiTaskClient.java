package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.dto.biTask.Indicators;
import cn.net.yzl.crm.staff.dto.lasso.Indicator;
import cn.net.yzl.crm.staff.dto.lasso.IndicatorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liuChangFu
 * @date: 2021/1/18 13:27
 * @desc: //TODO  请说明该类的用途
 **/
@FeignClient(name = "BiTask", url = "${api.gateway.url}/taskDB")
public interface BiTaskClient {

    @GetMapping("/indicatorsSetting/getBiIndicatorsSettingList")
    ComResponse<Page<Indicators>> getBiIndicatorsSettingList(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam("indicatorsDomainType") Integer indicatorsDomainType);



    @GetMapping("/staffTargetTask/getStaffCodeByIndicatorsSettingId")
    ComResponse<List<String>> getStaffCodeByIndicatorsSettingId(@RequestParam("taskCycleTime") String taskCycleTime,
                                                                @RequestParam("achieveIndicatorsSettingIds") List<Integer> achieveIndicatorsSettingIds,
                                                                @RequestParam("noAchieveIndicatorsSettingIds") List<Integer> noAchieveIndicatorsSettingIds);

    default List<String> getStaffIndicatorList(IndicatorDto indicatorDto) {
        List<Indicator> indicatorList = indicatorDto.getIndicatorList();
        //是否完成 0：未完成，1：已完成
        Integer isDone = indicatorDto.getIsDone();
        //不满足的
        List<Integer> disEnable = indicatorList.stream().filter(indicator -> 0 == indicator.getEnabled())
                .map(Indicator::getIndicatorsSettingId).collect(Collectors.toList());
        //满足的
        List<Integer> enable = indicatorList.stream().filter(indicator -> 1 == indicator.getEnabled())
                .map(Indicator::getIndicatorsSettingId).collect(Collectors.toList());

        // 满足已完成 或者 不满足未完成的  相当于完成的
        List<Integer> inIds = (1 == isDone ? enable : disEnable);
        // 满足未完成 或者 不满足已完成的 相当于未完成的
        List<Integer> notInIds = (0 == isDone ? enable : disEnable);
        String taskCycleTime = DateFormatUtil.dateToString(new Date(), DateFormatUtil.MONTH_FORMAT);

        ComResponse<List<String>> staffCodeByIndicatorsSettingId = getStaffCodeByIndicatorsSettingId(taskCycleTime, inIds, notInIds);
        if (null != staffCodeByIndicatorsSettingId && staffCodeByIndicatorsSettingId.getCode() == 200) {
            return staffCodeByIndicatorsSettingId.getData();
        }
        return Collections.emptyList();
    }
}
