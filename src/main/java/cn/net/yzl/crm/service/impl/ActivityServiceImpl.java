package cn.net.yzl.crm.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.activity.model.requestModel.*;
import cn.net.yzl.activity.model.responseModel.*;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.product.MealClient;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.service.ActivityService;
import cn.net.yzl.crm.service.micservice.ActivityClient;
import cn.net.yzl.crm.service.micservice.CrmStaffClient;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroup;
import cn.net.yzl.product.model.vo.product.dto.MealDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
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
    private ProductClient productClient;

    @Autowired
    private MealClient mealClient;


    @Autowired
    MemberFien memberFien;

    @Override
    public ComResponse<ProductListDiscountResponse> getProductListDiscount(ProductListDiscountRequest request) {
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
    public ComResponse<ProductPriceResponse> calculate(CalculateRequest request) {
        Long groupId = this.getGroupIdByUserNo();
        if (null != groupId) {
            request.setGroupId(groupId);
        }
        Member member = memberFien.getMemberDefault(request.getMemberCard());
        if (null != member) {
            request.setMemberLevelGrade(member.getMGradeId());
        }
        //查询商品的信息
        if(0 == request.getCalculateProductDto().getProductType()){
            ProductDetailVO productDetailVO = productClient.queryProductDetailDefault(request.getCalculateProductDto().getProductCode());
            if(null == productDetailVO){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE,"找不到对应的商品信息");
            }
            request.getCalculateProductDto().setSalePrice(Long.valueOf(productDetailVO.getSalePrice()));
            request.getCalculateProductDto().setLimitDownPrice(Long.valueOf(productDetailVO.getLimitDownPrice()));
        }else{
            //套餐
            MealDTO mealDTO = mealClient.queryProductMealPortrayDefault(request.getCalculateProductDto().getProductCode());
            if(null == mealDTO){
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE,"找不到对应的商品信息");
            }
            request.getCalculateProductDto().setSalePrice(Long.valueOf(mealDTO.getMeal().getPrice()));
            request.getCalculateProductDto().setLimitDownPrice(Long.valueOf(mealDTO.getMeal().getDiscountPrice()));
        }

        return activityClient.calculate(request);
    }


    @Override
    public ComResponse<MemberAccountResponse> getAccountByMemberCard(String memberCard) {
        return activityClient.getAccountByMemberCard(memberCard);
    }

    @Override
    public ComResponse<Page<MemberAccountHistoryResponse>> getAccountHistoryByMemberCard(AccountHistoryRequest request) {
        return activityClient.getAccountHistoryByMemberCard(request);
    }

    @Override
    public ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecords(AccountRequest request) {
        if (null != request.getSearchDate()) {
            if (0 == request.getSearchDate()) {
                Date endTime = new Date();
                Date beginTime = DateUtil.offset(endTime, DateField.MONTH, -3);
                request.setBeginTime(beginTime);
                request.setEndTime(endTime);
            }else{
                Date now = new Date();
                Date endTime = DateUtil.offset(now, DateField.MONTH, -3);
                request.setEndTime(endTime);
            }
        }
        return activityClient.getMemberIntegralRecords(request);
    }

    @Override
    public ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecords(AccountRequest request) {
        if (null != request.getSearchDate()) {
            if (0 == request.getSearchDate()) {
                Date endTime = new Date();
                Date beginTime = DateUtil.offset(endTime, DateField.MONTH, -3);
                request.setBeginTime(beginTime);
                request.setEndTime(endTime);
            }else{
                Date now = new Date();
                Date endTime = DateUtil.offset(now, DateField.MONTH, -3);
                request.setEndTime(endTime);
            }
        }
        return activityClient.getMemberRedBagRecords(request);
    }

    @Override
    public ComResponse<Page<MemberCouponResponse>> getMemberCoupon(AccountRequest request) {
        if (null != request.getSearchDate()) {
            if (0 == request.getSearchDate()) {
                Date endTime = new Date();
                Date beginTime = DateUtil.offset(endTime, DateField.MONTH, -3);
                request.setBeginTime(beginTime);
                request.setEndTime(endTime);
            }else{
                Date now = new Date();
                Date endTime = DateUtil.offset(now, DateField.MONTH, -3);
                request.setEndTime(endTime);
            }
        }
        return activityClient.getMemberCoupon(request);
    }


    private Long getGroupIdByUserNo() {
        // 获取当前登录人
        String userNo = QueryIds.userNo.get();
        if (StringUtils.isEmpty(userNo)) {
            return null;
        }
        // 获取员工群，查看当前员工属于哪个群
        List<StaffCrowdGroup> staffCrowdGroupList = crmStaffClient.getStaffCrowdGroupDefault(0L,0);
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
