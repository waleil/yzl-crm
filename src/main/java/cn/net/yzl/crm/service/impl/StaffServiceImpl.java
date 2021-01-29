package cn.net.yzl.crm.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONObject;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.dto.staff.OrderCriteriaDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.service.StaffService;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.OderListReqDTO;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工业务层业务层实现
 */
@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    private EhrStaffClient ehrStaffClient;

    @Autowired
    private CrmStaffClient crmStaffClient;

    @Autowired
    private OrderSearchClient orderSearchClient;

    @Override
    public StaffImageBaseInfoDto getStaffImageBaseInfoByStaffNo(String staffNo) {
        ComResponse<StaffImageBaseInfoDto> ehrBaseInfoResponse = ehrStaffClient.getDetailsByNo(staffNo);
        if (ehrBaseInfoResponse == null || ehrBaseInfoResponse.getCode() != 200) {
            log.info(".......StaffServiceImpl.getStaffImageBaseInfoByStaffNo()调用ehr获取员工详情接口错误.....");
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE);
        }
        StaffImageBaseInfoDto data = ehrBaseInfoResponse.getData();
        // 获取商品优势
        ComResponse<List<String>> basicProductAdvance = crmStaffClient.getBasicProductAdvance(staffNo);
        if (null != basicProductAdvance && basicProductAdvance.getCode() == 200) {
            data.setProductAdvanced(basicProductAdvance.getData());
        }

        // 获取病症优势
        ComResponse<List<JSONObject>> basicDiseaseAdvance = crmStaffClient.getBasicDiseaseAdvance(staffNo);
        if (null != basicDiseaseAdvance && basicDiseaseAdvance.getCode() == 200) {
            data.setDiseaseAdvanced(basicDiseaseAdvance.getData());
        }

        ComResponse<List<JSONObject>> trainProductRes = ehrStaffClient.selectStaffTrainProduct(staffNo, 5);
        if (trainProductRes.getCode() == 200 && trainProductRes.getData() != null) {
            List<String> trainProductHistory = trainProductRes.getData().stream().map(x -> {
                String productName = x.getStr("productName");
                String gradeName = x.getStr("gradeName");
                return productName + ":" + gradeName;

            }).collect(Collectors.toList());
            data.setTrainProductHistory(trainProductHistory);
        }
        return data;
    }


    @Override
    public Page<StaffProdcutTravelDto> getStaffProductTravel(String staffNo, Integer pageNo, Integer pageSize) {
        ComResponse<Page<StaffProdcutTravelDto>> response = crmStaffClient.getStaffProductTravelList(staffNo, pageNo, pageSize);
        if (response.getCode() != 200) {
            log.info("......员工画像 获取员工商品旅程信息错误: code=[{}],msg=[{}]", response.getCode(), response.getMessage());
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE);
        }
        return response.getData();
    }


    @Override
    public Page<CustomerDto> getCustomerListByStaffNo(String staffNo, Integer pageNo, Integer pageSize) {
        ComResponse<Page<CustomerDto>> response = crmStaffClient.getCustomerList(staffNo, pageNo, pageSize);
        if (response.getCode() != 200) {
            log.info("......员工画像 获取员工顾客列表错误: code=[{}],msg=[{}]", response.getCode(), response.getMessage());
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE);
        }
        return response.getData();
    }

    @Override
    public ComResponse<Page<OderListResDTO>> getStaffOrderList(OrderCriteriaDto req) {
        OderListReqDTO reqDTO = new OderListReqDTO();
        reqDTO.setPageNo(req.getPageNo());
        reqDTO.setPageSize(req.getPageSize());
        reqDTO.setStaffCode(req.getStaffNo());
        reqDTO.setOrderStatus(req.getStatus());
        if(null!=req.getTimeType()){
            switch (req.getTimeType()) {
                case 1:
                    reqDTO.setStartTime(LocalDateTimeUtil.format(LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(1)), DatePattern.NORM_DATETIME_FORMATTER));
                    break;
                case 2:
                    reqDTO.setStartTime(LocalDateTimeUtil.format(LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(7)), DatePattern.NORM_DATETIME_FORMATTER));
                    break;
                case 3:
                    reqDTO.setStartTime(LocalDateTimeUtil.format(LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(15)), DatePattern.NORM_DATETIME_FORMATTER));
                    break;
                case 4:
                    reqDTO.setStartTime(LocalDateTimeUtil.format(LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(30)), DatePattern.NORM_DATETIME_FORMATTER));
                    break;
                default:
            }
        }
        ComResponse<Page<OderListResDTO>> response = orderSearchClient.selectOrderList(reqDTO);
        return response;
    }


    /**
     * 根据员工工号数组获取员工信息
     * @param list
     * @return
     */
    @Override
   public ComResponse<List<StaffDetail>> getDetailsListByNo(List<String> list) {
        return ehrStaffClient.getDetailsListByNo(list);
    }
}
