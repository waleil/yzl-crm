package cn.net.yzl.crm.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.dto.staff.OrderDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OrderListReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    private  OrderSearchClient orderSearchClient;

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
    public Page<OrderDto> getStaffOrderList(String staffNo, Integer timeType, Integer status, Integer pageNo, Integer pageSize) {
        OrderListReqDTO reqDTO = new OrderListReqDTO();
        reqDTO.setPageNo(pageNo);
        reqDTO.setPageSize(pageSize);
        reqDTO.setCreateCode(staffNo);
//        switch (timeType){
//            case 1:
//                reqDTO.setStartTime(Timestamp.from(LocalDateTimeUtil.beginOfDay(LocalDateTime.now()).toInstant(ZoneId.systemDefault())));
//                break;
//        }
//        reqDTO.setStartTime();
//        orderSearchClient.selectOrderList(OrderListReqDTO)
        return null;
    }
}
