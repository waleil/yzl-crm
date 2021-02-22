package cn.net.yzl.crm.service;

import cn.net.yzl.activity.model.requestModel.*;
import cn.net.yzl.activity.model.responseModel.*;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;

import java.math.BigDecimal;

/**
 * @author: liuChangFu
 * @date: 2021/2/4 11:58
 * @desc: //TODO  请说明该类的用途
 **/
public interface ActivityService {
    ComResponse<ProductListDiscountResponse> getProductListDiscount(ProductListDiscountRequest request);

    ComResponse<ProductDiscountResponse> getProductDiscount(ProductDiscountRequest request);

    ComResponse<ProductPriceResponse> calculate(CalculateRequest request);

    ComResponse<MemberAccountResponse> getAccountByMemberCard(String memberCard);

    ComResponse<Page<MemberAccountHistoryResponse>> getAccountHistoryByMemberCard(AccountHistoryRequest request);

    ComResponse<Page<MemberIntegralRecordsResponse>> getMemberIntegralRecords(AccountRequest request);

    ComResponse<Page<MemberRedBagRecordsResponse>> getMemberRedBagRecords(AccountRequest request);

    ComResponse<Page<MemberCouponResponse>> getMemberCoupon(AccountRequest request);
}
