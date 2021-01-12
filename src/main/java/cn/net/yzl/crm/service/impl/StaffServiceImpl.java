package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.staff.OrderDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.sys.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  员工业务层业务层实现
 */
@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private CrmStaffClient crmStaffClient;

    @Override
    public StaffImageBaseInfoDto getStaffImageBaseInfoByStaffNo(String staffNo) {
        ComResponse<StaffImageBaseInfoDto> ehrBaseInfoResponse = ehrStaffClient.getDetailsByNo(staffNo);
        if (ehrBaseInfoResponse==null||ehrBaseInfoResponse.getCode()!=200){
            log.info(".......StaffServiceImpl.getStaffImageBaseInfoByStaffNo()调用ehr获取员工详情接口错误.....");
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE);
        }
        StaffImageBaseInfoDto data = ehrBaseInfoResponse.getData();
        // 获取产品优势
        ComResponse<List<String>> basicProductAdvance = crmStaffClient.getBasicProductAdvance(Integer.parseInt(staffNo));
        if (basicProductAdvance.getCode()==200){
            data.setProductAdvanced(basicProductAdvance.getData());
        }

        // 获取产品优势
        ComResponse<List<String>> basicDiseaseAdvance = crmStaffClient.getBasicDiseaseAdvance(Integer.parseInt(staffNo));
        if (basicDiseaseAdvance.getCode()==200){
            data.setDiseaseAdvanced(basicDiseaseAdvance.getData());
        }
        return data;
    }


    @Override
    public Page<StaffProdcutTravelDto> getStaffProductTravel(Integer staffNo, Integer pageNo, Integer pageSize) {
        ComResponse<Page<StaffProdcutTravelDto>> response = crmStaffClient.getStaffProductTravelList(staffNo, pageNo, pageSize);
        if (response.getCode()!=200){
            log.info("......员工画像 获取员工商品旅程信息错误: code=[{}],msg=[{}]",response.getCode(),response.getMessage());
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE);
        }
        return response.getData();
    }


    @Override
    public Page<CustomerDto> getCustomerListByStaffNo(Integer staffNo, Integer pageNo, Integer pageSize) {
        ComResponse<Page<CustomerDto>> response = crmStaffClient.getCustomerList(staffNo, pageNo, pageSize);
        if (response.getCode()!=200){
            log.info("......员工画像 获取员工顾客列表错误: code=[{}],msg=[{}]",response.getCode(),response.getMessage());
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE);
        }
        return response.getData();
    }

    @Override
    public Page<OrderDto> getStaffOrderList(String staffNo) {
        //TODO 获取定单列表
        return null;
    }
}
