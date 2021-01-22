package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.staff.dto.lasso.CalculationDto;

import java.util.concurrent.ExecutionException;

/**
 * @author: liuChangFu
 * @date: 2021/1/21 19:21
 * @desc: //TODO  请说明该类的用途
 **/
public interface StaffLassoService {

    Integer calculationDto(CalculationDto calculationDto) throws ExecutionException, Exception;

    ComResponse<Integer> trialStaffNo(long groupId) throws Exception;
}
