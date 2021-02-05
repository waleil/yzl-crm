package cn.net.yzl.crm.service.impl;

import cn.net.yzl.activity.model.requestModel.CalculateRequest;
import cn.net.yzl.activity.model.requestModel.CheckOrderAmountRequest;
import cn.net.yzl.activity.model.requestModel.ProductDiscountRequest;
import cn.net.yzl.activity.model.requestModel.ProductListDiscountRequest;
import cn.net.yzl.activity.model.responseModel.ProductDiscountResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.service.ActivityService;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liuChangFu
 * @date: 2021/2/4 11:58
 * @desc: //TODO  请说明该类的用途
 **/
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private CrmStaffClient crmStaffClient;

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    MemberFien memberFien;

    @Override
    public ComResponse<List<ProductDiscountResponse>> getProductListDiscount(ProductListDiscountRequest request) {
        Long groupId = this.getGroupIdByUserNo();
        if (null != groupId) {
            request.setGroupId(groupId);
        }
        return activityClient.getProductListDiscount(request);
    }


    @Override
    public ComResponse<ProductDiscountResponse> getProductDiscount(ProductDiscountRequest request) {
        Long groupId = this.getGroupIdByUserNo();
        if (null != groupId) {
            request.setGroupId(groupId);
        }
        return activityClient.getProductDiscount(request);
    }

    @Override
    public ComResponse<BigDecimal> calculate(CalculateRequest request) {
        Long groupId = this.getGroupIdByUserNo();
        if (null != groupId) {
            request.setGroupId(groupId);
        }
        Member member = memberFien.getMemberDefault(request.getMemberCard());
        if (null != member) {
            request.setMemberLevelGrade(member.getMGradeId());
        }
        return activityClient.calculate(request);
    }

    @Override
    public ComResponse<Boolean> checkOrderAmount(CheckOrderAmountRequest request) {
        Long groupId = this.getGroupIdByUserNo();
        if (null != groupId) {
            request.setGroupId(groupId);
        }
        Member member = memberFien.getMemberDefault(request.getMemberCard());
        if (null != member) {
            request.setMemberLevelGrade(member.getMGradeId());
        }
        return activityClient.checkOrderAmount(request);
    }


    private Long getGroupIdByUserNo() {
        // 获取当前登录人
        String userNo = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userNo)) {
            return null;
        }
        // 获取员工群，查看当前员工属于哪个群
        List<StaffCrowdGroup> staffCrowdGroupList = crmStaffClient.getStaffCrowdGroupDefault(0L);
        if (CollectionUtils.isEmpty(staffCrowdGroupList)) {
            return null;
        }
        Long groupId = null;
        for (StaffCrowdGroup staffCrowdGroup : staffCrowdGroupList) {
            if (!CollectionUtils.isEmpty(staffCrowdGroup.getStaffCodeList()) && staffCrowdGroup.getStaffCodeList().contains(userNo)) {
                groupId = staffCrowdGroup.getId();
            }
        }
        return groupId;
    }

}
