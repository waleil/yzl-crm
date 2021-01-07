package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.StaffClient;
import cn.net.yzl.crm.sys.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  员工业务层业务层实现
 */
@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffClient staffClient;

    @Override
    public StaffImageBaseInfoDto getStaffImageBaseInfoByStaffNo(String staffNo) {
        ComResponse<StaffImageBaseInfoDto> ehrBaseInfoResponse = staffClient.getDetailsByNo(staffNo);
        if (ehrBaseInfoResponse==null||ehrBaseInfoResponse.getCode()!=200){
            log.info(".......StaffServiceImpl.getStaffImageBaseInfoByStaffNo()调用ehr获取员工详情接口错误.....");
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE);
        }
        StaffImageBaseInfoDto data = ehrBaseInfoResponse.getData();
        //TODO  员工标签相关
        return data;
    }
}
