package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.staff.CalculationResult;
import cn.net.yzl.crm.staff.dto.lasso.CalculationDto;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroupListDTO;

import java.util.Date;
import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/1/21 19:21
 * @desc: //TODO  请说明该类的用途
 **/
public interface StaffLassoService {

    List<String> calculationDto(CalculationDto calculationDto, Long id) throws Exception;

    ComResponse<CalculationResult> trialStaffNo(long groupId) throws Exception;

    ComResponse<Page<StaffCrowdGroupListDTO>> getGroupListByPage(String crowdGroupName, Integer status, Date startTime, Date endTime, Integer pageNo, Integer pageSize);

    void taskCalculation();

    CalculationResult calculationUerDetail(List<String> userNoList);
}
